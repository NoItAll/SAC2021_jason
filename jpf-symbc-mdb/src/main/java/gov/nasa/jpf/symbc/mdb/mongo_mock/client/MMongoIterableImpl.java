package gov.nasa.jpf.symbc.mdb.mongo_mock.client;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.MFilter;

public class MMongoIterableImpl<T> implements MongoIterable<T> {
	
	private MMongoCollection<T> belongsTo;
	private MFilter filter;
	
	protected MMongoIterableImpl(
			MMongoCollection<T> belongsTo,
			MFilter filter) {
		this.belongsTo = belongsTo;
		this.filter = filter;
	}

	@Override
	public MongoCursor<T> iterator() {
		return cursor();
	}

	@Override
	public T first() {
		try {
			return cursor().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public MongoIterable<T> batchSize(int batchSize) {
		throw new UnsupportedOperationException("Not yet implemented"); 
	}

	@Override
	public <U> MongoIterable<U> map(Function<T, U> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEach(Block<? super T> block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <A extends Collection<? super T>> A into(A target) {
		throw new UnsupportedOperationException("Not yet implemented"); 
	}

	@Override
	public MongoCursor<T> cursor() {
		return new MMongoCursor<>(filter, belongsTo);
	}
}
