package gov.nasa.jpf.symbc.mdb.symbolic_collection;

import java.util.NoSuchElementException;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.MFilter;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection.mData;

public class InternalDataCursor {
	
	@SuppressWarnings("rawtypes")
	protected mData currentHead;
	protected final boolean collectionWasInsertedInto;
	protected final MFilter filter;
	protected final MCollection<?> belongsTo;
	protected int numberFilterMatched = 0;
	protected boolean nexted = true;
	
	protected InternalDataCursor(
			MFilter filter,
			MCollection<?> belongsTo) {
		this.currentHead = belongsTo.getSafeDataListContainers();
		this.collectionWasInsertedInto = belongsTo.getWasInsertedInto();
		this.filter = filter;
		this.belongsTo = belongsTo;
	}
	
	public boolean hasNext() {
		while (currentHead != null && currentHead.cur != null && nexted) {
			if (!collectionWasInsertedInto && currentHead.cur.isInserted) {
				return false; /// Only valid if MCollection.cutDataForDerivedCursor is true
			}
			prepareMDocument(currentHead.cur);
			if (!currentHead.cur.getIsDeleted() && filter.filter(currentHead.cur)) {
				nexted = false;
				break;
			} else {
				currentHead = currentHead.tail;
			}
		}
		return (currentHead != null && currentHead.cur != null && !currentHead.cur.getIsDeleted());
	}
	
	
	/* Attributes will be used by custom GETFIELD of JPF */
	protected void prepareMDocument(MDocument m) {
		m.setCollectionName(belongsTo.getCollectionName());
		m.setPojoName(belongsTo.getEntityClass().getName());
		m.setContainingMCollection(belongsTo);
		assert m.getFields() != null;
	}
	
	public MDocument next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		nexted = true;
		numberFilterMatched++;
		MDocument cur = currentHead.cur;
		currentHead = currentHead.tail;
		return cur;
	}
	
	public int getNumberFilterMatched() {
		return numberFilterMatched;
	}
}