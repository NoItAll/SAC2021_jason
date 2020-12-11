package gov.nasa.jpf.symbc.mdb.tcg.test_method_generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nasa.jpf.symbc.mdb.tcg.MethodSummary.InputArgument;
import gov.nasa.jpf.symbc.mdb.tcg.TestCase;
import gov.nasa.jpf.symbc.mdb.tcg.TestCaseGenerationAborted;
import gov.nasa.jpf.symbc.mdb.tcg.TestGenUtil;
import gov.nasa.jpf.symbc.mdb.tcg.TestMethodGenerator;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ArrayFields;
import gov.nasa.jpf.vm.BooleanFieldInfo;
import gov.nasa.jpf.vm.ByteFieldInfo;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.DoubleFieldInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.FloatFieldInfo;
import gov.nasa.jpf.vm.Heap;
import gov.nasa.jpf.vm.IntegerFieldInfo;
import gov.nasa.jpf.vm.LongFieldInfo;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.NoUncaughtExceptionsProperty;
import gov.nasa.jpf.vm.ReferenceArrayFields;
import gov.nasa.jpf.vm.ReferenceFieldInfo;
import gov.nasa.jpf.vm.ShortFieldInfo;

/**
 * Standard {@link TestMethodGenerator}. 
 * For all treated objects it is assumed that the object with an attribute field XYZ it is assumed
 * that a corresponding setXYZ(...)-method is implemented. 
 * If this is not the case, special cases can be added in subclasses by adding to the {@link StdTestMethodGenerator#specialCases}-list. 
 * Thereafter, the method {@link StdTestMethodGenerator#treatSpecialCase} must be overridden.
 * In the future, this should be done more flexibly with delegation instead of inheritance.
 * This class implements certain general cases like Wrapping-Objects ({@link Integer}, {@link Boolean}, {@link Float}, ...)
 * and {@link HashMap}. 
 */
public class StdTestMethodGenerator implements TestMethodGenerator {
	public static boolean assumeSettersForNonSpecialCases = true;
	public static boolean assumePublicEmptyConstructorForNonSpecialCases = true;
	public static final String ASSERT_EQUALS_DELTA = "10e-6";
	protected static final String EQ = " = ";
	protected static final String LB = "\r\n";
	protected static final String NNEW = "new ";
	protected static final String BLR = "()";
	protected static final String SP = " ";
	protected static final String S = ";";
	protected static final String BL = "(";
	protected static final String BR = ")";
	protected static final String NULL = "null";
		
	protected static final String INT_NAME = Integer.class.getName();
	protected static final String DOUBLE_NAME = Double.class.getName();
	protected static final String FLOAT_NAME = Float.class.getName();
	protected static final String LONG_NAME = Long.class.getName();
	protected static final String SHORT_NAME = Short.class.getName();
	protected static final String BYTE_NAME = Byte.class.getName();
	protected static final String CHARACTER_NAME = Character.class.getName();
	protected static final String BOOLEAN_NAME = Boolean.class.getName();
	protected static final String STRING_NAME = String.class.getName();
	protected static final String CLASS_NAME = Class.class.getName();
	
	protected static final String PINT_NAME = int.class.getName();
	protected static final String PDOUBLE_NAME = double.class.getName();
	protected static final String PFLOAT_NAME = float.class.getName();
	protected static final String PLONG_NAME = long.class.getName();
	protected static final String PSHORT_NAME = short.class.getName();
	protected static final String PBYTE_NAME = byte.class.getName();
	protected static final String PCHARACTER_NAME = char.class.getName();
	protected static final String PBOOLEAN_NAME = boolean.class.getName();
			
	/**
	 * The constructor.
	 */
	public StdTestMethodGenerator() {
		types.add("org.junit.*");
		types.add("static org.junit.Assert.*");
		types.add("java.util.ArrayList");
		specialCases.add(ArrayList.class); // TODO same for other special cases. Refactor
	}
	
	@Override
	public String generateTestCaseString(TestCase tc) {
		init(tc);
		try {
			return execute();
		} catch (TestCaseGenerationAborted tcga) {
			return "";
		}
	}
	
