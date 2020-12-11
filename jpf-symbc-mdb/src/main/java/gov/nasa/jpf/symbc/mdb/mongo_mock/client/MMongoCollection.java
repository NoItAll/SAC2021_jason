package gov.nasa.jpf.symbc.mdb.mongo_mock.client;

import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.EstimatedDocumentCountOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.MFilter;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MUpdates.MUpdate;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.InternalDataCursor;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;

public class MMongoCollection<E> implements MongoCollection<E> {
	
	private final MCollection<E> collection;
	private MongoMDocumentTransformer<E> transformer; // TODO final?

	@SuppressWarnings("unchecked")
	public MMongoCollection(String name) {
		this(name, (Class<E>) Document.class);
	}
	
	public MMongoCollection(String name, Class<E> entityClass) {
		collection = new MCollection<>(name, entityClass);
		this.transformer = new MongoMDocumentTransformer<>(entityClass);
	}
	
	@SuppressWarnings("unchecked")
	public MMongoCollection(String name, final int maxNumberInitialDocuments) {
		this(name, (Class<E>) Document.class, maxNumberInitialDocuments);
	}
	
	public MMongoCollection(
			String name, 
			Class<E> entityClass,
			final int maxNumberInitialDocuments) {
		collection = new MCollection<>(name, entityClass, maxNumberInitialDocuments);
		this.transformer = new MongoMDocumentTransformer<>(entityClass);
	}
	
	public void setEntityClass(Class<E> entityClass) {
		if (entityClass.equals(MDocument.class)) {
			throw new IllegalArgumentException("MDocument should only be used within the database.");
		}
		collection.setEntityClass(entityClass);
		transformer = new MongoMDocumentTransformer<>(entityClass);
	}
	
	public MCollection<E> getCollection() {
		return collection;
	}
	
	public MongoMDocumentTransformer<E> getTransformer() {
		return transformer;
	}
	
	@Override
	public FindIterable<E> find() {
		return new MFindIterableImpl<>(this, MFilters.alwaysTrue());
	}
	
	@Override
	public FindIterable<E> find(Bson filter) {
		if (filter instanceof MFilter) {
			return find((MFilter) filter);
		}
		throw new UnsupportedOperationException("Filter type is not supported.");
	}

	@Override
	public long count(Bson filter) {
		if (filter instanceof MFilter) {
			return count((MFilter) filter);
		}
		throw new UnsupportedOperationException("Only MFilter-instances are supported.");
	}
	
	@Override
	public long countDocuments() {
		return count();
	}
	
	@Override
	public long countDocuments(Bson filter) {
		return count(filter);
	}
	
	@Override
	public E findOneAndDelete(Bson filter) {
		if (filter instanceof MFilter) {
			MDocument result = findOneAndDelete((MFilter) filter);
			if (result != null) {
				return transformer.transform(result);
			} else {
				return null;
			}
		}
		throw new UnsupportedOperationException("Filter must be instance of MFilter.");
	}
	
	@Override
	public E findOneAndReplace(Bson filter, E replacement) {
		if (filter instanceof MFilter) {
			findOneAndReplace(
					(MFilter) filter,
					transformer.toMDocument(replacement)
			);
		}
		throw new UnsupportedOperationException("Filter must be an instance of MFilter.");
	}
	
	@Override
	public E findOneAndUpdate(Bson filter, Bson update) {
		if (filter instanceof MFilter && 
				(update instanceof MUpdate)) {
			MDocument result = findOneAndUpdate((MFilter) filter, (MUpdate) update);
			if (result != null) {
				return transformer.transform(result);
			} else {
				return null;
			}
		}
		throw new UnsupportedOperationException("Filter must be an instance of MFilter, "
				+ "update must be an instance of MUpdate.");
	}
	
	@Override
	public DeleteResult deleteOne(Bson filter) {
		if (filter instanceof MFilter) {
			return deleteOne((MFilter) filter);
		}
		throw new UnsupportedOperationException("Filter type is not supported.");
	}
	
