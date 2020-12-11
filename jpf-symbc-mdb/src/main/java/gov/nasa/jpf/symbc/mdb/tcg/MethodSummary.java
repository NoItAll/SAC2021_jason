package gov.nasa.jpf.symbc.mdb.tcg;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.vm.DynamicElementInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.Types;


/**
 * Represents the analysis of a method using the MongoDB Java driver. 
 * Currently, the method's test must be initialized immediately. Otherwise backtracking
 * deletes the input arguments.
 */
public class MethodSummary {
	protected final ElementInfo callerEi;
	protected MethodInfo mi;
	protected static int methodCount = 0;
	protected int methodSummaryNumber;
	// Object which calls the to-be-tested method. Is null for static methods.
	protected final String className;
	protected final String methodName;
	protected String stringArgTypes = "";
	protected String stringArgValues = "";
	protected String stringSymValues = "";
	protected final List<TestCase> tests = new ArrayList<>();
	// More "workable" information about the input arguments:
	protected final List<InputArgument> inputArguments = new ArrayList<>();
	protected final BitSet executableMethodLines;
	protected final BitSet executableClassLines;
	protected final TestMethodGenerator integrationTcsg;
	
	public MethodSummary(
			ElementInfo callerEi,
			String testedClassName,
			String methodName, 
			MethodInfo mi, 
			BitSet executableLines,
			BitSet executableClassLines,
			TestMethodGenerator tcg) {
		this.callerEi = callerEi;
		this.className = testedClassName;
		this.methodName = methodName;
		this.mi = mi;
		this.executableMethodLines = executableLines;
		this.executableClassLines = executableClassLines;
		this.methodSummaryNumber = methodCount++;
		this.integrationTcsg = tcg;
	}
	
	public void addInputArgument(byte argType, String argName, Object argValue, Expression argExpr) {
		this.inputArguments.add(new InputArgument(argType, argName, argValue, argExpr));
	}
	
	public void generateTestCase(
			Map<InputArgument, Object> valuesForArgs, 
			Object returnValue, 
			String returnValueString, 
			Heap heap, 
			BitSet coveredLines) {
		TestCase tc = new TestCase(this, valuesForArgs, returnValue, returnValueString, heap, coveredLines);
		this.tests.add(tc);
		if (integrationTcsg != null) {
			integrationTcsg.generateTestCaseString(tc);
		}
	}
	
	public List<InputArgument> getIA() {
		return Collections.unmodifiableList(inputArguments);
	}
	
	public List<InputArgument> getObjectIA() {
		List<InputArgument> result = new ArrayList<>();
		for (InputArgument ia : inputArguments) {
			if (ia.isSymbolicObject()) {
				result.add(ia);
			}
		}
		return result;
	}
	
	public List<String> getTestMethodStrings() {
		if (integrationTcsg != null) {
			List<String> result = new ArrayList<>();
			for (TestCase tc : tests) {
				result.add(TestGenUtil.tabIndentOnce(integrationTcsg.getString(tc)));
			}
			return result;
		} else {
			return null;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MethodSummary)) {
			return false;
		} else {
			MethodSummary temp = (MethodSummary) o;
			return methodName.equals(temp.methodName) && methodSummaryNumber == temp.methodSummaryNumber;
		}
	}
	
	public void reduceTestCases() {
		SimpleLosslessTestCaseReducer tcr = new SimpleLosslessTestCaseReducer(this);
		tests.retainAll(tcr.generateSubset(tests));
	}
	
	
	public double overallClassCoverageRatio(Collection<TestCase> testsSubset) {
		return overallCoverageRatio(this.executableClassLines, testsSubset);
	}
	
	public double overallMethodCoverageRatio(Collection<TestCase> testsSubset) {
		return overallCoverageRatio(this.executableMethodLines, testsSubset);
	}
	
	public static double overallCoverageRatio(
			BitSet executableLines, 
			Collection<TestCase> testsSubset) {
		BitSet disjunction = new BitSet();
		for (TestCase t : testsSubset) {
			disjunction.or(t.incrementallyCoveredLines);
		}
		double result = ((double) disjunction.cardinality()) / executableLines.cardinality();
		return result;
	}
	
	public class InputArgument {
		/** Type as defined in gov.nasa.jpf.vm.Types */
		public final byte argType;
		/** Names of input argument as given in method signature */
		public final String argName;
		/** "Value" of input argument the method was called with. 
		 *  This value is meaningless if inputExpression is not null, i.e., iff
		 *  the input argument is symbolic. If the input argument is primitive, e.g., int,
		 *  the value is Integer. For objects, noticeably also for Integer, an ElementInfo is given.
		 */
		public final Object argValue;
		/** The input expression. Is not null, if the input argument is symbolic. */
		public final Expression argExpr;
		
		public InputArgument(
				byte argType,
				String argName,
				Object argValue,
				Expression argExpression) {
			this.argType = argType;
			this.argName = argName;
			this.argValue = argValue;
			this.argExpr = argExpression;
		}
		
		public boolean isString() {
			return TestGenUtil.isString(argType, argValue);
		}
		
		public boolean isSymbolic() {
			return TestGenUtil.isSymbolic(argExpr);
		}
		
		public boolean isSymbolicInteger() {
			return TestGenUtil.isSymbolicInteger(argValue, argExpr);
		}
		
		public boolean isPrimitiveIntegerArg() {
			return TestGenUtil.isPrimitiveIntegerArg(argValue);
		}
		
		public boolean isPrimitiveBooleanArg() {
			return TestGenUtil.isPrimitiveBooleanArg(argValue);
		}
		
		public boolean isWrappingIntegerArg() {
			return TestGenUtil.isWrappingIntegerArg(argValue);
		}
		
		public boolean isPrimitiveRealArg() {
			return TestGenUtil.isPrimitiveRealArg(argValue);
		}
		
		public boolean isWrappingRealArg() {
			return TestGenUtil.isWrappingRealArg(argValue);
		}
		
		public boolean isSymbolicObject() {
			return TestGenUtil.isSymbolicObject(argValue, argExpr);
		}
		
		public boolean isSymbolicReal() {
			return TestGenUtil.isSymbolicReal(argValue, argExpr);
		}
		
		public boolean isSymbolicString() {
			return TestGenUtil.isSymbolicString(argExpr);
		}
		
		public Double getSymbolicRealSolution() {
			return TestGenUtil.getSymbolicRealSolution(argValue, argExpr);
		}
		
		public Long getSymbolicIntegerSolution() {
			return TestGenUtil.getSymbolicIntegerSolution(argValue, argExpr);
		}
		
		public String getSymbolicStringSolution() {
			return TestGenUtil.getSymbolicStringSolution(argValue, argExpr);
		}
		
		public boolean isObject() {
			return argType == Types.T_REFERENCE;
		}
		
		public String getTypeName() {
			if (!isObject()) {
				switch (argType) {
				  case Types.T_BOOLEAN:
					  return "boolean";
				  case Types.T_BYTE:
					  return "byte";
				  case Types.T_CHAR:
					  return "char";
				  case Types.T_SHORT:
					  return "short";
				  case Types.T_INT:
					  return "int";
				  case Types.T_LONG:
					  return "long";
				  case Types.T_FLOAT:
					  return "float";
				  case Types.T_DOUBLE:
					  return "double";
				  default:
					  return null;
				}
			} else {
				return ((DynamicElementInfo) argValue).getClassInfo().getName();
			}
		}
		
		public String toString() {
			return "InputArgument::" + this.argName;
		}
	}
	
	public Set<String> getTypesToImportForIntegrationTests() {
		return integrationTcsg.getImports();
	}
	
}
