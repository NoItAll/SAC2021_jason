package gov.nasa.jpf.symbc.mdb.mongo_mock.client;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.MFilter;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.InternalDataCursor;

public class MMongoCursor<T> implements MongoCursor<T> {
	
	protected final InternalDataCursor delegateTo;
	protected final MongoMDocumentTransformer<T> transformer;

	public MMongoCursor(
			MFilter filter,
			MMongoCollection<T> belongsTo) {
		delegateTo = belongsTo.getCollection().iterator(filter);
		transformer = belongsTo.getTransformer();
	}
	
	@Override
	public void close() {}

	@Override
	public ServerAddress getServerAddress() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public ServerCursor getServerCursor() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public boolean hasNext() {
		return delegateTo.hasNext();
	}

	@Override
	public T next() {
		return transformer.transform(delegateTo.next());
	}

	@Override
	public T tryNext() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

}
