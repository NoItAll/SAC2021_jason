package gov.nasa.jpf.symbc.mdb.mongo_mock.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateViewOptions;

import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;

public class MMongoDatabase implements MongoDatabase { 
	// TODO To fix: multiple views on the same collection...currently, 
	// because of the usage of entity-class, a collection can only be manifested with one type
	protected Map<String, MMongoCollection<?>> collections = new HashMap<>();
	protected String name;
	
	public MMongoDatabase(String name) {
		this.name = name;
	}
	
	
	public MMongoCollection<Document> getCollection(String collectionName) {
		return getCollection(collectionName, Document.class);
	}
	
	@Override
	public <T> MMongoCollection<T> getCollection(
			String collectionName, 
			Class<T> documentClass) {
		MMongoCollection<T> result = (MMongoCollection<T>) collections.get(collectionName);
		if (result == null) {
			result = new MMongoCollection<>(collectionName, documentClass);
			this.collections.put(collectionName, result);
		}
		if (!result.getDocumentClass().equals(documentClass)) {
			result.setEntityClass(documentClass); // TODO Fix for interleaving sequential accesses to the same collection with different classes
		}
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public CodecRegistry getCodecRegistry() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ReadPreference getReadPreference() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public WriteConcern getWriteConcern() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ReadConcern getReadConcern() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public MongoDatabase withReadPreference(ReadPreference readPreference) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public MongoDatabase withReadConcern(ReadConcern readConcern) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public Document runCommand(Bson command) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public Document runCommand(Bson command, ReadPreference readPreference) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> TResult runCommand(Bson command, Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> TResult runCommand(Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public Document runCommand(ClientSession clientSession, Bson command) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public Document runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> TResult runCommand(ClientSession clientSession, Bson command, Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> TResult runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference,
			Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void drop() {
		MObjectId.NEXT_COUNTER = 0;
		this.collections.clear();
	}

	@Override
	public void drop(ClientSession clientSession) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public MongoIterable<String> listCollectionNames() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ListCollectionsIterable<Document> listCollections() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> ListCollectionsIterable<TResult> listCollections(Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public MongoIterable<String> listCollectionNames(ClientSession clientSession) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ListCollectionsIterable<Document> listCollections(ClientSession clientSession) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> ListCollectionsIterable<TResult> listCollections(ClientSession clientSession,
			Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createCollection(String collectionName) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createCollection(String collectionName, CreateCollectionOptions createCollectionOptions) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createCollection(ClientSession clientSession, String collectionName) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createCollection(ClientSession clientSession, String collectionName,
			CreateCollectionOptions createCollectionOptions) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createView(String viewName, String viewOn, List<? extends Bson> pipeline) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createView(String viewName, String viewOn, List<? extends Bson> pipeline,
			CreateViewOptions createViewOptions) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline,
			CreateViewOptions createViewOptions) {
		throw new UnsupportedOperationException("Not implemented in mock");		
	}

	@Override
	public ChangeStreamIterable<Document> watch() {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline,
			Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public AggregateIterable<Document> aggregate(List<? extends Bson> pipeline) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public AggregateIterable<Document> aggregate(ClientSession clientSession, List<? extends Bson> pipeline) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}

	@Override
	public <TResult> AggregateIterable<TResult> aggregate(ClientSession clientSession, List<? extends Bson> pipeline,
			Class<TResult> resultClass) {
		throw new UnsupportedOperationException("Not implemented in mock");
	}
}
