package gov.nasa.jpf.symbc.mdb.tcg.test_method_generator;

import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility._idFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.curFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.dataFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.fieldsFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.isDeletedFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.isInsertedFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.isUpdatedFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.keyFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.nameFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.originalVersionFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.tailFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.valFi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;
import gov.nasa.jpf.symbc.mdb.tcg.TestGenUtil;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;

public abstract class MDBTestMethodGenerator extends StdTestMethodGenerator {
	
	protected final boolean circumventWrappingTestDriver;
	protected final Map<ElementInfo, ElementInfo> mCollectionToOriginalMock = new HashMap<>();
	protected List<String> excludeTypeFromMethodCallParameters = new ArrayList<>();
	
	public MDBTestMethodGenerator(boolean circumventWrappingTestDriver) {
		super();
		this.specialCases.add(MCollection.class);
		this.specialCases.add(MObjectId.class);
		this.specialCases.add(MDocument.class);
		this.circumventWrappingTestDriver = circumventWrappingTestDriver;
	}
	
	@Override
	public void clear() {
		super.clear();
		this.mCollectionToOriginalMock.clear();
	}
	
	public String treatSpecialCase(StringBuilder sb, ElementInfo ei) {
		ClassInfo ci = ei.getClassInfo();
		String ciName = ci.getName();
		if (ciName.equals(MCollection.class.getName())) {
			throw new UnsupportedOperationException("MCollection instances should only be "
					+ "accessed while generating the content for the original database mock.");
		} else if (ciName.equals(MObjectId.class.getName())) {
			return treatMObjectId(sb, ei);
		} else if (ciName.equals(MDocument.class.getName())) {
			return treatMDocument(sb, ei, true);
		} else {
			return super.treatSpecialCase(sb, ei);
		}
	}
	
	
	/**
	 * Generate the content of the database using the mock entity (originalDataCollection) 
	 * ElementInfo and the MCollection ElemenInfo. 
	 */
	protected String treatOriginalWithMCol(StringBuilder sb, ElementInfo originalDataCollection, ElementInfo mcolEi) {
		String collectionArgName = getNameForEi(originalDataCollection);
		if (collectionArgName != null) {
			return collectionArgName;
		}
		collectionArgName = getNameForEi(mcolEi);
		if (collectionArgName != null) {
			return collectionArgName;
		}
		collectionArgName = generateDataCollectionForInput(sb, originalDataCollection, mcolEi);
		addEiToName(originalDataCollection, collectionArgName);
		addEiToName(mcolEi, collectionArgName);
		mCollectionToOriginalMock.put(mcolEi, originalDataCollection);
		ElementInfo dataEi = mcolEi.getObjectField(dataFi.getName());
		insertMData(sb, collectionArgName, originalDataCollection, dataEi);
		return collectionArgName;
	}
	
	protected abstract String treatMObjectId(StringBuilder sb, ElementInfo mobjectIdEi);

	protected abstract String generateDataCollectionForInput(StringBuilder sb, ElementInfo originalDataCollection, ElementInfo mcolEi);
	
	protected abstract String generateDataCollectionForAssertions(StringBuilder sb, ElementInfo originalDataCollection, ElementInfo mcolEi);

	protected abstract String generateEntityForMDocumentValues(StringBuilder sb, LinkedHashMap<String, String> mdocEiValues);

	protected abstract String generateInsertStatementForDataCollection(String collectionArgName, String databaseElementArgName, ElementInfo originalDataCollection);
	
	protected abstract String findOneElementWithIdFunction(String mdocArgName);
	
	protected abstract String countDocumentsInDataCollectionFunction();
	
	protected void insertMData(StringBuilder sb, String collectionArgName, ElementInfo originalDataCollection, ElementInfo dataEi) {
		if (dataEi == null) {
			return;
		}
		ElementInfo curEi = dataEi.getObjectField(curFi.getName());
		if (curEi != null) {
			if (!curEi.getBooleanField(isInsertedFi)) {
				String curArgName = treatMDocument(sb, curEi, false);
				sb.append(generateInsertStatementForDataCollection(collectionArgName, curArgName, originalDataCollection)).append(S).append(LB);
			}
			ElementInfo tailDataEi = dataEi.getObjectField(tailFi.getName());
			if (tailDataEi != null) {
				insertMData(sb, collectionArgName, originalDataCollection, tailDataEi);
			}
		}
	}
	
	protected String treatMDocument(StringBuilder sb, ElementInfo curEi, boolean generateAlteredVersion) {
		if (!generateAlteredVersion) {
			curEi = curEi.getBooleanField(isUpdatedFi) ? curEi.getObjectField(originalVersionFi.getName()) : curEi;
		}
		String curEiName = this.getNameForEi(curEi);
		if (curEiName != null) {
			return curEiName;
		}
		LinkedHashMap<String, String> mdocEiValues = getKeyValuePairsFromMDocument(sb, curEi);
		String mdocName = generateEntityForMDocumentValues(sb, mdocEiValues);
		addEiToName(curEi, mdocName);
		return mdocName;
	}
	
