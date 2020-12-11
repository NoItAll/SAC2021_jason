package gov.nasa.jpf.symbc.mdb.tcg;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.Property;
import gov.nasa.jpf.symbc.mdb.tcg.MethodSummary.InputArgument;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;
import gov.nasa.jpf.vm.NoUncaughtExceptionsProperty;

public class TestCase { 
	
	protected final MethodSummary belongsTo;
	protected final int testNumber;
	protected final Map<InputArgument, Object> valuesForArgs;
	protected final Object returnVal;
	protected final String returnValString;
	protected final Heap heap;
	protected final BitSet incrementallyCoveredLines;
	
	protected TestCase(
			MethodSummary belongsTo, 
			Map<InputArgument, Object> valuesForArgs,
			Object returnValue,
			String returnValueString,
			Heap heap,
			BitSet coveredLines) {
		this.belongsTo = belongsTo;
		this.valuesForArgs = valuesForArgs;
		this.returnVal = returnValue;
		this.returnValString = returnValueString;
		this.heap = heap;
		this.testNumber = belongsTo.tests.size();
		this.incrementallyCoveredLines = coveredLines;
	}

	public MethodSummary getBelongsTo() {
		return belongsTo;
	}

	public int getTestNumber() {
		return testNumber;
	}

	public Map<InputArgument, Object> getValuesForArgs() {
		return valuesForArgs;
	}

	public Object getReturnVal() {
		return returnVal;
	}

	public String getReturnValString() {
		return returnValString;
	}

	public Heap getHeap() {
		return heap;
	}

	public BitSet getIncrementallyCoveredLines() {
		return incrementallyCoveredLines;
	}
	
	public String getMethodName() {
		return belongsTo.methodName;
	}
	
	public String getClassName() {
		return belongsTo.className;
	}
	
	public int getMethodSummaryNumber() {
		return belongsTo.methodSummaryNumber;
	}
	
	public boolean methodIsStatic() {
		return belongsTo.mi.isStatic();
	}
	
	public ElementInfo getCallerEi() {
		return belongsTo.callerEi;
	}
	
	public List<InputArgument> getInputArgumentsInOrder() {
		return belongsTo.getIA();
	}
	
	public Object getValueForArg(InputArgument ia) {
		return valuesForArgs.get(ia);
	}
	
	public boolean resultIsSymbolicUndefined() {
		return returnValString.equals(""+SymbolicReal.UNDEFINED) || 
				returnValString.equals(""+SymbolicInteger.UNDEFINED) || 
				returnValString.equals(StringSymbolic.UNDEFINED);
	}
	
	public boolean hasRealReturnVal() {
		return this.returnVal instanceof SymbolicReal || this.returnVal instanceof RealConstant;
	}
	
	public boolean hasIntegerReturnVal() {
		return this.returnVal instanceof SymbolicInteger || this.returnVal instanceof IntegerConstant;
	}
	
	public boolean hasError() {
		return returnVal instanceof gov.nasa.jpf.Error;
	}
	
	public boolean hasObjectReturn() {
		return this.returnVal instanceof ElementInfo;
	}
	
	public boolean hasStringReturnVal() {
		if (returnVal == null) {
			return false;
		}
		if (!(returnVal instanceof ElementInfo)) {
			return false;
		}
		ElementInfo temp = (ElementInfo) returnVal;
		return temp.getClassInfo().isStringClassInfo();
	}
 	
	public boolean hasPrimitiveOrStringReturn() {
		return returnVal instanceof Expression || hasStringReturnVal();
	}
	
	public boolean isVoid() {
		return returnVal instanceof VoidMarker;
	}
	
	public String getTestedClassName() {
		return belongsTo.className;
	}
	
	public double getMethodCoverage() {
		BitSet coveredLinesCopy = new BitSet();
		coveredLinesCopy.or(incrementallyCoveredLines);
		coveredLinesCopy.and(belongsTo.executableMethodLines);
		return ((double) coveredLinesCopy.cardinality()) / belongsTo.executableMethodLines.cardinality();
	}
	
	public double getClassCoverage() {
		BitSet coveredLinesCopy = new BitSet();
		coveredLinesCopy.or(incrementallyCoveredLines);
		coveredLinesCopy.and(belongsTo.executableClassLines);
		return ((double) coveredLinesCopy.cardinality()) / belongsTo.executableClassLines.cardinality();
	}
	
	public ClassInfo getErrorClassInfo() {
		if (!this.hasError()) {
			throw new RuntimeException("Test case has no error.");
		}
		Property errorProperty = ((gov.nasa.jpf.Error) returnVal).getProperty();
		if (!(errorProperty instanceof NoUncaughtExceptionsProperty)) {
			throw new RuntimeException("Only uncaught Exceptions are currently treated.");
		}
		NoUncaughtExceptionsProperty nuep = (NoUncaughtExceptionsProperty) errorProperty;
		return nuep.getUncaughtExceptionInfo().getException().getClassInfo();
	}
}
