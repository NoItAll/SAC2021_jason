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
import scenarios_examples_data.Customer;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoRead {
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
	@Test public void test_checkFirstCustomer_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		// Generate assertEquals for method call
		try {
			scenarios_crud.MongoRead.checkFirstCustomer(arg0);
			fail("Uncaught exception expected.");
		} catch (java.util.NoSuchElementException e) {}
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_checkFirstCustomer_0_1() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 0.0);
		arg2.put("status", 0);
		arg0.insertOne(arg2);
		// Generate assertEquals for method call
		scenarios_crud.MongoRead.checkFirstCustomer(arg0);
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg3 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg3.find(eq(arg2.get("_id"))).first());
		assertEquals(1,arg3.countDocuments());
	}
	@Test public void test_checkFirstCustomer_0_3() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 13.0);
		arg2.put("status", 0);
		arg0.insertOne(arg2);
		// Generate assertEquals for method call
		scenarios_crud.MongoRead.checkFirstCustomer(arg0);
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg3 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg3.find(eq(arg2.get("_id"))).first());
		assertEquals(1,arg3.countDocuments());
	}
}