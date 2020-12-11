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
import scenarios_crud.MongoRead_5;
import scenarios_examples_data.Customer;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoRead_5 {
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
	@Test public void test_readCustomersUsingLTEFilters_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		int arg1 = (int) 0;
		String arg2 = "0";
		double arg3 = (double) 0.0;
		MongoRead_5 arg4 = new MongoRead_5();
		// Generate assertEquals for method call
		assertEquals(null,arg4.readCustomersUsingLTEFilters(arg0,arg1,arg2,arg3));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg5 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(0,arg5.countDocuments());
	}
	@Test public void test_readCustomersUsingLTEFilters_0_536() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 21.1);
		arg2.put("status", 14);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#106");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#107");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 21.05);
		arg4.put("status", 0);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#118");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#119");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 21.05);
		arg6.put("status", 0);
		arg0.insertOne(arg6);
		int arg7 = (int) 0;
		String arg8 = "0";
		double arg9 = (double) 0.0;
		MongoRead_5 arg10 = new MongoRead_5();
		// Generate assertEquals for method call
		assertEquals(null,arg10.readCustomersUsingLTEFilters(arg0,arg7,arg8,arg9));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg11 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg11.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg11.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg11.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg11.countDocuments());
	}
	@Test public void test_readCustomersUsingLTEFilters_0_538() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 21.1);
		arg2.put("status", 14);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#106");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#107");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 21.1);
		arg4.put("status", 0);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#118");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#119");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 23.0);
		arg6.put("status", 0);
		arg0.insertOne(arg6);
		int arg7 = (int) 0;
		String arg8 = "0";
		double arg9 = (double) 0.0;
		MongoRead_5 arg10 = new MongoRead_5();
		// Generate assertEquals for method call
		Customer arg11 = new Customer();
		arg11.setId("DummyString#119");
		arg11.setIncome((double) 23.0);
		Integer arg14 = new Integer(0);
		arg11.setStatus(arg14);
		Customer arg15 = new Customer();
		arg15.setId("DummyString#118");
		arg15.setIncome((double) 0.0);
		arg15.setStatus(null);
		arg15.setEnlistedBy(null);
		arg11.setEnlistedBy(arg15);
		assertEquals(arg11,arg10.readCustomersUsingLTEFilters(arg0,arg7,arg8,arg9));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg18 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg18.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg18.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg18.find(eq(arg6.get("_id"))).first());
		assertEquals(3,arg18.countDocuments());
	}
}