package gov.nasa.jpf.symbc.mdb.bytecode.collectionarray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.Helper;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.schema.Schema;
import gov.nasa.jpf.symbc.mdb.schema.Schema.SchemaVersion;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection.mData;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument.mField;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.RealExpression;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.string.StringComparator;
import gov.nasa.jpf.symbc.string.StringConstant;
import gov.nasa.jpf.symbc.string.StringExpression;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Heap;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.*;


public class GETFIELD extends gov.nasa.jpf.jvm.bytecode.GETFIELD {
	public static boolean useSchemaVersionInitializationHeuristic;
	protected final Instruction delegateTo;
	// Maps a lazily initialized MDocument to the mData-entry containing it
	protected static Map<Integer, Integer> mdocToMData = new HashMap<>(); /// TODO Store in MDocument
	
	
	public GETFIELD(
			String fieldName, 
			String clsName, 
			String fieldDescriptor, 
			Instruction delegateTo) {
		super(fieldName, clsName, fieldDescriptor);
		this.delegateTo = delegateTo;
	}
	
	@Override
	public void setMethodInfo(MethodInfo mi) {
		this.mi = mi;
		delegateTo.setMethodInfo(mi);
	}
	
	@Override 
	public void setLocation(int insnIndex, int position) {
		this.position = position;
		this.insnIndex = insnIndex;
		delegateTo.setLocation(insnIndex, position);
	}
	
