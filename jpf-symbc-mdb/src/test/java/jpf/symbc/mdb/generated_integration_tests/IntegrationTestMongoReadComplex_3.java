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
import scenarios_examples_data.Order;
import scenarios_examples_data.Product;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoReadComplex_3 {
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
	@Test public void test_readOrdersWithEQFilter_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		// Generate assertEquals for method call
		assertEquals(null,scenarios_crud.MongoReadComplex_3.readOrdersWithEQFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_readOrdersWithEQFilter_0_102() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("status", 0);
		Document arg3 = new Document();
		arg3.put("enlistedBy", arg2);
		arg3.put("status", 0);
		Document arg4 = new Document();
		arg4.put("enlistedBy", arg3);
		arg4.put("income", 0.0);
		arg4.put("status", 0);
		Document arg5 = new Document();
		arg5.put("price", 0);
		Document arg6 = new Document();
		arg6.put("_id", arg1);
		arg6.put("customer", arg4);
		arg6.put("product", arg5);
		arg0.insertOne(arg6);
		ObjectId arg7 = new ObjectId("000000000000000000000001");
		Document arg8 = new Document();
		arg8.put("status", 0);
		Document arg9 = new Document();
		arg9.put("enlistedBy", arg8);
		arg9.put("status", 0);
		Document arg10 = new Document();
		arg10.put("enlistedBy", arg9);
		arg10.put("income", 0.0);
		arg10.put("status", 0);
		Document arg11 = new Document();
		arg11.put("price", 0);
		Document arg12 = new Document();
		arg12.put("_id", arg7);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		arg0.insertOne(arg12);
		ObjectId arg13 = new ObjectId("000000000000000000000002");
		Document arg14 = new Document();
		arg14.put("status", 14);
		Document arg15 = new Document();
		arg15.put("enlistedBy", arg14);
		arg15.put("status", 13);
		Document arg16 = new Document();
		arg16.put("enlistedBy", arg15);
		arg16.put("income", 0.0);
		arg16.put("status", 12);
		Document arg17 = new Document();
		arg17.put("price", 0);
		Document arg18 = new Document();
		arg18.put("_id", arg13);
		arg18.put("customer", arg16);
		arg18.put("product", arg17);
		arg0.insertOne(arg18);
		// Generate assertEquals for method call
		Order arg19 = new Order();
		Customer arg20 = new Customer();
		arg20.setId(null);
		arg20.setIncome((double) 0.0);
		Integer arg22 = new Integer(12);
		arg20.setStatus(arg22);
		Customer arg23 = new Customer();
		arg23.setId(null);
		arg23.setIncome((double) 0.0);
		Integer arg25 = new Integer(13);
		arg23.setStatus(arg25);
		Customer arg26 = new Customer();
		arg26.setId(null);
		arg26.setIncome((double) 0.0);
		Integer arg28 = new Integer(14);
		arg26.setStatus(arg28);
		arg26.setEnlistedBy(null);
		arg23.setEnlistedBy(arg26);
		arg20.setEnlistedBy(arg23);
		arg19.setCustomer(arg20);
		Product arg29 = new Product();
		arg29.setName(null);
		arg29.setPrice((int) 0);
		arg19.setProduct(arg29);
		assertEquals(arg19,scenarios_crud.MongoReadComplex_3.readOrdersWithEQFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg31 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(arg6, arg31.find(eq(arg6.get("_id"))).first());
		assertEquals(arg12, arg31.find(eq(arg12.get("_id"))).first());
		assertEquals(arg18, arg31.find(eq(arg18.get("_id"))).first());
		assertEquals(3,arg31.countDocuments());
	}
}