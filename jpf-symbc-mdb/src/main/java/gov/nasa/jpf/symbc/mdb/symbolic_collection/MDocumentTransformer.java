package gov.nasa.jpf.symbc.mdb.symbolic_collection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument.mField;

public abstract class MDocumentTransformer<E> {
	protected final Class<?> entityClass;
	
	public MDocumentTransformer(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}
	
	public E transform(MDocument mdoc) {
		return transform(mdoc, entityClass);
	}
	
	public abstract E transform(MDocument mdoc, Class<?> entityClass);
	
	public <T> MDocument toMDocument(T toTransform) {
		return toMDocument(toTransform, false);
	}
	
	@SuppressWarnings("unchecked") // Standard implementation follows MongoCollection
	public <T> MDocument toMDocument(T toTransform, boolean isEmbedded) { // For insert and updates
		if (toTransform == null) {
			return null;
		}
		if (toTransform instanceof MDocument) { 
			return ((MDocument) toTransform).copy();
		}
		boolean containsIdAlternative = false;
		MObjectId _id = null;
		if (toTransform instanceof Map) { // TODO Also implement for array / List etc.
			Map<String, Object> temp = (Map<String, Object>) toTransform;
			Map<String, Object> allFields = new HashMap<>();
			for (Map.Entry<String, Object> entry : temp.entrySet()) {
				if (!(entry.getKey() instanceof String)) {
					continue;
				}
				String keyName = entry.getKey();
				if (keyName.equals("_id") && entry.getValue() instanceof ObjectId) {
					_id = ((ObjectId) entry.getValue()).toMObjectId();
					continue;
				} else if (keyName.equals("_id")) {
					containsIdAlternative = true;
				}
				
				if (entry.getValue() instanceof ObjectId) {
					allFields.put(keyName, ((ObjectId) entry.getValue()).toMObjectId());
				} else if (entry.getValue() instanceof Map) {
					allFields.put(keyName, toMDocument(entry.getValue(), true));
				} else if (isPrimitiveWrapperOrString(entry.getValue())) {
					allFields.put(keyName, entry.getValue());
				} else {
					allFields.put(keyName, toMDocument(entry.getValue(), true));
				}
			}
			if (_id == null && !isEmbedded && !containsIdAlternative) {
				_id = new MObjectId();
			}
			return new MDocument(allFields, _id);
		}
		try {
			Class<?> toTransformClass = toTransform.getClass();
			// MongoDB sorts the keys alphabetically in the automatic pojo-mapping
			Field[] fields = Utility.sortFieldsAlphabetically(toTransformClass.getDeclaredFields());
			// MObjectId is automatically generated: 
			Map<String, Object> values = new LinkedHashMap<>();
			for (Field f : fields ) {
				boolean accessibility = f.isAccessible();
				f.setAccessible(true);
				Object fieldVal = f.get(toTransform);
				if (fieldVal == null) {
					f.setAccessible(accessibility);
					continue;
				}
				String fieldName = f.getName();
				if (fieldName.equals("$jacocoData")) {
					f.setAccessible(accessibility);
					continue;
				}
				if (fieldName.equalsIgnoreCase("id") || fieldName.equalsIgnoreCase("_id")) {
					fieldName = "_id";
					if (fieldVal != null) {
						containsIdAlternative = true;
					}
				}
				if (isPrimitiveWrapperOrString(fieldVal)) {
					values.put(fieldName, fieldVal);
				} else if (isPrimitive(fieldVal)) {
					values.put(fieldName, wrapPrimitive(f, fieldVal));
				} else {
					// Is "usual" object
					values.put(fieldName, toMDocument(fieldVal, true));
				}
				f.setAccessible(accessibility);
			}
			if (_id == null && !isEmbedded && !containsIdAlternative) {
				_id = new MObjectId();
			}
			MDocument result = new MDocument(values, _id);
			return result;
		} catch (ClassCastException | IllegalAccessException | 
				IllegalArgumentException | SecurityException e) { // TODO Differentiate Exceptions
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	protected static Object wrapPrimitive(Field f, Object val) {
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
	
	protected static boolean isPrimitiveWrapperOrString(Object val) {
		return val instanceof Double || 
				val instanceof Float || 
				val instanceof Integer || 
				val instanceof Long ||
				val instanceof Short || 
				val instanceof Byte || 
				val instanceof Boolean || 
				val instanceof String;
	}
	
	protected static boolean isPrimitive(Object val) {
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
	
	public static class NullMDocumentTransformer extends MDocumentTransformer<MDocument> {
		public NullMDocumentTransformer() {
			super(MDocument.class);
		}
		
		public MDocument transform(MDocument mdoc, Class<?> entityClass) {
			return mdoc;
		}
	}
}