	@Override
	public Instruction execute(ThreadInfo ti) {
		/* 23-04-2020 Taken and adapted from gov.nasa.jpf.symbc.bytecode.GETFIELD
		 * to fit the behavior of querying and working on Collections in MongoDB:
		 * Hendrik Winkelmann
		 */
		
		HeapChoiceGenerator prevHeapCG;
		// Lazy is assumed for MDocument. 
		StackFrame frame = ti.getModifiableTopFrame();
	    int objRef = frame.peek(); // don't pop yet, we might re-enter
	    lastThis = objRef;
	    if (objRef == MJIEnv.NULL) {
	    	return ti.createAndThrowException("java.lang.NullPointerException",
	                                        "referencing field '" + fname + "' on null object");
	    }
		ElementInfo ei = ti.getModifiableElementInfo(lastThis);
		FieldInfo fi = getFieldInfo(); 
		assert fi != null;
		Object attr = ei.getFieldAttr(fi);
		
		int currentChoice = -1;
		HeapChoiceGenerator thisHeapCG;
		ClassInfo typeClassInfo = fi.getTypeClassInfo();
		
		/* MCollection.data-field auto-initialization */
		if (fi.equals(dataFi) &&
				ei.getReferenceField(dataFi) == MJIEnv.NULL) {
			// Data will immediately be initialized to non-null, no matter what.
			frame.pop();
			assert typeClassInfo.equals(mDataCi);
			ElementInfo mDataEi = ti.getVM().getHeap().newObject(mDataCi, ti);
			int daIndex = mDataEi.getObjectRef();
			
			mDataEi.setFieldAttr(curFi, new SymbolicInteger());
			mDataEi.setFieldAttr(tailFi, new SymbolicInteger());
			mDataEi.setIntField(depthFi, 0);
			mDataEi.setReferenceField(enclosingMCollectionFi, ei.getObjectRef());
			
			ei.setReferenceField(dataFi, daIndex);
			ei.setFieldAttr(dataFi, null);
			frame.push(daIndex);
			return getNext(ti);
		}
		
		// If the field is no reference there is no need for lazy initialization.
		// If there is no attribute
		if (!fi.isReference() || attr == null) {
			return super.execute(ti);
		}
		
		/* Lazy-initialization of mData-cur-field. */
		if (fi.equals(curFi)) {
			assert typeClassInfo.equals(MDocumentCi);
			if (!ti.isFirstStepInsn()) {
				PCChoiceGenerator pcg = ti.getVM().getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
				if (pcg == null) {
					pcg = new PCChoiceGenerator(1);
					ti.getVM().getSystemState().setNextChoiceGenerator(pcg); // Initialize for future conditions on this MDocument
				}
				// TODO We do not regard abstract classes for now
				thisHeapCG = new HeapChoiceGenerator(2); // null and new
				ti.getVM().getSystemState().setNextChoiceGenerator(thisHeapCG);
				return this;
			} else {
				frame.pop();
				thisHeapCG = ti.getVM().getSystemState().getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
				currentChoice = thisHeapCG.getNextChoice();
			}
			assert currentChoice == 0 || currentChoice == 1;
			
			PathCondition pcHeap;
			SymbolicInputHeap symInputHeap;
			
			prevHeapCG = thisHeapCG.getPreviousChoiceGeneratorOfType(HeapChoiceGenerator.class);
			if (prevHeapCG == null) {
				pcHeap = new PathCondition();
				symInputHeap = new SymbolicInputHeap();
			} else {
				pcHeap = prevHeapCG.getCurrentPCheap();
				symInputHeap = prevHeapCG.getCurrentSymInputHeap();
			}
			
			assert pcHeap != null;
			assert symInputHeap != null;
			
			int daIndex;
			if (currentChoice == 0) { // Insert null object
				pcHeap._addDet(Comparator.EQ, (SymbolicInteger) attr, new IntegerConstant(-1));
				daIndex = MJIEnv.NULL;
			} else { // Initialize MDocument-instance
				// Initialize with symbolic references so that later on, a concrete schema can be used to initialize fields.
				daIndex = ti.getVM().getHeap().newObject(typeClassInfo, ti).getObjectRef();
				pcHeap._addDet(Comparator.EQ, (SymbolicInteger) attr, new IntegerConstant(daIndex));
				ElementInfo mdocEi = ti.getModifiableElementInfo(daIndex);
				mdocEi.setBooleanField(isDeletedFi, false);
				mdocEi.setBooleanField(isInsertedFi, false);
				mdocEi.setBooleanField(isUpdatedFi, false);
				mdocEi.setFieldAttr(fieldsFi, new SymbolicInteger());
				mdocToMData.put(mdocEi.getObjectRef(), ei.getObjectRef());
			}
			ei.setReferenceField(curFi, daIndex);
			ei.setFieldAttr(curFi, null);
			frame.push(daIndex);
			thisHeapCG.setCurrentPCheap(pcHeap);
			thisHeapCG.setCurrentSymInputHeap(symInputHeap);
			return getNext(ti);
		}
		
		/* Lazy-initialization of mData-tail-field. */
		if (fi.equals(tailFi)) {
			assert typeClassInfo.equals(mDataCi);
			frame.pop();
			int currentDepth = ei.getIntField(depthFi);
			ElementInfo enclosingMCollection = ei.getObjectField(enclosingMCollectionFi.getName());
			int allowedDepth = enclosingMCollection.getIntField(maxNumberInitialDocumentsFi);
			int daIndex;
			if (currentDepth < allowedDepth - 1) {
				// Initialize mData-instance
				// Initialize with symbolic references so that later on, a concrete schema can be used to initialize fields.
				daIndex = ti.getVM().getHeap().newObject(typeClassInfo, ti).getObjectRef();
				ElementInfo mDataEi = ti.getModifiableElementInfo(daIndex);
				mDataEi.setFieldAttr(curFi, new SymbolicInteger());
				mDataEi.setFieldAttr(tailFi, new SymbolicInteger());
				mDataEi.setIntField(depthFi, currentDepth + 1);
				mDataEi.setReferenceField(enclosingMCollectionFi, ei.getEnclosingElementInfo().getObjectRef());
			} else {
				daIndex = MJIEnv.NULL;
			}
			ei.setReferenceField(tailFi, daIndex);
			ei.setFieldAttr(tailFi, null);
			frame.push(daIndex);
			return getNext(ti);
		}
		
		/* Initialize fields of MDocument according to schema */
		if (fi.equals(fieldsFi)) {
			if (!ti.isFirstStepInsn()) {
				int numberChoices = getNumberSchemaVersions(ei);
				if (numberChoices > 0) {
					thisHeapCG = new HeapChoiceGenerator(numberChoices);
					ti.getVM().getSystemState().setNextChoiceGenerator(thisHeapCG);
				} else {
					ti.getVM().getSystemState().setIgnored(true);
				}
				return this;
				// TODO we do not evaluate abstract classes for now
			} else {
				frame.pop();
				thisHeapCG = ti.getVM().getSystemState().getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
				assert (thisHeapCG !=null && thisHeapCG instanceof HeapChoiceGenerator) :
					  "expected HeapChoiceGenerator, got: " + thisHeapCG;
				currentChoice = thisHeapCG.getNextChoice();
				assert currentChoice >= 0;
			}
			PathCondition pcHeap;
			SymbolicInputHeap symInputHeap;
			prevHeapCG = thisHeapCG.getPreviousChoiceGeneratorOfType(HeapChoiceGenerator.class);
			if (prevHeapCG == null) {
				pcHeap = new PathCondition();
				symInputHeap = new SymbolicInputHeap();
			} else {
				pcHeap = prevHeapCG.getCurrentPCheap();
				symInputHeap = prevHeapCG.getCurrentSymInputHeap();
			}
			
			assert pcHeap != null;
			assert symInputHeap != null;
			
			SchemaVersion sv = getNextSchemaVersion(ei, currentChoice, ti);
			generateMDocumentForSchema(ti, frame, thisHeapCG, (SymbolicInteger) attr, ei, pcHeap, symInputHeap, sv.structure, sv.getVersionNumber());
			
			return getNext(ti);
		} else {
			return delegateTo.execute(ti);
		}
	}
	
