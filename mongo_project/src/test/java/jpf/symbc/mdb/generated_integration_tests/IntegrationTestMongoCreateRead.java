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
public class IntegrationTestMongoCreateRead {
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
	@Test public void test_insertAndReadCustomer_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		double arg1 = (double) 0.0;
		// Generate assertEquals for method call
		Customer arg2 = new Customer();
		arg2.setId("Turing123");
		arg2.setIncome((double) 0.0);
		Integer arg5 = new Integer(2);
		arg2.setStatus(arg5);
		arg2.setEnlistedBy(null);
		assertEquals(arg2,scenarios_crud.MongoCreateRead.insertAndReadCustomer(arg0,arg1));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg6 = mongoDatabase.getCollection("custs", Document.class);
		Document arg7 = new Document();
		arg7.put("_id", "Turing123");
		arg7.put("income", 0.0);
		arg7.put("status", 2);
		assertEquals(arg7, arg6.find(eq(arg7.get("_id"))).first());
		assertEquals(1,arg6.countDocuments());
	}
	@Test public void test_insertAndReadCustomer_0_3() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "DummyString#1");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 0.0);
		arg2.put("status", 0);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", "DummyString#2");
		Document arg4 = new Document();
		arg4.put("_id", "DummyString#3");
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 0.0);
		arg4.put("status", 0);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("_id", "DummyString#4");
		Document arg6 = new Document();
		arg6.put("_id", "DummyString#5");
		arg6.put("enlistedBy", arg5);
		arg6.put("income", 0.0);
		arg6.put("status", 0);
		arg0.insertOne(arg6);
		double arg7 = (double) 0.0;
		// Generate assertEquals for method call
		Customer arg8 = new Customer();
		arg8.setId("Turing123");
		arg8.setIncome((double) 0.0);
		Integer arg11 = new Integer(2);
		arg8.setStatus(arg11);
		arg8.setEnlistedBy(null);
		assertEquals(arg8,scenarios_crud.MongoCreateRead.insertAndReadCustomer(arg0,arg7));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg12 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg12.find(eq(arg2.get("_id"))).first());
		assertEquals(arg4, arg12.find(eq(arg4.get("_id"))).first());
		assertEquals(arg6, arg12.find(eq(arg6.get("_id"))).first());
		Document arg13 = new Document();
		arg13.put("_id", "Turing123");
		arg13.put("income", 0.0);
		arg13.put("status", 2);
		assertEquals(arg13, arg12.find(eq(arg13.get("_id"))).first());
		assertEquals(4,arg12.countDocuments());
	}
}