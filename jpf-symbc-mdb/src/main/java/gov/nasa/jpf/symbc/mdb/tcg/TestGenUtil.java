package gov.nasa.jpf.symbc.mdb.tcg;

import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.DynamicElementInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Types;

public class TestGenUtil {
	
	private TestGenUtil() {}
	
	public static boolean isString(
			byte argType,
			Object argValue) {
		return argType == Types.T_REFERENCE && 
				((ElementInfo) argValue).getClassInfo().getName().equals(String.class.getName());
	}
	
	public static boolean isSymbolic(
			Expression argExpr) {
		return argExpr != null;
	}
	
	public static boolean isSymbolicInteger(
			Object argValue,
			Expression argExpr) {
		if (!isSymbolic(argExpr)) {
			return false;
		}
		if (argExpr instanceof SymbolicInteger) {
			// Might be Wrapping-object
			return isPrimitiveIntegerArg(argValue) || 
					isWrappingIntegerArg(argValue);
		}
		return false;
	}
	
	public static boolean isPrimitiveIntegerArg(
			Object argValue) {
		return argValue instanceof Byte || 
				argValue instanceof Short ||
				argValue instanceof Integer ||
				argValue instanceof Long ||
				argValue instanceof Boolean;
	}
	
	public static boolean isPrimitiveBooleanArg(
			Object argValue) {
		return argValue instanceof Boolean;
	}
	
	public static boolean isWrappingIntegerArg(
			Object argValue) {
		String className = ((ElementInfo) argValue).getClassInfo().getName();
		return className.equals(Byte.class.getName()) || 
				className.equals(Short.class.getName()) ||
				className.equals(Integer.class.getName()) ||
				className.equals(Long.class.getName());
	}
	
	public static boolean isPrimitiveRealArg(
			Object argValue) {
		return argValue instanceof Float || argValue instanceof Double;
	}
	
	public static boolean isWrappingRealArg(
			Object argValue) {
		String className = ((ElementInfo) argValue).getClassInfo().getName();
		return className.equals(Float.class.getName()) || 
				className.equals(Double.class.getName());
	}
	
	public static boolean isSymbolicObject(
			Object argValue,
			Expression argExpr) {
		return isSymbolic(argExpr) && 
				argExpr instanceof SymbolicInteger && 
				argValue instanceof ElementInfo;
	}
	
	public static boolean isSymbolicReal(
			Object argValue,
			Expression argExpr) {
		if (!isSymbolic(argExpr)) {
			return false;
		}
		if (argExpr instanceof SymbolicInteger) {
			// Might be Double-object
			return isWrappingRealArg(argValue);
		}
		return isPrimitiveRealArg(argValue);
	}
	
	public static boolean isSymbolicString(
			Expression argExpr) {
		return argExpr instanceof StringSymbolic;
	}
	
	public static Double getSymbolicRealSolution(
			Object argValue,
			Expression argExpr) {
		if (!isSymbolicReal(argValue, argExpr)) {
			throw new RuntimeException("Argument is no symbolic real");
		} else {
			if (isPrimitiveRealArg(argValue)) {
				Double solution = ((SymbolicReal) argExpr).solution;
				if (solution.equals(SymbolicReal.UNDEFINED)) {
					solution = (double) argValue;
				}
				return solution;
			} else {
				throw new RuntimeException("Only the values of primitive double-types"
						+ " can be extracted with this method.");
			}
		}
	}
	
	public static Long getSymbolicIntegerSolution(
			Object argValue,
			Expression argExpr) {
		if (!isSymbolicInteger(argValue, argExpr)) {
			throw new RuntimeException("Argument is no symbolic integer");
		} else {
			if (isPrimitiveIntegerArg(argValue)) {
				Long solution = ((SymbolicInteger) argExpr).solution;
				if (solution.equals(SymbolicInteger.UNDEFINED)) {
					solution = ((Number) argValue).longValue();
				}
				return solution;
			} else {
				throw new RuntimeException("Only the values of primitive integer-types"
						+ " can be extracted with this method.");
			}
		}
	}
	
	public static String getSymbolicStringSolution(
			Object argValue,
			Expression argExpr) {
		if (!isSymbolicString(argExpr)) {
			throw new RuntimeException("Argument is no symbolic String");
		} else {
			String solution = ((StringSymbolic) argExpr).solution;
			if (solution.equals(StringSymbolic.UNDEFINED)) {
				solution = ((DynamicElementInfo) argValue).asString();
			}
			return solution;
		}
	}

	
	public static boolean isExpressionLeaf(Expression e) {
		return e instanceof SymbolicInteger || 
				e instanceof SymbolicReal || 
				e instanceof StringSymbolic;
	}
	
	public static Object getLeafSolution(Expression e) {
		if (e instanceof SymbolicInteger) {
			return new Long(((SymbolicInteger) e).solution);
		} else if (e instanceof SymbolicReal) {
			return new Double(((SymbolicReal) e).solution);
		} else if (e instanceof StringSymbolic) {
			return ((StringSymbolic) e).solution;
		} else {
			assert e == null;
			return null;
		}
	}
	
	public static boolean isArrayField(FieldInfo fi) {
		String type = fi.getType();
		return type.substring(type.length() - 2).equals("[]");
	}
	
	public static String toFirstUpper(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public static String tabIndentOnce(String s) {
		return s.replaceAll("(?m)^", "\t");
	}
}