	@Override
	public UpdateResult updateMany(Bson filter, Bson update) {
		if (filter instanceof MFilter && 
				(update instanceof MUpdate)) {
			return updateMany((MFilter) filter, (MUpdate) update);
		}
		throw new UnsupportedOperationException("Filter must be an instance of MFilter, "
				+ "update must be an instance of MUpdate.");
	}
	
	@Override
	public UpdateResult updateOne(Bson filter, Bson update) {
		if (filter instanceof MFilter && 
				(update instanceof MUpdate)) {
			return updateOne((MFilter) filter, (MUpdate) update);
		}
		throw new UnsupportedOperationException("Filter must be an instance of MFilter, "
				+ "update must be an instance of MUpdate.");
	}
	
	@Override
	public DeleteResult deleteMany(Bson filter) {
		if (filter instanceof MFilter) {
			return deleteMany((MFilter) filter);
		}
		throw new UnsupportedOperationException("Filter type is not supported.");
	}

	@Override
	public void insertOne(E arg0) {
		collection.insertOne(transformer.toMDocument(arg0));
	}
	
	public void insertMany(List<? extends E> documents) {
		for (E document : documents) {
			collection.insertOne(transformer.toMDocument(document));
		}
	}
	
	public FindIterable<E> find(MFilter filter) {
		return new MFindIterableImpl<>(this, filter);
	}
	
	public long count() {
		return count(MFilters.alwaysTrue());
	}
	
	public long count(MFilter filter) {
		long count = 0;
		MongoCursor<E> it = new MFindIterableImpl<>(this, filter).iterator();
		while (it.hasNext()) {
			count++;
			it.next();
		}
		return count;
	}

