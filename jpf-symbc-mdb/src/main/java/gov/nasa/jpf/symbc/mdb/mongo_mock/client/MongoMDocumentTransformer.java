package gov.nasa.jpf.symbc.mdb.mongo_mock.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bson.Document;

import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument.mField;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocumentTransformer;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;

public class MongoMDocumentTransformer<E> extends MDocumentTransformer<E> {
	
	public MongoMDocumentTransformer(Class<E> entityClass) {
		super(entityClass);
	}
	
	
	public Document toDocument(MDocument mdoc) {
		Document result = new Document();
		for (mField f : mdoc.getFields()) {
			if (f.getVal() instanceof MDocument) {
				Document embeddedInMDoc = toDocument((MDocument) f.getVal());
				result.put(f.getKey(), embeddedInMDoc);
			} else if (f.getVal() instanceof MObjectId) {
				result.put(f.getKey(), ((MObjectId) f.getVal()).toObjectId());
			} else {
				result.put(f.getKey(), f.getVal());
			}
		}
		Object _id = mdoc.get("_id");
		if (_id instanceof MObjectId) {
			result.put("_id", ((MObjectId) _id).toObjectId());
		}
		return result;
	}
	
	// Transform MDocument to the type demanded by a find(...)-query.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public E transform(MDocument mdoc, Class<?> entityClass) {
		assert !MDocument.class.equals(entityClass);
		
		if (Document.class.equals(entityClass)) {
			E result = (E) toDocument(mdoc);
			return result;
		}
		
		try {
			Constructor<?> emptyConstructor = entityClass.getConstructor(new Class[0]);
			E result = (E) emptyConstructor.newInstance();
			if (Map.class.isAssignableFrom(entityClass)) {
				result = (E) toMapOfClass(mdoc, (Map) result);
				return result;
			}
			
			Field[] fields = entityClass.getDeclaredFields();
			
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equals("$jacocoData")) {
					// Special case during coverage analysis.
					continue;
				}
				String fieldName = fields[i].getName();
				if (fieldName.equalsIgnoreCase("id") || fieldName.equalsIgnoreCase("_id")) {
					fieldName = "_id"; // MongoDB is weird like that for automatic mapping.
				}
				if (mdoc.containsKey(fieldName)) {
					fields[i].setAccessible(true); // TODO Array objects, Collection objects, isAccessible
					Object mdocVal = mdoc.get(fieldName);
					if (mdocVal instanceof MDocument) {
						mdocVal = transform((MDocument) mdocVal, fields[i].getType());
					} else if (mdocVal instanceof MObjectId) {
						mdocVal = ((MObjectId) mdocVal).toObjectId();
					}
					fields[i].set(result, mdocVal);
					fields[i].setAccessible(false);
				}
			}
			return result;
		} catch (ClassCastException | IllegalAccessException | 
				InstantiationException | IllegalArgumentException | 
				InvocationTargetException | SecurityException e) { // TODO Differentiate exceptions
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage()); // TODO Correct error message.
		}
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected E toMapOfClass(MDocument mdoc, Map result) {		
		for (mField f : mdoc.getFields()) {
			if (f.getVal() instanceof MDocument) {
				Document embeddedInMDoc = toDocument((MDocument) f.getVal());
				result.put(f.getKey(), embeddedInMDoc);
			} else if (f.getVal ()instanceof MObjectId) {
				result.put(f.getKey(), ((MObjectId) f.getVal()).toObjectId());
			} else {
				result.put(f.getKey(), f.getVal());
			}
		}
		return (E) result;
	}	
}