	protected int getNumberSchemaVersions(ElementInfo ei) {
		if (useSchemaVersionInitializationHeuristic) {
			return getUnusedSchemaVersions(ei).size();
		} else {
			return getSchema(ei).numberVersions();
		}
	}
	
	protected SchemaVersion getNextSchemaVersion(ElementInfo ei, int currentChoice, ThreadInfo ti) {
		if (useSchemaVersionInitializationHeuristic) {
			List<SchemaVersion> unusedSchemaVersions = getUnusedSchemaVersions(ei);
			if (currentChoice >= unusedSchemaVersions.size()) {
				ti.getVM().getSystemState().setIgnored(true);
			}
			return unusedSchemaVersions.get(currentChoice);
		} else {
			return getSchema(ei).getVersion(currentChoice);
		}
	}

	protected List<SchemaVersion> getUnusedSchemaVersions(ElementInfo ei) {
		List<SchemaVersion> allVersions = getSchema(ei).getVersions();
		ArrayList<SchemaVersion> versions = new ArrayList<>(allVersions);
		
		ElementInfo mcEi = ei.getObjectField(containingMCollectionFi.getName());
		ElementInfo currentMdataEi = mcEi.getObjectField(dataFi.getName());
		ElementInfo currentMdocEi = currentMdataEi.getObjectField(curFi.getName());
		
		while (currentMdataEi != null && currentMdocEi != null) {
			if (currentMdocEi != ei) {
				int schemaVersion = currentMdocEi.getIntField(schemaVersionFi);
				if (schemaVersion >= 0) { // Default for MDocument.schemaVersion is -1.
					versions.set(schemaVersion, null);
				}
			}
			currentMdataEi = currentMdataEi.getObjectField(tailFi.getName());
			 if (currentMdataEi == null) {
				 break;
			 }
			currentMdocEi = currentMdataEi.getObjectField(curFi.getName());
		}
		
		for (int i = 0; i < versions.size(); i++) {
			if (versions.get(i) == null) {
				versions.remove(i);
				i--;
			}
		}
		
		return versions;
	}
	
	protected static int dummyStringCount = 0;
	protected int treatWithDummyString(Heap heap, ThreadInfo ti) {
		ElementInfo dummyStringEi = heap.newString("DummyString#"+dummyStringCount++, ti);
		ti.getHeap().registerPinDown(dummyStringEi.getObjectRef());
		return dummyStringEi.getObjectRef();
	}
	
	protected Schema getSchema(ElementInfo ei) {
		String collectionName = ei.getStringField(collectionNameFi.getName());
		String pojoName = ei.getStringField(pojoNameFi.getName());
		return Schema.get(collectionName, pojoName);
	}
	