	public MDocument findOneAndDelete(MFilter filter) {
		InternalDataCursor idc = collection.iterator(filter);
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			cur.setIsDeleted();
			return cur.copy();
		}
		return null;
	}
	
	public MDocument findOneAndReplace(MFilter filter, MDocument replacement) {
		InternalDataCursor idc = collection.iterator(filter);
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			cur.setIsDeleted();
			if (cur.containsKey("_id")) {
				replacement.put("_id", cur.get("_id"));
			}
			collection.insertOne(replacement);
			return cur.copy();
		}
		return null;
	}
	
	public MDocument findOneAndUpdate(MFilter filter, MUpdate update) {
		InternalDataCursor idc = collection.iterator(filter);
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			MDocument before = cur.copy();
			update.update(cur);
			return before;
		}
		return null;
	}
	
	public DeleteResult deleteOne(MFilter filter) {
		InternalDataCursor idc = collection.iterator(filter);
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			cur.setIsDeleted();
			return DeleteResult.acknowledged(1);
		}
		return DeleteResult.acknowledged(0);
	}
	
	public void drop() {
		deleteMany(MFilters.alwaysTrue());
	}
	
	public UpdateResult updateMany(MFilter filter, MUpdate update) {
		InternalDataCursor idc = collection.iterator(filter);
		int numberUpdated = 0;
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			if (update.update(cur)) {
				numberUpdated++;
			}
		}
		return UpdateResult.acknowledged(idc.getNumberFilterMatched(), (long) numberUpdated, null);
	}
	
	public UpdateResult updateOne(MFilter filter, MUpdate update) {
		InternalDataCursor idc = collection.iterator(filter);
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			if (update.update(cur)) {
				return UpdateResult.acknowledged(idc.getNumberFilterMatched(), (long) 1, null);
			}
		}
		return UpdateResult.acknowledged(idc.getNumberFilterMatched(), (long) 0, null);
	}
	
	public DeleteResult deleteMany(MFilter filter) {
		InternalDataCursor idc = collection.iterator(filter);
		while (idc.hasNext()) {
			MDocument cur = idc.next();
			cur.setIsDeleted();
		}
		return DeleteResult.acknowledged(idc.getNumberFilterMatched());
	}
	
	@Override
	public AggregateIterable<E> aggregate(List<? extends Bson> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public AggregateIterable<E> aggregate(ClientSession arg0, List<? extends Bson> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public <TResult> AggregateIterable<TResult> aggregate(ClientSession arg0, List<? extends Bson> arg1,
			Class<TResult> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends E>> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends E>> arg0, BulkWriteOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public BulkWriteResult bulkWrite(ClientSession arg0, List<? extends WriteModel<? extends E>> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public BulkWriteResult bulkWrite(ClientSession arg0, List<? extends WriteModel<? extends E>> arg1,
			BulkWriteOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public long count(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public long count(Bson arg0, CountOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public long count(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public long count(ClientSession arg0, Bson arg1, CountOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public long countDocuments(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public long countDocuments(Bson arg0, CountOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public long countDocuments(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public long countDocuments(ClientSession arg0, Bson arg1, CountOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public String createIndex(Bson arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public String createIndex(Bson arg0, IndexOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public String createIndex(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public String createIndex(ClientSession arg0, Bson arg1, IndexOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public List<String> createIndexes(List<IndexModel> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public List<String> createIndexes(List<IndexModel> arg0, CreateIndexOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public List<String> createIndexes(ClientSession arg0, List<IndexModel> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public List<String> createIndexes(ClientSession arg0, List<IndexModel> arg1, CreateIndexOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public DeleteResult deleteMany(Bson arg0, DeleteOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public DeleteResult deleteMany(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public DeleteResult deleteMany(ClientSession arg0, Bson arg1, DeleteOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public DeleteResult deleteOne(Bson arg0, DeleteOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public DeleteResult deleteOne(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public DeleteResult deleteOne(ClientSession arg0, Bson arg1, DeleteOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> DistinctIterable<TResult> distinct(String arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> DistinctIterable<TResult> distinct(String arg0, Bson arg1, Class<TResult> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> DistinctIterable<TResult> distinct(ClientSession arg0, String arg1, Class<TResult> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> DistinctIterable<TResult> distinct(ClientSession arg0, String arg1, Bson arg2,
			Class<TResult> arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void drop(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(String arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(Bson arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(String arg0, DropIndexOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(Bson arg0, DropIndexOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(ClientSession arg0, String arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(ClientSession arg0, String arg1, DropIndexOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndex(ClientSession arg0, Bson arg1, DropIndexOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndexes() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndexes(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndexes(DropIndexOptions arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void dropIndexes(ClientSession arg0, DropIndexOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public long estimatedDocumentCount() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public long estimatedDocumentCount(EstimatedDocumentCountOptions arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> FindIterable<TResult> find(Class<TResult> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}
	
	@Override
	public FindIterable<E> find(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> FindIterable<TResult> find(Bson arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> FindIterable<TResult> find(ClientSession arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public FindIterable<E> find(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> FindIterable<TResult> find(ClientSession arg0, Bson arg1, Class<TResult> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}
	
	@Override
	public E findOneAndDelete(Bson arg0, FindOneAndDeleteOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndDelete(ClientSession arg0, Bson arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndDelete(ClientSession arg0, Bson arg1, FindOneAndDeleteOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndReplace(Bson arg0, E arg1, FindOneAndReplaceOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndReplace(ClientSession arg0, Bson arg1, E arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndReplace(ClientSession arg0, Bson arg1, E arg2, FindOneAndReplaceOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(Bson arg0, List<? extends Bson> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(Bson arg0, Bson arg1, FindOneAndUpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(ClientSession arg0, Bson arg1, Bson arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(Bson arg0, List<? extends Bson> arg1, FindOneAndUpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(ClientSession arg0, Bson arg1, List<? extends Bson> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(ClientSession arg0, Bson arg1, Bson arg2, FindOneAndUpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public E findOneAndUpdate(ClientSession arg0, Bson arg1, List<? extends Bson> arg2, FindOneAndUpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public CodecRegistry getCodecRegistry() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public Class<E> getDocumentClass() {
		return collection.getEntityClass();
	}

	@Override
	public MongoNamespace getNamespace() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ReadConcern getReadConcern() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ReadPreference getReadPreference() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public WriteConcern getWriteConcern() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void insertMany(List<? extends E> arg0, InsertManyOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void insertMany(ClientSession arg0, List<? extends E> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void insertMany(ClientSession arg0, List<? extends E> arg1, InsertManyOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void insertOne(E arg0, InsertOneOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void insertOne(ClientSession arg0, E arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void insertOne(ClientSession arg0, E arg1, InsertOneOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ListIndexesIterable<Document> listIndexes() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> ListIndexesIterable<TResult> listIndexes(Class<TResult> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ListIndexesIterable<Document> listIndexes(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> ListIndexesIterable<TResult> listIndexes(ClientSession arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public MapReduceIterable<E> mapReduce(String arg0, String arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> MapReduceIterable<TResult> mapReduce(String arg0, String arg1, Class<TResult> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public MapReduceIterable<E> mapReduce(ClientSession arg0, String arg1, String arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> MapReduceIterable<TResult> mapReduce(ClientSession arg0, String arg1, String arg2,
			Class<TResult> arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void renameCollection(MongoNamespace arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void renameCollection(MongoNamespace arg0, RenameCollectionOptions arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void renameCollection(ClientSession arg0, MongoNamespace arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public void renameCollection(ClientSession arg0, MongoNamespace arg1, RenameCollectionOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult replaceOne(Bson arg0, E arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult replaceOne(Bson arg0, E arg1, UpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult replaceOne(Bson arg0, E arg1, ReplaceOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult replaceOne(ClientSession arg0, Bson arg1, E arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult replaceOne(ClientSession arg0, Bson arg1, E arg2, UpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult replaceOne(ClientSession arg0, Bson arg1, E arg2, ReplaceOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(Bson arg0, List<? extends Bson> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(Bson arg0, Bson arg1, UpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(ClientSession arg0, Bson arg1, Bson arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(Bson arg0, List<? extends Bson> arg1, UpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(ClientSession arg0, Bson arg1, List<? extends Bson> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(ClientSession arg0, Bson arg1, Bson arg2, UpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateMany(ClientSession arg0, Bson arg1, List<? extends Bson> arg2, UpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(Bson arg0, List<? extends Bson> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(Bson arg0, Bson arg1, UpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(ClientSession arg0, Bson arg1, Bson arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(Bson arg0, List<? extends Bson> arg1, UpdateOptions arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(ClientSession arg0, Bson arg1, List<? extends Bson> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(ClientSession arg0, Bson arg1, Bson arg2, UpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public UpdateResult updateOne(ClientSession arg0, Bson arg1, List<? extends Bson> arg2, UpdateOptions arg3) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ChangeStreamIterable<E> watch() {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ChangeStreamIterable<E> watch(List<? extends Bson> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ChangeStreamIterable<E> watch(ClientSession arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(ClientSession arg0, Class<TResult> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public ChangeStreamIterable<E> watch(ClientSession arg0, List<? extends Bson> arg1) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(ClientSession arg0, List<? extends Bson> arg1,
			Class<TResult> arg2) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public MongoCollection<E> withCodecRegistry(CodecRegistry arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public <NewTDocument> MongoCollection<NewTDocument> withDocumentClass(Class<NewTDocument> arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public MongoCollection<E> withReadConcern(ReadConcern arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public MongoCollection<E> withReadPreference(ReadPreference arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}

	@Override
	public MongoCollection<E> withWriteConcern(WriteConcern arg0) {
		throw new UnsupportedOperationException("Not yet implemented.");
		
	}
}
