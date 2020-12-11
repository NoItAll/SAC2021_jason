package gov.nasa.jpf.symbc.mdb.symbolic_collection;

import com.mongodb.MongoWriteException;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.MFilter;

public class MCollection<T> {
	protected String name;
	protected int maxNumberInitialDocuments;
	protected Class<T> entityClass; /// TODO Get rid
	public static final int DEFAULT_MAX_NUMBER_INITAL_DOCUMENTS = 3;
	protected mData data;
	protected boolean wasInsertedInto;
	// If a cursor is created, should it receive a copy of the list, so that newly
	// inserted data is not regarded anymore?
	protected boolean cutDataForDerivedCursor;
	
	public class mData {
		protected int depth;
		protected MDocument cur;
		protected mData tail;
		
		public void setCur(MDocument cur) {
			this.cur = cur;
		}
		
		public void setTail(mData tail) {
			this.tail = tail;
		}
	}
	
	
	public MCollection(String name, Class<T> entityClass) {
		this(name, entityClass, DEFAULT_MAX_NUMBER_INITAL_DOCUMENTS);
	}
	
	public MCollection(String name, Class<T> entityClass, final int maxNumberInitialDocuments) {
		this.maxNumberInitialDocuments = maxNumberInitialDocuments;
		this.data = null;
		this.name = name;
		this.entityClass = entityClass;
		this.wasInsertedInto = false;
		this.cutDataForDerivedCursor = true;
	}
	
	public void setData(mData data) {
		this.data = data;
	}
	
	public void set(String name, Class<T> entityClass) {
		this.setName(name);
		this.setEntityClass(entityClass);
	}
	
	public void setEntityClass(Class<T> entityClass) {
		if (entityClass.equals(MDocument.class)) {
			throw new IllegalArgumentException("MDocument should only be used within the database.");
		}
		this.entityClass = entityClass;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setMaxNumberInitialDocuments(int no) {
		this.maxNumberInitialDocuments = no;
	}

	public String getCollectionName() {
		return name;
	}
	
	public Class<T> getEntityClass() {
		return entityClass;
	}
	
	protected int getMaxNumberInitialDocuments() {
		return maxNumberInitialDocuments;
	}
	
	public boolean getWasInsertedInto() {
		return wasInsertedInto;
	}
	
	public void setWasInsertedInto(boolean wasInsertedInto) {
		this.wasInsertedInto = wasInsertedInto;
	}
	
	public boolean getCutDataForDerivedCursor() {
		return cutDataForDerivedCursor;
	}
	
	public void setCutDataForDerivedCursor(boolean cutDataForDerivedCursor) {
		this.cutDataForDerivedCursor = cutDataForDerivedCursor;
	}
	
	/* Copy the data containers (the mData wrappers) while preserving the content (the wrapped MDocuments) if needed.
	 * This is done to limit the view of initialized cursors. Entries which are inserted after starting the cursors must be neglected 
	 * by this cursor. On insert the MCollection appends to a list and holds all data in this list; to avoid appending to it for a cursor, this
	 * cursor gets another wrapper. */
	protected mData getSafeDataListContainers() {
		mData result;
		if (cutDataForDerivedCursor && wasInsertedInto) {
			mData current = data;
			result = new mData();
			mData currentResultTail = result;
			while (current != null && current.cur != null) {
				currentResultTail.cur = current.cur;
				currentResultTail.tail = new mData();
				currentResultTail = currentResultTail.tail;
				current = current.tail;
			}
		} else {
			result = data;
		}
		return result;
	}
	
	public void insertOne(MDocument objToDocument) {
		assert objToDocument != null;
		objToDocument.setIsInserted(true);
		InternalDataCursor cursor = new InternalDataCursor(MFilters.eq(objToDocument.get("_id")), this);
		if (cursor.hasNext()) {
			throw new MongoWriteException("Duplicated id");
		}
		mData currentData = data;
		if (currentData != null) {
			// Get last tail of the mData list
			while (currentData.cur != null) {
				if (currentData.tail == null) {
					mData newTail = new mData();
					currentData.tail = newTail;
					currentData = newTail;
				} else {
					currentData = currentData.tail;
				}
			}
		} else {
			currentData = new mData(); // TODO depth, just for consistency's sake. to append at the end, all data needs to be initialized anyway.
			data = currentData;
		}

		currentData.cur = objToDocument;
		currentData.tail = new mData();
		assert currentData.cur != null;
		wasInsertedInto = true;
	}
	
	public InternalDataCursor iterator() {
		return new InternalDataCursor(MFilters.alwaysTrue(), this);
	}
	
	public InternalDataCursor iterator(MFilter filter) {
		return new InternalDataCursor(filter, this);
	}
}