	protected int getNextMObjectIdAndIncr(
			ThreadInfo ti) {
		ElementInfo mobjectEi = ti.getHeap().newObject(MObjectIdCi, ti);
		ti.getHeap().registerPinDown(mobjectEi.getObjectRef());
		int daIndex = mobjectEi.getObjectRef();
		mobjectEi.setIntField(timestampFi, 0);
		mobjectEi.setIntField(machineIdentifierFi, 0);
		mobjectEi.setShortField(processIdentifierFi, (short) 0);
		ElementInfo staticMObjectEi = MObjectIdCi.getModifiableStaticElementInfo();
		if (staticMObjectEi == null) {
			MObjectIdCi.registerClass(ti);
			staticMObjectEi = MObjectIdCi.getStaticElementInfo();
		}
		int staticNextCounter = staticMObjectEi.getIntField(staticNextCounterFi);
		mobjectEi.setIntField(counterFi, staticNextCounter);
		mobjectEi.setFieldAttr(timestampFi, null);
		mobjectEi.setFieldAttr(machineIdentifierFi, null);
		mobjectEi.setFieldAttr(processIdentifierFi, null);
		mobjectEi.setFieldAttr(counterFi, null);
		staticMObjectEi.setIntField(staticNextCounterFi, staticNextCounter + 1);
		staticMObjectEi.setFieldAttr(staticNextCounterFi, null);
		return daIndex;
	}
	
	
	protected void generateMDocumentForSchema(
			ThreadInfo ti,
			StackFrame frame,
			HeapChoiceGenerator thisHeapCG,
			SymbolicInteger attr,
			ElementInfo ei, 
			PathCondition pcHeap,
			SymbolicInputHeap symInputHeap,
			Map<String, Object> structure,
			int schemaVersion) { // Is -1 if the MDocument is embedded.
		/* Step 1: Init array of fields in heap. The array can be
		 * concrete, as we do know its length and initialize the objects it contains
		 * immediately.
		 */
		// 27-04-2020 Inspired by gov.nasa.jpf.jvm.bytecode.ANEWARRAY: Hendrik Winkelmann
		int arrayLength = structure.size();
		Heap heap = ti.getHeap();
		ElementInfo eiArray = heap.newArray(
				mFieldCi.getType(),
				arrayLength, 
				ti
		);
		ti.getHeap().registerPinDown(eiArray.getObjectRef());
		int eiArrayIndex = eiArray.getObjectRef();
		ei.setReferenceField(fieldsFi, eiArrayIndex);
		ei.setFieldAttr(fieldsFi, null); // Avoid re-initializing field again
		SymbolicInteger eiArrayAttr = (SymbolicInteger) attr;
		
		// Step 2: For each of the attributes defined in the schema, create a mField-object and add it to the array
		// 27-04-2020: Inspired by gov.nasa.jpf.symbc.heap.Helper.addNewArrayHeapNode(...): Hendrik Winkelmann
		int mFieldNumber = 0;
		// MongoDB's automatic POJO-mapping assumes that an attribute named id or similar replaces ObjectId as the _id.
		boolean containsIdAlternative = false;
		for (Map.Entry<String, Object> svf : structure.entrySet()) {
			assert svf.getKey() != null;
			assert svf.getValue() != null;
			String keyName = svf.getKey();
			// Step 2.1: Create new mFieldReference
			HeapNode[] prevMFields= symInputHeap.getNodesOfType(mFieldCi);
			int numMFields = prevMFields.length;
			int newMFieldRef = Helper.addNewHeapNode(mFieldCi, ti, eiArrayAttr, pcHeap, symInputHeap, numMFields, prevMFields, false);
			SymbolicInteger newFieldAttr = symInputHeap.getNode(newMFieldRef);
			assert newFieldAttr != null;
			ElementInfo mFieldEi = ti.getModifiableElementInfo(newMFieldRef);
			if (Utility.isIdAlternative(keyName)) {
				assert !containsIdAlternative;
				keyName = "_id";
				containsIdAlternative = true;
			}
			// Step 2.2: Create new key-value-pair
			ElementInfo stringValEiForKey = heap.newString(keyName, ti);
			ti.getHeap().registerPinDown(stringValEiForKey.getObjectRef());
			mFieldEi.setReferenceField(keyFi, stringValEiForKey.getObjectRef());
			mFieldEi.setFieldAttr(keyFi, null);
			mFieldEi.setFieldAttr(valFi, null);
			// Register a new reference for the value field. This value is our only symbolic part.
			// Either the value is a primitive or another MDocument.
			int mFieldValRef;
			if (svf.getValue() instanceof Class<?>) {		
				Class<?> eiClass =  (Class<?>) svf.getValue();
				assert Utility.isWrappingOrString(eiClass) : "Only primitive classes are supported.";
				ClassInfo mFieldValCi = 
						ClassLoaderInfo.getCurrentResolvedClassInfo(eiClass.getName()); // Get ClassInfo from type of attribute
				if (!svf.getValue().equals(String.class)) {	
					mFieldValRef = heap.newObject(mFieldValCi, ti).getObjectRef();
					ElementInfo wrappingPrimitive = heap.get(mFieldValRef);
					FieldInfo valueFi = wrappingPrimitive.getClassInfo().getDeclaredInstanceField("value");
					if (eiClass.equals(Float.class) || eiClass.equals(Double.class)) {
						wrappingPrimitive.setFieldAttr(valueFi, new SymbolicReal());
					} else {
						wrappingPrimitive.setFieldAttr(valueFi, new SymbolicInteger());
					}
				} else {
					mFieldValRef = treatWithDummyString(heap, ti);
					mFieldEi.setFieldAttr(valFi, new StringSymbolic());
				}
			} else if (svf.getValue() instanceof Map<?, ?>) {
				// In case it is a nested Map, an embedded MDocument must be initialized
				ElementInfo nestedEi = heap.newObject(MDocumentCi, ti);
				ti.getHeap().registerPinDown(nestedEi.getObjectRef());
				nestedEi.setFieldAttr(fieldsFi, new SymbolicInteger());
				mFieldValRef = nestedEi.getObjectRef();
				generateMDocumentForSchema(ti,frame,thisHeapCG,attr,nestedEi,pcHeap,symInputHeap,(Map<String, Object>) svf.getValue(), -1);				
			} else {
				throw new RuntimeException("An illegal value has been found in the schema: " + svf.getValue());
			}
			mFieldEi.setReferenceField(valFi, mFieldValRef);
			// Step 3: Add key-value pair to corresponding position in array
			eiArray.setReferenceElement(mFieldNumber, newMFieldRef);
			mFieldNumber++;
		}
		thisHeapCG.setCurrentPCheap(pcHeap);
		thisHeapCG.setCurrentSymInputHeap(symInputHeap);
		if (schemaVersion >= 0) {
			if (!containsIdAlternative) { 
				int mobjectIdRef = getNextMObjectIdAndIncr(ti);
				ei.setReferenceField(_idFi, mobjectIdRef);
			} else {
				// Due to the way we initialize MObjectIds, this has not to be done for them, only for alternatives:
				// TODO Fixed insertions of ObjectIds should still trigger errors.
				restrictIdAlternativeValue(ei, heap, ti);
			}
			ei.setIntField(schemaVersionFi, schemaVersion);
			// Finally push the eiArrayIndex
			frame.pushRef(eiArrayIndex);
		}
	}
	
