package gov.nasa.jpf.symbc.mdb.symbolic_collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import gov.nasa.jpf.symbc.mdb.Utility;

// Easier to initialize from GETFIELD than Document, also necessary to instantiate dedicated mock in test classes. 
public final class MDocument {
		
	protected MObjectId _id = null; // Just set for non-embedded
	protected mField[] fields;
	protected boolean isInserted = false;
	protected boolean isUpdated = false;
	protected boolean isDeleted = false;
	protected MDocument originalVersion;
	// Do not remove the helper fields, they are used by JPF-internals
	// Helper field to easily access desired mapping in JPF
	protected String collectionName;
	// Helper field to map the POJO directly if no schema has been specified for collectionName
	protected String pojoName;
	// MCollection this MDocument is contained within, if any. Used by GETFIELD for
	// determining which schema versions can be initialized for this MDocument if a heuristic for
	// shrinking the number of applicable schema versions based on the current content of said 
	// MCollection is applied.
	protected MCollection<?> containingMCollection;
	// Used by GETFIELD to determine which schema version was used to initialize this MDocument
	protected int schemaVersion = -1;
	
	public void setIsDeleted() {
		this.isDeleted = true;
	}
	
	public void setIsUpdated() {
		if (!isUpdated) {
			isUpdated = true;
			originalVersion = this.copy();
		}
	}
	
	public boolean getIsUpdated() {
		return isUpdated;
	}
	
	public boolean getIsDeleted() {
		return isDeleted;
	}
	
	public void setIsInserted(boolean b) {
		this.isInserted = b;
	}
	
	public boolean getIsInserted() {
		return isInserted;
	}
	
	public void setContainingMCollection(MCollection<?> mc) {
		this.containingMCollection = mc;
	}
	
	public MDocument() {
		this(new mField[0], null);
	} // For initialization using JPF and MDocument.transform(...).
	
	public MDocument(mField[] fields, MObjectId _id) {
		this.fields = fields;
		this._id = _id;
	}
	
	public MDocument(MObjectId _id) {
		this(new mField[0], _id);
	}
	
	public MDocument(Map<String, Object> values, MObjectId _id) {
		this._id = _id;
		fields = new mField[0];
		putAll(values);
	}
	
	public void setFields(mField[] fields) {
		this.fields = fields;
	}
	
	public void set_id(MObjectId id) {
		this._id = id;
	}
	
	public MObjectId getMObjectId() {
		return _id;
	}
	
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	public void setPojoName(String pojoName) {
		this.pojoName = pojoName;
	}
	
	public MDocument copy() {
		mField[] newFields = new mField[fields.length];
		for (int i = 0; i < newFields.length; i++) {
			newFields[i] = new mField();
			newFields[i].key = fields[i].key;
			// The value is either null, a wrapper for primitives or an instance of MDocument
			if (fields[i].val == null) {
				newFields[i].val = null;
			} else if (fields[i].val instanceof MDocument) {
				newFields[i].val = ((MDocument) fields[i].val).copy();
			} else {
				Object val = fields[i].val;
				assert Utility.isPrimitiveWrapperOrString(val);
				// Wrappers for primitives are immutable --> ok to copy as-is.
				newFields[i].val = val;
			}
		}
		return new MDocument(newFields, _id == null ? null : _id.copy());
	}
	
	public mField[] getFields() {
		return fields;
	}
	
	public class mField {
		// Do NOT change the names of the attributes.
		protected String key;
		protected Object val;	
		
		public mField() {}
		
		public mField(String key, Object val) {
			this.key = key;
			this.val = val;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
		
		public void setVal(Object val) {
			this.val = val;
		}
		
		public Object getVal() {
			return val;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof mField) {
				mField temp = (mField) o;
				boolean valEquals;
				if (val != null) {
					valEquals = val.equals(temp.val);
				} else {
					valEquals = temp.val == null;
				}
				return temp.key.equals(key) && valEquals;
			} else {
				return false;
			}
		}
		@Override
		public String toString() {
			return key + " -> " + val;
		}
	}
	
	
	public int size() {
		assert fields != null;
		return fields.length + (_id == null ? 0 : 1);
	}
	
