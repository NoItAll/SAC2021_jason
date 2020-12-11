package gov.nasa.jpf.symbc.mdb.mongo_mock.client;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.CursorType;
import com.mongodb.Function;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.MFilter;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection.mData;

public class MFindIterableImpl<T> implements FindIterable<T> {
	
	private MMongoIterableImpl<T> delegateTo;
	
	public MFindIterableImpl(
			MMongoCollection<T> belongsTo,
			MFilter filter) {
		delegateTo = new MMongoIterableImpl<>(belongsTo, filter);
	}
	
	@Override
	public MongoCursor<T> iterator() {
		return delegateTo.iterator();
	}

	@Override
	public T first() {
		return delegateTo.first();
	}

	@Override
	public <U> MongoIterable<U> map(Function<T, U> mapper) {
		return delegateTo.map(mapper);
	}

	@Override
	public void forEach(Block<? super T> block) {
		delegateTo.forEach(block);
	}

	@Override
	public FindIterable<T> filter(Bson filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> limit(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> skip(int skip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> maxTime(long maxTime, TimeUnit timeUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> modifiers(Bson modifiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> projection(Bson projection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> sort(Bson sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> noCursorTimeout(boolean noCursorTimeout) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> oplogReplay(boolean oplogReplay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> partial(boolean partial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> cursorType(CursorType cursorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> collation(Collation collation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> comment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> hint(Bson hint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> max(Bson max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> min(Bson min) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> maxScan(long maxScan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> returnKey(boolean returnKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> showRecordId(boolean showRecordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> snapshot(boolean snapshot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A extends Collection<? super T>> A into(A target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindIterable<T> batchSize(int batchSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MongoCursor<T> cursor() {
		return delegateTo.cursor();
	}

	@Override
	public FindIterable<T> hintString(String hint) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