	protected void generateStringField(ThreadInfo ti, ElementInfo ownerEi, FieldInfo stringFi, String value) {
		ElementInfo stringObjectEi = ti.getHeap().newString(value, ti);
		ti.getHeap().registerPinDown(stringObjectEi.getObjectRef());
		ownerEi.setReferenceField(stringFi, stringObjectEi.getObjectRef());
	}
	
	protected void restrictIdAlternativeValue(ElementInfo mDocEi, Heap heap, ThreadInfo ti) {
		ElementInfo mDataEi = heap.get(mdocToMData.get(mDocEi.getObjectRef()));
		assert mDataEi != null && mDataEi.getClassInfo().equals(mDataCi);
		ElementInfo mCollectionEi = mDataEi.getObjectField(enclosingMCollectionFi.getName());
		List<Expression> uniqueAlternativeIdExpressions = getUniqueIdExprs(mCollectionEi, heap);
		Expression alternativeMDocEiId = getAlternativeMDocEiIdExpr(mDocEi, heap);
		restrictUniqueValueOfMDoc(alternativeMDocEiId, uniqueAlternativeIdExpressions, ti);
	}
	
	protected List<Expression> getUniqueIdExprs(ElementInfo mCollectionEi, Heap heap) {
		ElementInfo mDataEi = heap.get(mCollectionEi.getReferenceField(dataFi));
		
		ElementInfo curMDocEi;
		List<Expression> result = new ArrayList<>();
		
		while (mDataEi != null) {
			curMDocEi = heap.get(mDataEi.getReferenceField(curFi));
			if (curMDocEi == null) { // Remaining list is empty.
				break;
			}
			if (curMDocEi.getBooleanField(isInsertedFi)) { // End of lazily initialized entities.
				break;
			}
			Expression id = getAlternativeMDocEiIdExpr(curMDocEi, heap);
			if (id != null) { // Else MDocument.fields was not yet initialized
				result.add(id);
			}
			mDataEi = heap.get(mDataEi.getReferenceField(tailFi));
		}
		
		return result;
	}
	