	public boolean containsKey(Object key) {
		assert fields != null;
		if (!(key instanceof String)) {
			return false;
		}
		if (_id != null && ((String) key).equals("_id")) {
			return true;
		}
		for (mField f : fields) {
			assert f.key != null; 
			if (f.key.equals(key)) {
				return true;
			}
		}
		return false;
	}

	
	public Object get(Object key) {
		assert fields != null;
		if (!(key instanceof String)) {
			return null;
		}
		
		if (((String) key).equals("_id") && _id != null) {
			return _id;
		}
		
		for (mField f: fields) {
			if (f.key.equals(key)) {
				return f.val;
			}
		}
		return null;
	}

	
	public Object put(String key, Object value) {
		assert fields != null;
		Object previous;
		if (key.equals("_id")) {
			previous = get(key);
			if (value instanceof MObjectId) {
				_id = (MObjectId) value;
				return previous;
			} else {
				_id = null; // It will be set as a regular attribute in the following loop.
			}
		}
		
		// Check if key already resides in fields.
		for (mField f : fields) {
			if (f.key.equals(key)) {
				previous = f.val;
				f.val = value;
				// Return previous value as per specification.
				return previous;
			}
		}
		List<mField> temp = new ArrayList<>(Arrays.asList(fields));

		mField newField = new mField(key, value);
		temp.add(newField);
		fields = temp.toArray(new mField[0]);
		// No value was associated with the key, thus return null as per specification.
		return null;
	}
	
	
	public Object remove(Object key) {
		assert fields != null;
		if (!(key instanceof String)) {
			return null;
		}
		
		if (key.equals("_id")) {
			MObjectId temp = _id;
			_id = null;
			return temp.toObjectId();
		}
		
		List<mField> temp = new ArrayList<>(Arrays.asList(fields));
		for (mField f : temp) {
			if (f.key.equals(key)) {
				temp.remove(f);
				this.fields = temp.toArray(new mField[0]);
				return f.val;
			}
		}
		return null;
	}

	
	public void putAll(Map<? extends String, ?> map) {
		assert fields != null;
		Set<? extends Entry<? extends String, ?>> entries = map.entrySet();
		List<mField> addToFields = new LinkedList<>();
		for (Entry<? extends String, ?> e : entries) {
			addToFields.add(new mField(e.getKey(), e.getValue()));
		}
		
		// Check for all current fields, whether they are updated or a field is added.
		List<mField> result = new ArrayList<>(Arrays.asList(fields));
		for (mField toAdd : addToFields) {
			boolean alreadyInFields = false;
			for (mField toCheck : result) {
				if (toAdd.key.equals(toCheck.key)) {
					toCheck.val = toAdd.val;
					alreadyInFields = true;
					break;
				}
			}
			if (!alreadyInFields) {
				result.add(toAdd);
			}
		}
		fields = result.toArray(new mField[0]);
	}

	
	public void clear() {
		fields = new mField[0];
		_id = null;
	}

	
	public Set<String> keySet() {
		assert fields != null;
		Set<String> keys = new LinkedHashSet<>();
		if (_id != null) {
			keys.add("_id");
		}
		for (mField f : fields) {
			if (f.getKey() != null) {
				keys.add(f.key);
			}
		}
		return keys;
	}

	
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> clazz) {
		return (T) get(key);
	}

	
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, T defaultValue) {
		T res = (T) get(key);
		return res == null? defaultValue : res;
	}

	
	public Integer getInteger(Object key) {
		return (Integer) get(key);
	}

	
	public int getInteger(Object key, int defaultValue) {
		Integer res = getInteger(key);
		return res == null ? defaultValue : res.intValue();
	}

	
	public Long getLong(Object key) {
		return (Long) get(key);
	}

	
	public Double getDouble(Object key) {
		return (Double) get(key);
	}

	
	public String getString(Object key) {
		return (String) get(key);
	}

	
	public Boolean getBoolean(Object key) {
		return (Boolean) get(key);
	}

	
	public boolean getBoolean(Object key, boolean defaultValue) {
		Boolean res = getBoolean(key);
		return res == null ? defaultValue : res.booleanValue();
	}
	
	
	public Date getDate(Object key) {
		return (Date) get(key);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MDocument) {
			MDocument temp = (MDocument) o;
			if (this.size() != temp.size()) {
				return false;
			}
			if (_id != null) {
				if (_id.equals(temp._id)) {
					return true;
				}
			}
			if (temp._id != null) {
				return false;
			}
			
			for (int i = 0; i < fields.length; i++)  {
				if (!this.fields[i].equals(temp.fields[i])) {
					return false;
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		String res = "MDocument{";
		
		for (mField f : fields) {
			res += f.key + "=" + f.val + ",";
		}
		
		if (_id != null) 
			res += "_id=" + _id;
		else 
			res = res.substring(0, res.length() - 1); // Remove ','
		res += "}";
		return res;
	}
}