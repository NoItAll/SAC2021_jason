package gov.nasa.jpf.symbc.mdb.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.schema.Schema.SchemaVersion;

public class SchemaReader {
	
	protected static Set<String> schemaNames;
	
	public static void invoke(String resourcePath) {
		File schemaDir = new File(resourcePath);
		assert schemaDir.isDirectory();
		Map<String, Schema> result = new HashMap<>();
		String[] fileList = schemaDir.list();
		
		for (String f : fileList) {
			if (!f.substring(f.length()-5).equals(".json")) {
				continue;
			} else {
				result.put(f.substring(0, f.length()-5), 
						readSchema(resourcePath + "/" + f));
			}
		}
		
		Schema.schemas = result;
	}
	
	protected static Schema readSchema(String schemaPath) { 
		int versionNumber = 0;
		List<SchemaVersion> versions = new ArrayList<>();
		FileReader reader;
		try {
			reader = new FileReader(schemaPath);
			JSONParser parser = new JSONParser();
			JSONArray schemaVersionsRaw;
			schemaVersionsRaw = (JSONArray) parser.parse(reader);
			for (Object osvr : schemaVersionsRaw) {
				JSONObject svr = (JSONObject) osvr;
				Map<String, Object> attrToClass = getSchemaFromJSON(svr);
				SchemaVersion version = new SchemaVersion(versionNumber, attrToClass);
				versions.add(version);
				versionNumber++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return new Schema(versions);
	}
	
	protected static Map<String, Object> getSchemaFromJSON(JSONObject svr) {
		Map<String, Object> attrToClass = new LinkedHashMap<>();
		List<String> sortedAttrNames = Utility.sortStrings(svr.keySet());
		try {
			for (String s : sortedAttrNames) {
				Object val = svr.get(s);
				if (val instanceof String) {
					Class<?> representedClass = getClassForName((String) val);
					attrToClass.put(s, representedClass);
				} else if (val instanceof JSONObject){
					attrToClass.put(s, getSchemaFromJSON((JSONObject) val));
				} else {
					throw new RuntimeException("Currently, the schema only supports either type-names or nested JSON-objects.");
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return attrToClass;
	}
	
	// Returns either the name itself, if it represents another schema, or the Class-object of the schema's field.
	protected static Class<?> getClassForName(String name) throws ClassNotFoundException {
		if (name.equalsIgnoreCase("int") || name.equalsIgnoreCase("integer")) {
			return Integer.class;
		} else if (name.equalsIgnoreCase("long")) {
			return Long.class;
		} else if (name.equalsIgnoreCase("short")) {
			return Short.class;
		} else if (name.equalsIgnoreCase("byte")) {
			return Byte.class;
		} else if (name.equalsIgnoreCase("boolean")) {
			return Boolean.class;
		} else if (name.equalsIgnoreCase("double")) {
			return Double.class;
		} else if (name.equalsIgnoreCase("float")) {
			return Float.class;
		} else if (name.equalsIgnoreCase("string")) {
			return String.class;
		} else if (name.equalsIgnoreCase("char") || name.equalsIgnoreCase("character")) {
			return Character.class;
		} else if (name.charAt(0) == '[' && name.charAt(name.length() - 1) == ']') {
			return Array.newInstance(getClassForName(name.substring(1, name.length() - 1)), 1).getClass();
		} else {
			throw new IllegalArgumentException("Given class in JSON-schema is no primitive or String.");
		}
	}
}
