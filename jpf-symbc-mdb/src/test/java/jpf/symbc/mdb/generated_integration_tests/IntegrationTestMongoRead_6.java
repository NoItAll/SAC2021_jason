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
public class IntegrationTestMongoRead_6 {
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
	@Test public void test_readCustomersUsingAndFilters_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		// Generate assertEquals for method call
		assertEquals(1,scenarios_crud.MongoRead_6.readCustomersUsingAndFilters(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_readCustomersUsingAndFilters_0_3() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 0.0);
		arg2.put("status", 14);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#2");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#3");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 0.0);
		arg4.put("status", 14);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#4");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#5");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 0.0);
		arg6.put("status", 14);
		arg0.insertOne(arg6);
		// Generate assertEquals for method call
		assertEquals(3,scenarios_crud.MongoRead_6.readCustomersUsingAndFilters(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg7 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg7.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg7.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg7.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg7.countDocuments());
	}
	@Test public void test_readCustomersUsingAndFilters_0_37() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 0.0);
		arg2.put("status", 15);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#18");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#19");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 0.0);
		arg4.put("status", 15);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#24");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#25");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 0.0);
		arg6.put("status", 14);
		arg0.insertOne(arg6);
		// Generate assertEquals for method call
		assertEquals(1,scenarios_crud.MongoRead_6.readCustomersUsingAndFilters(arg0));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg7 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg7.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg7.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg7.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg7.countDocuments());
	}
}