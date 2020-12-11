package jpf.symbc.mdb.generated_integration_tests;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.junit.*;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoRead_2 {
	protected static MongoDatabase mongoDatabase;
	@BeforeClass
	public static void setUpClass() {
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
			CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
		mongoDatabase = mongoClient.getDatabase("devel");
		mongoDatabase.drop();
	}
	@After
	public void tearDown() {
		mongoDatabase.drop();
	}
	@Test public void test_readSchemaUsingDocument_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Document.class);
		// Generate assertEquals for method call
		assertEquals(null,scenarios_crud.MongoRead_2.readSchemaUsingDocument(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_readSchemaUsingDocument_0_394() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Document.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 0.0);
		arg2.put("status", 14);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#50");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#51");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 0.0);
		arg4.put("status", 14);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#60");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#61");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 0.0);
		arg6.put("status", 0);
		arg0.insertOne(arg6);
		// Generate assertEquals for method call
		assertEquals(null,scenarios_crud.MongoRead_2.readSchemaUsingDocument(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg7 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg7.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg7.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg7.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg7.countDocuments());
	}
	@Test public void test_readSchemaUsingDocument_0_408() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Document.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 13.0);
		arg2.put("status", 14);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#50");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#51");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 13.0);
		arg4.put("status", 14);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#60");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#61");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 13.0);
		arg6.put("status", 0);
		arg0.insertOne(arg6);
		// Generate assertEquals for method call
		Document arg7 = new Document();
		String arg8 = "enlistedBy";
		Document arg9 = new Document();
		String arg10 = "_id";
		String arg11 = "DummyString#60";
		arg9.put(arg10,arg11);
		arg7.put(arg8,arg9);
		String arg12 = "_id";
		String arg13 = "DummyString#61";
		arg7.put(arg12,arg13);
		String arg14 = "income";
		Double arg15 = new Double(13.0);
		arg7.put(arg14,arg15);
		String arg16 = "status";
		Integer arg17 = new Integer(0);
		arg7.put(arg16,arg17);
		assertEquals(arg7,scenarios_crud.MongoRead_2.readSchemaUsingDocument(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg18 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg18.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg18.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg18.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg18.countDocuments());
	}
	@Test public void test_readSchemaUsingDocument_0_423() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Document.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 13.0);
		arg2.put("status", 14);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#50");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#51");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 13.0);
		arg4.put("status", 14);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#60");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#61");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 13.0);
		arg6.put("status", 14);
		arg0.insertOne(arg6);
		// Generate assertEquals for method call
		Document arg7 = new Document();
		String arg8 = "enlistedBy";
		Document arg9 = new Document();
		String arg10 = "_id";
		String arg11 = "DummyString#0";
		arg9.put(arg10,arg11);
		arg7.put(arg8,arg9);
		String arg12 = "_id";
		String arg13 = "DummyString#1";
		arg7.put(arg12,arg13);
		String arg14 = "income";
		Double arg15 = new Double(13.0);
		arg7.put(arg14,arg15);
		String arg16 = "status";
		Integer arg17 = new Integer(14);
		arg7.put(arg16,arg17);
		assertEquals(arg7,scenarios_crud.MongoRead_2.readSchemaUsingDocument(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg18 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg18.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg18.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg18.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg18.countDocuments());
	}
}