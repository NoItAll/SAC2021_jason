package gov.nasa.jpf.symbc.mdb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocumentTransformer.NullMDocumentTransformer;

public final class Utility {

	private Utility() {}
	
	private static NullMDocumentTransformer transformer = new NullMDocumentTransformer();
	
	public static boolean isWrappingOrString(Class<?> c) {
		return c.equals(Double.class) || 
				c.equals(Float.class) || 
				c.equals(Long.class) || 
				c.equals(Integer.class) || 
				c.equals(Short.class) || 
				c.equals(Byte.class) || 
				c.equals(Boolean.class) || 
				c.equals(Character.class) || 
				c.equals(String.class);
	}
	
	
	public static Object wrapPrimitive(Field f, Object val) {
		assert isPrimitive(val);
		Class<?> ft = f.getType();
		if (ft.equals(int.class)) {
			return new Integer((int) val);
		} else if (ft.equals(double.class)) {
			return new Double((double) val);
		} else if (ft.equals(float.class)) {
			return new Float((float) val);
		} else if (ft.equals(long.class)) {
			return new Long((long) val);
		} else if (ft.equals(short.class)) {
			return new Short((short) val);
		} else if (ft.equals(byte.class)) {
			return new Byte((byte) val);
		} else if (ft.equals(boolean.class)) {
			return new Boolean((boolean) val);
		} else if (ft.equals(char.class)) {
			return new Character((char) val);
		}
		assert false;
		return null;
	}
	
	public static boolean isPrimitiveWrapperOrString(Object val) {
		return val instanceof Double || 
				val instanceof Float || 
				val instanceof Integer || 
				val instanceof Long ||
				val instanceof Short || 
				val instanceof Byte || 
				val instanceof Boolean || 
				val instanceof String;
	}
	
	public static boolean isPrimitive(Object val) {
		return val != null && 
				(val.getClass().equals(int.class) || 
				val.getClass().equals(float.class) || 
				val.getClass().equals(double.class) || 
				val.getClass().equals(long.class) ||
				val.getClass().equals(short.class) || 
				val.getClass().equals(byte.class) || 
				val.getClass().equals(char.class) ||
				val.getClass().equals(boolean.class));
	}
	
	
	public static Object transformToNumberStringMObjectIdOrMDocument(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Number || value instanceof String || value instanceof Boolean) {
			return value;
		} else if (value instanceof ObjectId) {
			return ((ObjectId) value).toMObjectId();
		} else {
			return transformer.toMDocument(value, true);
		}
	}
	
	
	private static Map<Class<?>, Field[]> classToSortedFields = new HashMap<>();
	private static FieldAlphabeticalComparator fieldComparator = new FieldAlphabeticalComparator();
	
	public static Field[] sortFieldsAlphabetically(Field[] toSort) {
		if (toSort.length == 0) {
			return toSort;
		}
		Class<?> declaringClass = toSort[0].getDeclaringClass();
		Field[] result = classToSortedFields.get(declaringClass);
		if (result != null) {
			return result;
		}
		
		Arrays.sort(toSort, fieldComparator);
		
		classToSortedFields.put(declaringClass, toSort);
		return toSort;
	}
	
	private final static class FieldAlphabeticalComparator implements Comparator<Field> {
		@Override
		public int compare(Field o1, Field o2) {
			String o1Name = o1.getName();
			String o2Name = o2.getName();
			
			//o1Name = isIdAlternative(o1Name) ? "_id" : o1Name;
			//o2Name = isIdAlternative(o2Name) ? "_id" : o2Name;
			
			return o1Name.compareTo(o2Name);
		}
	}
	
	public final static List<String> sortStrings(Collection<String> stringCol) {
		List<String> sortList = new ArrayList<>(stringCol);
		Collections.sort(sortList);
		return sortList;
	}
	
	public final static List<String> sortStringsWithRegardsToId(Collection<String> stringCol) {
		List<String> sortList = new ArrayList<>(stringCol);
		boolean seenOnlyOnce = true;
		for (String s : stringCol) {
			if (isIdAlternative(s)) {
				assert seenOnlyOnce; // Undefined what should happen otherwise
				sortList.add("_id");
				seenOnlyOnce = false;
			} else {
				sortList.add(s);
			}
		}
		return sortStrings(sortList);
	}
	
	public static boolean isIdAlternative(String keyName) {
		return keyName.equalsIgnoreCase("id") || keyName.equalsIgnoreCase("_id");
	}	
}