	protected Expression getAlternativeMDocEiIdExpr(ElementInfo mDocEi, Heap heap) {
		ElementInfo fieldsArray = heap.get(mDocEi.getReferenceField(fieldsFi));
		if (fieldsArray == null) {
			return null; // Not yet initialized
		} 
		int[] arrayRefs = fieldsArray.asReferenceArray();
		
		for (int ref : arrayRefs) {
			ElementInfo mFieldEi = heap.get(ref);
			String keyValue = heap.get(mFieldEi.getReferenceField(keyFi)).asString();
			if (Utility.isIdAlternative(keyValue)) {
				ElementInfo alternativeEi = heap.get(mFieldEi.getReferenceField(valFi));
				Expression alternativeAttr = getExpressionForMFieldEi(mFieldEi, alternativeEi);
				assert alternativeEi.isBoxObject() || alternativeEi.isStringObject();
				if (alternativeAttr == null) {
					return getConstantValueAsExpression(alternativeEi);
				} else {
					return alternativeAttr;
				}
			}
		}
		throw new IllegalStateException("This method should only be called if it has been checked, that the MDocument at hand contains an alternative.");
	}
	
	protected Expression getExpressionForMFieldEi(ElementInfo mFieldEi, ElementInfo mFieldValEi) {
		if (mFieldValEi.getClassInfo().equals(IntegerCi)) {
			return (Expression) mFieldEi.getObjectField(valFi.getName()).getFieldAttr(integerValueFi);
		} else {
			return (Expression) mFieldEi.getFieldAttr(valFi);
		}
	}
	
	protected Expression getConstantValueAsExpression(ElementInfo alternativeEi) {
		assert alternativeEi != null : "Alternative id must not be null";
		if (alternativeEi.isStringObject()) {
			return new StringConstant(alternativeEi.asString());
		} else if (alternativeEi.isBoxObject()) {
			String valFieldEiCiName = alternativeEi.getClassInfo().getName();
			if (valFieldEiCiName.equals(Integer.class.getName())) {
				return new IntegerConstant((Integer) alternativeEi.asBoxObject());
			} else if (valFieldEiCiName.equals(Long.class.getName())) {
				return new IntegerConstant((Long) alternativeEi.asBoxObject());
			}
		}
		throw new RuntimeException("Only Strings and integer/long box objects are supported as IDs.");
	}
	
	protected void restrictUniqueValueOfMDoc(Expression expression, List<Expression> prevUniqueValues, ThreadInfo th) {
		// 29-06-2020 Inspired by SymbolicStringHandler#handleBooleanStringInstructions(...) : Hendrik Winkelmann
		PCChoiceGenerator cg = th.getVM().getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
		PathCondition pc = cg.getCurrentPC();

		assert pc != null || prevUniqueValues.size() == 0; // Is taken care of during the lazy initialization of MDocument via the cur-field
		for (Object obj : prevUniqueValues) {
			assert obj != null;
			if (obj == expression) {
				continue;
			}
			if (expression instanceof StringExpression) {
				// The else-case should also be allowed, yet, does not have an effect.
				if (obj instanceof StringExpression) {
					pc.spc._addDet(StringComparator.NOTEQUALS, (StringExpression) obj, (StringExpression) expression);
				}
			} else if (expression instanceof IntegerExpression) {
				if (obj instanceof IntegerExpression) {
					pc._addDet(Comparator.NE, (IntegerExpression) obj, (IntegerExpression) expression);
				}
			} else if (expression instanceof RealExpression) {
				throw new RuntimeException("RealExpression is not supported to be an ID.");
				/*if (obj instanceof RealExpression) {
					pc._addDet(Comparator.NE, (RealExpression) obj, (RealExpression) expression);
				}*/
			}
		}
		
		if (!pc.simplify()) {
			th.getVM().getSystemState().setIgnored(true);
		} else {
			cg.setCurrentPC(pc);
		}
		
	}
}