	protected String execute() {
		// StringBuilder for the body of the test case
		StringBuilder sb = new StringBuilder();
		// StringBuilder for the method call to which the arguments of the test case's 
		// body are forwarded
		StringBuilder methodCallParameterSb = new StringBuilder();
		// If the method is not static, a new object must be created
		// Currently, the empty constructor is assumed.		
		
		for (InputArgument ia : tc.getInputArgumentsInOrder()) {
			// Append each of the input arguments into the method call. 
			Object val = tc.getValueForArg(ia);
			if (ia.isPrimitiveIntegerArg() ||
					ia.isPrimitiveRealArg() ||
					ia.isPrimitiveBooleanArg() ||
					ia.isString()) {
				String value = generatePrimitiveElementString(
						sb, 
						ia.getTypeName(), 
						val,
						TestGenUtil.getLeafSolution(ia.argExpr), 
						null, 
						null
				);
				methodCallParameterSb.append(includeMethodCallParameter(null, value));
			} else if (val == null) {
				sb.append(ia.getTypeName()).append(SP).append(argn()).append(EQ).append("null;").append(LB);
				methodCallParameterSb.append(includeMethodCallParameter(null, NULL));
			} else {
				// If the input argument is an object, the objects in its state are generated recursively.
				String value = generateElementInfoString(sb, (ElementInfo) val);
				methodCallParameterSb.append(includeMethodCallParameter((ElementInfo) val, value));
			}
		}
		// Delete last ','
		methodCallParameterSb.deleteCharAt(methodCallParameterSb.length()-1).append(BR);		
		StringBuilder methodCallSb = new StringBuilder();
		if (!tc.methodIsStatic()) {
			String methodCallObjectArgName = generateMethodCallObject(sb);
			methodCallSb.append(methodCallObjectArgName);
		} else {
			methodCallSb.append(tc.getClassName());
		}
		// The method is called using either the priorly created class, or newly created object.
		methodCallSb.append('.').append(generateTestedMethodName());
		methodCallSb.append(BL).append(methodCallParameterSb);
		
		// For the assertEquals-statement: Generate the output of the method.
		generateAssertEquals(sb, methodCallSb.toString());
		sb.append(generatePostMethodCallAssertions());
		// Indent the method's body.
		String methodBody = TestGenUtil.tabIndentOnce(sb.toString());
		
		String result = generateTestMethodDeclaration().concat(methodBody).concat(generateTestMethodEnd()).toString();
		
		testCaseStringRepresentations.put(tc, result);
		clear();
		return result;
	}
	
	protected String generateMethodCallObject(StringBuilder methodCallSb) {
		if (!tc.methodIsStatic()) {
			ElementInfo callerEi = tc.getCallerEi();
			String methodCallObjectArgName = generateElementInfoString(methodCallSb, callerEi);
			return methodCallObjectArgName;
		} else {
			return null;
		}
	}
	
	protected String generateTestedMethodName() {
		// This is just for easily generating a method call for a different function than the function
		// defined via symbolic.method. If, for example, the symbolic.method is a wrapper function for 
		// the actual tested method, this might come in handy. 
		return tc.getMethodName();
	}
	
	protected String includeMethodCallParameter(ElementInfo ei, String valueOrArgName) {
		// This is just for easily filtering method call parameters if an additional wrapping test-driver is used. 
		// If the method which is tested directly corresponds to the method for which test cases should be generated, 
		// it is not really needed.
		return valueOrArgName + ",";
	}
	