	protected LinkedHashMap<String, String> getKeyValuePairsFromMDocument(StringBuilder sb, ElementInfo ei) {
		assert ei != null;
		LinkedHashMap<String, String> result = new LinkedHashMap<>();
		
		ElementInfo _idEi = heap.get(ei.getReferenceField(_idFi));
		
		if (_idEi != null) {
			result.put("_id", treatMObjectId(sb, _idEi));
		}
		
		ElementInfo fieldsReferencesEi = ei.getObjectField(fieldsFi.getName());
		if (fieldsReferencesEi == null) {
			assert false;
			return result;
		}
		int[] fieldsReferences = fieldsReferencesEi.asReferenceArray();
		for (int i = 0; i < fieldsReferences.length; i++) {
			ElementInfo mFieldEi = heap.get(fieldsReferences[i]);
			String mFieldKey = mFieldEi.getStringField(keyFi.getName());
			if (mFieldKey == null) {
				continue;
			}
			ElementInfo mFieldValEi = mFieldEi.getObjectField(valFi.getName());
			
			if (mFieldValEi == null) {
				continue;
			}
			String mFieldVal;
			if (isWrapping(mFieldValEi.getClassInfo())) {
				FieldInfo valueFi = mFieldValEi.getClassInfo().getDeclaredInstanceField("value");
				// Booleans are encoded as '1' if true...
				if (mFieldValEi.getClassInfo().getName().equals(BOOLEAN_NAME)) {
					Long representsBoolean = (Long)TestGenUtil.getLeafSolution((Expression) mFieldValEi.getFieldAttr(valueFi));
					Boolean symbolicValue = null;
					if (representsBoolean == null) {
						symbolicValue = null;
					} else {
						symbolicValue = (representsBoolean == 1);
					}
					mFieldVal = decideOnConcreteOrSymbolicValue(
							mFieldValEi.getFieldValueObject("value"), 
							symbolicValue
					);
				} else {
					mFieldVal = decideOnConcreteOrSymbolicValue(
							mFieldValEi.getFieldValueObject("value"), 
							TestGenUtil.getLeafSolution((Expression) mFieldValEi.getFieldAttr(valueFi))
					);
				}
			} else if (mFieldValEi.getClassInfo().getName().equals(STRING_NAME)) {
				mFieldVal = decideOnConcreteOrSymbolicValue(
						mFieldEi.getStringField(valFi.getName()), 
						TestGenUtil.getLeafSolution((Expression) mFieldEi.getFieldAttr(valFi))
				);
			} else {
				mFieldVal = generateReferenceFieldForEi(mFieldEi, valFi, sb, false);
			}
			result.put(mFieldKey, mFieldVal);
		}
		return result;
	}
	
	@Override
	protected String generatePostMethodCallAssertions() {
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<ElementInfo, ElementInfo> mcom : mCollectionToOriginalMock.entrySet()) {
			sb.append(generateAssertEqualsStatementsForCollection(mcom.getValue(), mcom.getKey()));
		}
		return sb.toString();
	}
	
	protected String generateAssertEqualsStatementsForCollection(ElementInfo originalDataCollection, ElementInfo mcolEi) {
		ElementInfo dataEi = mcolEi.getObjectField(dataFi.getName());
		if (dataEi == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String collectionName = mcolEi.getStringField(nameFi.getName());
		sb.append("// Generate assertion statements to check content of '")
			.append(collectionName)
			.append("':\r\n");
		String collectionArgName = generateDataCollectionForAssertions(sb, originalDataCollection, mcolEi);
		ElementInfo currentMDocEi = dataEi.getObjectField(curFi.getName());
		String eiName = getMDocEiNameAltered(sb, currentMDocEi);
		int count = 0;
		while (dataEi != null && currentMDocEi != null) {
			if (!currentMDocEi.getBooleanField(isDeletedFi) && !eiName.equals("")) {
				sb.append("assertEquals(")
					.append(eiName)
					.append(", ")
					.append(collectionArgName).append(".").append(findOneElementWithIdFunction(eiName))
					.append(");\r\n");
				count++;
			}
			dataEi = dataEi.getObjectField(tailFi.getName());
			if (dataEi != null) {
				currentMDocEi = dataEi.getObjectField(curFi.getName());
				eiName = getMDocEiNameAltered(sb, currentMDocEi);
			}
		}
		sb.append("assertEquals(")
			.append(count)
			.append(",")
			.append(collectionArgName).append(".").append(countDocumentsInDataCollectionFunction())
			.append(");\r\n");
		return sb.toString(); 
	}
		
	protected String getMDocEiNameAltered(StringBuilder sb, ElementInfo mdocEi) {
		if (mdocEi == null) {
			return null;
		}
		String eiName = getNameForEi(mdocEi);
		boolean isUpdated = mdocEi.getBooleanField(isUpdatedFi);
		boolean isInserted = mdocEi.getBooleanField(isInsertedFi);
		// Create new MDocument instance in the test if it has not already been created
		// or if the MDocument was updated
		if (isInserted || isUpdated) {
			eiName = treatMDocument(sb, mdocEi, isUpdated);
		}
		return eiName;
	}
	
	/** 
	 * Construct an instance of the original data collection, e.g., MongoCollection, 
	 * using the mock entity (originalDataCollection) ElementInfo and the MCollection-
	 * ElemenInfo. 
	 */
	
	@Override
	protected String includeMethodCallParameter(ElementInfo ei, String valueOrArgName) {
		if (!circumventWrappingTestDriver) {
			return valueOrArgName + ","; // Nothing to do in this case
		}
		if (ei == null) {
			// Always include null or primitives
			return valueOrArgName + ",";
		} else if (excludeTypeFromMethodCallParameters.contains(ei.getClassInfo().getName())) {
			return "";
		} else {
			return valueOrArgName + ",";
		}
	}
	
	@Override
	protected String generateTestedMethodName() { // TODO automate for driver_-named method
		String methodName = tc.getMethodName();
		if (methodName.contains("driver_")) {
			methodName = methodName.substring(methodName.indexOf('_')+1);
		}
		return methodName;
	}
}
