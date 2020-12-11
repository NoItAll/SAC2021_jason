package gov.nasa.jpf.symbc.mdb.schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;

// Represents a database schema consisting of different versions.
public class Schema {
	
	protected static Map<String, Schema> schemas;

	protected List<SchemaVersion> versions;
	
	public Schema(List<SchemaVersion> versions) {
		this.versions = Collections.unmodifiableList(versions);
	}

	public int numberVersions() {
		return versions.size();
	}

	public SchemaVersion getVersion(int versionNumber) {
		return versions.get(versionNumber);
	}
	
	public List<SchemaVersion> getVersions() {
		return versions;
	}
	
	public static class SchemaVersion {
		
		public final int id;
		// Structure of the schema version:
		// Name of attribute --> {Map<String, Object>::Embedded schema, Class<?>::type of attribute}
		public final Map<String, Object> structure;
		
		public SchemaVersion(int id, Map<String, Object> structure) {
			this.id = id;
			this.structure = Collections.unmodifiableMap(structure);
		}
		
		public int size() {
			return structure.size();
		}
		
		public String toString() {
			return id + "::" + structure;
		}
		
		public int getVersionNumber() {
			return id;
		}
	}
	
	public static Schema get(String collectionName, String pojoName) {
		// TODO add case of MObjectId (?)
		Schema result = schemas.get(collectionName);
		
		if (result != null) {
			return result;
		}  
		try {
			Class<?> pojoClass = Class.forName(pojoName);
			if (!pojoClass.equals(MDocument.class) &&
					!Map.class.isAssignableFrom(pojoClass)) { // The user did not provide a specification for the given collection
			// Try direct POJO-mapping
			result = schemas.get(pojoName);
			if (result != null) {
				return result;
			}
			Map<String, Object> schema = getSchemaForPojoName(pojoName);
			SchemaVersion singleVersion = 
					new SchemaVersion(0, Collections.unmodifiableMap(schema));
			List<SchemaVersion> singleVersionInList = new ArrayList<>();
			singleVersionInList.add(singleVersion);
			result = new Schema(singleVersionInList);
			schemas.put(pojoName, result);
			return result;
		} else {
			/* The schema is unconstrained: Neither a schema for the collection has 
			 * been specified, nor is the input space restricted by a schema derived from 
			 * a regular POJO.
			 */
			throw new RuntimeException("No schema for the collection " + collectionName + 
					" has been found.");
		}
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, Object> getSchemaForPojoName(String pojoName) {
		try {
			Class<?> pojoClass = Class.forName(pojoName);
			Field[] fields = pojoClass.getDeclaredFields();
			fields = Utility.sortFieldsAlphabetically(fields);
			Map<String, Object> schema = new LinkedHashMap<>();
			for (Field f : fields) {
				Class<?> type = f.getType();
				if (Utility.isWrappingOrString(type)) {
					type = type == int.class ? Integer.class : type;
					type = type == double.class ? Double.class : type;
					type = type == float.class ? Float.class : type;
					type = type == long.class ? Long.class : type;
					type = type == short.class ? Short.class : type;
					type = type == byte.class ? Byte.class : type;
					type = type == boolean.class ? Boolean.class : type;
					type = type == char.class ? Character.class : type;
					schema.put(f.getName(), type);
				} else {
					// Initialize empty object to avoid endless loop
					schema.put(f.getName(), new HashMap<>());
				}
			}
			return schema;
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new RuntimeException(cnfe);
		}
	}
	
	public String toString() {
		return versions.toString();
	}
}