	protected void generateAssertEquals(StringBuilder sb, String methodCallString) {
		sb.append("// Generate assertEquals for method call\r\n");
		String val;
		if (tc.hasError()) {
			sb.append("try {\r\n\t");
		}
		if (tc.hasObjectReturn() && !tc.hasStringReturnVal()) {
			ElementInfo temp = (ElementInfo) tc.getReturnVal();
			val = generateElementInfoString(sb, temp);
			sb.append("assertEquals(").append(val).append(',').append(methodCallString).append(BR).append(S).append(LB);
		} else if (tc.hasPrimitiveOrStringReturn()) {
			// Treat constants and symbolic values
			if (tc.resultIsSymbolicUndefined()) {
				// Assume neutral value.
				val = tc.hasRealReturnVal() ? "0.0" : "\"\"";
				val = tc.hasIntegerReturnVal() ? "0" : val;
				val = tc.hasStringReturnVal() ? "\"\"" : val;
			} else {
				val = tc.getReturnValString();
				val = tc.hasStringReturnVal() ? '"' + val + '"' : val;
			}
			if (tc.hasRealReturnVal()) {
				// If the return value is a symbolic real we will use the delta-variant of assertEquals
				sb.append("assertEquals(").append(val).append(',').append(methodCallString).append(',').append(ASSERT_EQUALS_DELTA).append(BR).append(S).append(LB);
			} else {
				sb.append("assertEquals(").append(val).append(',').append(methodCallString).append(BR).append(S).append(LB);
			}
		} else if (tc.hasError()) {
			// In case an error is treated, we will just call the method.
			sb.append(methodCallString).append(S).append(LB);
		} else if (tc.isVoid()) {
			// If the method is void, do not create any assert-equals.
			sb.append(methodCallString).append(S).append(LB);
		} else {
			sb.append("assertEquals(").append("null").append(',').append(methodCallString).append(BR).append(S).append(LB);
		}
		if (tc.hasError()) {
			sb.append("\t").append("fail(\"Uncaught exception expected.\");\r\n");
			sb.append("} catch (")
			.append(getExceptionClassNameFromError((gov.nasa.jpf.Error) tc.getReturnVal()))
			.append(" e) {}\r\n");
		}
	}
	
	
	/* Data of the current test case */
	protected Heap heap;
	protected TestCase tc;
	
	protected void init(TestCase tc) {
		this.tc = tc;
		this.heap = tc.getHeap();
	}
	
	/* State for current test case */
	protected int argNo = 0;
	private Map<ElementInfo, String> eiToName = new HashMap<>();
	protected void clear() {
		argNo = 0;
		eiToName.clear();
		methodCallObjectArgumentName = null;
	}
	
	public String getNameForEi(ElementInfo ei) {
		return eiToName.get(ei);
	}
	
	public void addEiToName(ElementInfo ei, String name) {
		eiToName.put(ei, name);
	}
	
	protected final Map<TestCase, String> testCaseStringRepresentations = new HashMap<>();
	
	protected List<Class<?>> specialCases = new ArrayList<>();
	
	protected Set<String> types = new HashSet<>();
	
	protected String methodCallObjectArgumentName;
	
	@Override
	public String getString(TestCase tc) {
		return testCaseStringRepresentations.get(tc);
	}
	
	protected boolean isSpecialCase(ElementInfo ei) {
		String classInfoName = ei.getClassInfo().getName();
		for (Class<?> c : specialCases) {
			if (c.getName().equals(classInfoName)) {
				return true;
			}
		}
		return false;
	}
	
	protected String treatSpecialCase(StringBuilder sb, ElementInfo ei) {
		if (ei.getClassInfo().getName().equals(ArrayList.class.getName())) {
			return treatArrayList(sb, ei);
		}
		
		throw new IllegalArgumentException("Special case not treated: " + ei);
	}
	
	public String treatArrayList(StringBuilder sb, ElementInfo ei) {
		ElementInfo elementData = ei.getObjectField("elementData");
		int noElements = ei.getIntField("size");
		ReferenceArrayFields raf = (ReferenceArrayFields) elementData.getArrayFields();
		
		assert raf.arrayLength() >= noElements;
		
		String arrayListName = argn();
		sb.append("ArrayList ").append(arrayListName).append(EQ).append(NNEW).append("ArrayList();\r\n");
		
		for (int i = 0; i < noElements; i++) {
			ElementInfo elementEi = heap.get(raf.getReferenceValue(i));
			String elementName;
			if (elementEi.getClassInfo().getName().equals(STRING_NAME)) {
				elementName = generatePrimitiveElementString(
						sb, STRING_NAME, 
						elementEi, 
						null, // TODO Extract symbolic String value from element...
						null, null
				);
			} else {
				elementName = generateElementInfoString(sb, elementEi);
			}
			sb.append(arrayListName).append(".add(").append(elementName).append(");\r\n");
		}
		
		return arrayListName;
	}
	
	protected String generatePrimitiveElementString(
			StringBuilder sb, 
			String typeName, 
			Object concreteVal,
			Object symbolicValueSolution,
			FieldInfo setForField,
			ElementInfo setFor) {
		
		if (concreteVal instanceof ElementInfo && ((ElementInfo) concreteVal).isStringObject()) {
			concreteVal = ((ElementInfo) concreteVal).asString();
		}
		
		String value;
		if (symbolicValueSolution != null) {
			String symbolicString;
			if (symbolicValueSolution instanceof StringSymbolic) {
				symbolicString = ((StringSymbolic) symbolicValueSolution).solution;
			} else {
				symbolicString = symbolicValueSolution.toString();
			}
			if (!symbolicString.equals(""+SymbolicReal.UNDEFINED) && 
					!symbolicString.equals(""+SymbolicInteger.UNDEFINED) &&
					!symbolicString.equals(StringSymbolic.UNDEFINED)) {
				value = symbolicString;
			} else {
				value = concreteVal != null ? concreteVal.toString() : "0";
			}
		} else {
			value = concreteVal.toString();
		}
		if (concreteVal instanceof String || symbolicValueSolution instanceof String) {
			value = '"' + value + '"';
		} else {
			if (typeName.equals(PBOOLEAN_NAME)) {
				value = (value.equals("1") || value.equals("true")) ? "true" : "false";
			}
			value = BL + typeName + BR + SP + value;
		}

		String name = argn();
		if (setFor != null) {
			assert setForField != null;
			primitiveFieldSetter(sb, setFor, setForField, value);
		} else {
			sb.append(typeName).append(SP).append(name).append(EQ)
				.append(value).append(S).append(LB);
		}
		return name;
	}
	
	private StringBuilder primitiveFieldSetter(
			StringBuilder sb, 
			ElementInfo setFor,
			FieldInfo fi, 
			String value) {
		if (assumeSettersForNonSpecialCases) {
			return sb.append(getNameForEi(setFor))
					.append(".")
					.append("set")
					.append(TestGenUtil.toFirstUpper(fi.getName()))
					.append(BL)
					.append(value)
					.append(BR)
					.append(S)
					.append(LB);
		} else {
			return setWithReflection(sb, setFor, fi, value);
		}
		
	}
	
	protected StringBuilder setWithReflection(StringBuilder sb, ElementInfo setFor, FieldInfo fi, String value) {
		sb.append("setWithReflection(").append(getNameForEi(setFor)).append(", \"").append(fi.getName()).append("\", ").append(value).append(");\r\n");
		return sb;
	}
	
	protected String generateElementInfoString(StringBuilder sb, ElementInfo ei) {
		if (ei == null) {
			return NULL;
		}
		if (getNameForEi(ei) != null) {
			return getNameForEi(ei);
		}
		ClassInfo ci = ei.getClassInfo();
		
		if (isSpecialCase(ei)) {
			String argName = treatSpecialCase(sb, ei);
			addEiToName(ei, argName);
			return argName;
		}
		
		if (isWrapping(ci)) {
			FieldInfo fi = ci.getDeclaredInstanceField("value");
			Expression val = (Expression) ei.getFieldAttr(fi);
			Object concreteVal = ei.getFieldValueObject(fi.getName());
			Object value = TestGenUtil.getLeafSolution(val);
			constructorForWrapping(sb, ei, ci.getName(), concreteVal, value, argn());
			return getNameForEi(ei);
		}
		
		constructorFor(sb, ei);
		
		
		FieldInfo[] fis = ci.getDeclaredInstanceFields();
		for (FieldInfo fi : fis) {
			generateFieldForEi(ei, fi, sb);
		}	
		return getNameForEi(ei);
	}
	
	protected boolean isWrapping(ClassInfo ci) {
		String className = ci.getName();
		return className.equals(INT_NAME) || 
				className.equals(DOUBLE_NAME) ||
				className.equals(CHARACTER_NAME) ||
				className.equals(FLOAT_NAME) ||
				className.equals(BYTE_NAME) ||
				className.equals(LONG_NAME) ||
				className.equals(SHORT_NAME) ||
				className.equals(BOOLEAN_NAME);
	}
	
	protected String generateFieldForEi(ElementInfo ei, FieldInfo fi, StringBuilder sb) {
		if (fi instanceof DoubleFieldInfo) {
			return generatePrimitiveElementString(
					sb, PDOUBLE_NAME, 
					ei.getDoubleField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)),
					fi, ei);
		} else if (fi instanceof FloatFieldInfo) {
			return generatePrimitiveElementString(
					sb, PFLOAT_NAME, 
					ei.getFloatField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)), 
					fi, ei);
		} else if (fi instanceof LongFieldInfo) {
			return generatePrimitiveElementString(
					sb, PLONG_NAME, 
					ei.getLongField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)), 
					fi, ei);
		} else if (fi instanceof IntegerFieldInfo) {
			return generatePrimitiveElementString(
					sb, PINT_NAME, 
					ei.getIntField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)), 
					fi, ei);
		} else if (fi instanceof ShortFieldInfo) {
			return generatePrimitiveElementString(
					sb, PSHORT_NAME, 
					ei.getShortField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)), 
					fi, ei);
		} else if (fi instanceof ByteFieldInfo) {
			return generatePrimitiveElementString(
					sb, PBYTE_NAME, 
					ei.getByteField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)), 
					fi, ei);
		} else if (fi instanceof BooleanFieldInfo) {
			return generatePrimitiveElementString(
					sb, PBOOLEAN_NAME, 
					ei.getBooleanField(fi), 
					TestGenUtil.getLeafSolution((Expression) ei.getFieldAttr(fi)), 
					fi, ei);	
		} else if (fi instanceof ReferenceFieldInfo) {
			return generateReferenceFieldForEi(ei, fi, sb, true);
		} else {
			throw new RuntimeException("Field type is not recognized.");
		}
	}
	
	protected String generateReferenceFieldForEi(ElementInfo ei, FieldInfo fi, StringBuilder sb, boolean generateSetter) {
		ElementInfo value = heap.get(ei.getReferenceField(fi));
		return generateReferenceFieldForEiWithValue(ei, fi, sb, generateSetter, value);
	}
	
	protected String generateReferenceFieldForEiWithValue(
			ElementInfo ei, 
			FieldInfo fi, 
			StringBuilder sb, 
			boolean generateSetter, 
			ElementInfo value) {
		String argName;
		if (value == null) {
			argName = NULL;
			if (generateSetter) {
				eiSetter(sb, ei, fi, argName);
			}
		} else if (value.getClassInfo().getName().equals(CLASS_NAME)) {
			argName = argn();
			String generic = value.getStringField("name");
			String genericInfo = '<' + generic + '>';
			addEiToName(value, argName);
			sb.append(CLASS_NAME)
				.append(genericInfo)
				.append(SP)
				.append(argName)
				.append(EQ)
				.append(generic).append(".class;").append(LB);
			if (generateSetter) {
				eiSetter(sb, ei, fi, argName);
			}
		} else if (value.getClassInfo().isStringClassInfo()) {
			String fieldValue = ei.getStringField(fi.getName());
			Expression expr = null;
			if (ei != null) {
				expr = ((Expression) ei.getFieldAttr(fi));
				expr = expr instanceof StringSymbolic ? expr : null;
			}
			ei = generateSetter ? ei : null;
			argName = generatePrimitiveElementString(
					sb, STRING_NAME, 
					fieldValue, 
					expr,
					fi, ei
			);
			
		} else if (value.getClassInfo().getName().equals(HashMap.class.getName()) || 
				value.getClassInfo().getName().equals(LinkedHashMap.class.getName())) {
			argName = generateMapString(sb, value);
			if (generateSetter) {
				eiSetter(sb, ei, fi, argName);
			}
		} else if (!TestGenUtil.isArrayField(fi)) {
			argName = generateElementInfoString(sb, value);
			if (generateSetter) {
				eiSetter(sb, ei, fi, getNameForEi(value));
			}
		} else {
			// Is array-element in ei
			argName = argn();
			generateArrayString(sb, ei, fi, argName, heap.get(ei.getReferenceField(fi)));
			if (generateSetter) {
				eiSetter(sb, ei, fi, argName);
			}
		}
		return argName;
	}
	
	protected String generateMapString(
			StringBuilder sb,
			ElementInfo mapEi) {
		int[] fieldRefs = getKeyValueReferencesFromMap(mapEi);
		constructorFor(sb, mapEi);
		return generateMapPutStringForEi(sb, mapEi, isLinkedHashMap(mapEi), fieldRefs);
	}
	
	protected int[] getKeyValueReferencesFromMap(ElementInfo mapEi) {
		FieldInfo tableFi;
		boolean isLinkedHashMap = isLinkedHashMap(mapEi);
		if (isLinkedHashMap) {
			return getKeyValueReferencesFromLinkedHashMap(mapEi);
		} else {
			tableFi = mapEi.getClassInfo().getDeclaredInstanceField("table");
		}
		ElementInfo tableEi = heap.get(mapEi.getReferenceField(tableFi));
		assert tableEi.isArray();
		return tableEi.asReferenceArray();
	}
	
	protected int[] getKeyValueReferencesFromLinkedHashMap(ElementInfo mapEi) {
		ElementInfo headEi = mapEi.getObjectField("head");
		if (headEi == null) {
			return new int[] {};
		}
		List<Integer> keyValueRefs = new ArrayList<>();
		keyValueRefs.add(headEi.getObjectRef());
		
		ElementInfo after = headEi.getObjectField("after");
		while (after != null) {
			keyValueRefs.add(after.getObjectRef());
			after = after.getObjectField("after");
		}
		
		// Transform to array
		int[] result = new int[keyValueRefs.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = keyValueRefs.get(i);
		}
		
		return result;
	}
	
	protected boolean isLinkedHashMap(ElementInfo mapEi) {
		if (mapEi.getClassInfo().getName().equals(LinkedHashMap.class.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	protected String generateMapPutStringForEi(
			StringBuilder sb,
			ElementInfo putToEi,
			boolean isLinkedHashMap,
			int[] keyValueReferences) {
		String mapArgName = getNameForEi(putToEi);
		for (int i = 0; i < keyValueReferences.length; i++) {
			if (keyValueReferences[i] != MJIEnv.NULL) {
				ElementInfo mapEntryEi = heap.get(keyValueReferences[i]);
				FieldInfo keyFi;
				FieldInfo valueFi;
				if (isLinkedHashMap) {
					keyFi = mapEntryEi.getClassInfo().getSuperClass().getDeclaredInstanceField("key");
					valueFi = mapEntryEi.getClassInfo().getSuperClass().getDeclaredInstanceField("value");
				} else {
					keyFi = mapEntryEi.getClassInfo().getDeclaredInstanceField("key");
					valueFi = mapEntryEi.getClassInfo().getDeclaredInstanceField("value");
				}
				String mapEntryKeyArgName = generateReferenceFieldForEi(mapEntryEi, keyFi, sb, false);
				String mapEntryValueArgName = generateReferenceFieldForEi(mapEntryEi, valueFi, sb, false);
				sb.append(mapArgName)
					.append(".put(")
					.append(mapEntryKeyArgName)
					.append(',')
					.append(mapEntryValueArgName)
					.append(BR).append(S).append(LB);
			}
		}
		return mapArgName;
	}
	
	protected String generateArrayString( 
			StringBuilder sb, 
			ElementInfo arOwner, 
			FieldInfo fi, 
			String arName,
			ElementInfo arElement) {
		// TODO Distinguish between different types of array fields...currently only ReferenceArrayFields regarded
		addEiToName(arElement, arName);
		String type = fi.getType().replace('$', '.');
		
		ArrayFields temp = arElement.getArrayFields();
		assert temp instanceof ReferenceArrayFields : "Only reference-arrays are currently supported.";
		ReferenceArrayFields arFields = (ReferenceArrayFields) temp;
		
		int noElements = arFields.arrayLength();
		
		sb.append(type)
			.append(SP)
			.append(arName)
			.append(EQ)
			.append(NNEW)
			.append(SP)
			.append(type.substring(0, type.length() - 2))
			.append('[')
			.append(noElements)
			.append(']')
			.append(S)
			.append(LB);
		
		for (int i = 0; i < noElements; i++) {
			ElementInfo currentElement = heap.get(arFields.getReferenceValue(i));
			if (currentElement != null) {
				generateElementInfoString(sb, currentElement);
				sb.append(arName)
					.append('[')
					.append(i)
					.append("] = ")
					.append(getNameForEi(currentElement))
					.append(S)
					.append(LB);
			} else {
				sb.append(arName)
					.append('[')
					.append(i)
					.append("] = ")
					.append(NULL)
					.append(S)
					.append(LB);
			}
		}
		return arName;
	}

	protected StringBuilder eiSetter(StringBuilder sb, ElementInfo setFor, FieldInfo fi, String argName) {
		if (assumeSettersForNonSpecialCases) {
			return sb.append(getNameForEi(setFor))
					.append(".")
					.append("set")
					.append(TestGenUtil.toFirstUpper(fi.getName()))
					.append(BL)
					.append(argName)
					.append(BR)
					.append(S)
					.append(LB);
		} else {
			return setWithReflection(sb, setFor, fi, argName);
		}
	}

	protected String argn() {
		return "arg"+(argNo++);
	}
	
	protected String constructorFor(StringBuilder sb, ElementInfo ei) {
		return constructorFor(sb, ei, new Object[0]);
	}
	
	protected String constructorFor(StringBuilder sb, ElementInfo ei, Object... values) {
		return constructorForEiAsClass(sb, ei.getClassInfo().getName().replace('$', '.'), ei, values);
	}
	
	protected String constructorForEiAsClass(StringBuilder sb, String eiAsType, ElementInfo ei, Object... values) {
		String thisArgName = argn();
		addEiToName(ei, thisArgName);
		if (assumePublicEmptyConstructorForNonSpecialCases || isSpecialCase(ei)) {
			sb.append(eiAsType)
				.append(SP)
				.append(thisArgName)
				.append(EQ)
				.append(newStatement(eiAsType, values))
				.append(S)
				.append(LB);
		} else {
			sb.append(eiAsType).append(SP).append(thisArgName).append(" = (").append(eiAsType).append(") generateInstance(").append(eiAsType).append(".class);\r\n");
		}
		return thisArgName;
	}
	
	protected String newStatement(String eiAsType, Object... values) {
		addToTypesIfNeeded(eiAsType);
		StringBuilder sb = new StringBuilder();
		sb.append(NNEW).append(eiAsType)
			.append(BL);
		
		if (values.length > 0) {
			for (int i = 0; i < values.length - 1; i++) {
				sb.append(values[i]).append(',');
			}
			sb.append(values[values.length - 1]);
		}
		sb.append(BR);
		return sb.toString();
	}
	
	protected String constructorForWrapping(
			StringBuilder sb, 
			ElementInfo ei,
			String className, 
			Object concreteValue, 
			Object symbolicValue, 
			String name) {
		String value = decideOnConcreteOrSymbolicValue(concreteValue, symbolicValue);
		addEiToName(ei, name);
		sb.append(className).append(SP).append(name).append(EQ)
			.append(NNEW).append(className).append(BL)
			.append(value).append(BR).append(S).append(LB);
		return name;
	}
	
	protected String decideOnConcreteOrSymbolicValue(Object concreteValue, Object symbolicValue) {
		String value = concreteValue.toString();
		if (symbolicValue != null && 
				!symbolicValue.equals(SymbolicReal.UNDEFINED) && 
				!symbolicValue.equals(SymbolicInteger.UNDEFINED) && 
				!symbolicValue.equals(StringSymbolic.UNDEFINED)) {
			value = symbolicValue.toString();
			if (symbolicValue instanceof String) {
				value = '"' + (String) value + '"';
			}
		} else if (concreteValue instanceof String) {
			value = '"' + (String) value + '"';
		}
		return value;
	}
	
	protected String getExceptionClassNameFromError(gov.nasa.jpf.Error er) {
		NoUncaughtExceptionsProperty prop = (NoUncaughtExceptionsProperty) ((gov.nasa.jpf.Error) er).getProperty();
		String resVal = prop.getUncaughtExceptionInfo().getExceptionClassname();
		return resVal;
	}
	
	protected String generateTestMethodDeclaration() {
		String result = "@Test ";
		result += "public void test_" + generateTestedMethodName() + "_" + tc.getMethodSummaryNumber() + "_" + tc.getTestNumber() + "() {\r\n";
		return result;
	}
	
	protected String generateTestMethodEnd() {
		return "}\r\n";
	}
	
	protected String generatePostMethodCallAssertions() {
		return "";
	}
	
	@Override
	public Set<String> getImports() {
		return types;
	}
	
	protected void addToTypesIfNeeded(String typeName) {
		if (!types.contains(typeName)) {
			types.add(typeName);
		}
	}
}
