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
import scenarios_examples_data.Order;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoCreateReadUpdate_1 {
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
	@Test public void test_updateOrders_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		// Generate assertEquals for method call
		assertEquals(0.0,scenarios_crud.MongoCreateReadUpdate_1.updateOrders(arg0),10e-6);
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_updateOrders_0_32() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("income", 0.0);
		arg2.put("status", -3);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		ObjectId arg5 = new ObjectId("000000000000000000000001");
		Document arg6 = new Document();
		arg6.put("income", 0.0);
		arg6.put("status", -3);
		Document arg7 = new Document();
		arg7.put("price", 0);
		Document arg8 = new Document();
		arg8.put("_id", arg5);
		arg8.put("customer", arg6);
		arg8.put("product", arg7);
		arg0.insertOne(arg8);
		ObjectId arg9 = new ObjectId("000000000000000000000002");
		Document arg10 = new Document();
		arg10.put("income", 0.0);
		arg10.put("status", -3);
		Document arg11 = new Document();
		arg11.put("price", 0);
		Document arg12 = new Document();
		arg12.put("_id", arg9);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		arg0.insertOne(arg12);
		// Generate assertEquals for method call
		assertEquals(0.0,scenarios_crud.MongoCreateReadUpdate_1.updateOrders(arg0),10e-6);
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("orders", Document.class);
		ObjectId arg14 = new ObjectId("000000000000000000000000");
		Document arg15 = new Document();
		arg15.put("income", 12.9);
		arg15.put("status", -3);
		Document arg16 = new Document();
		arg16.put("price", 0);
		Document arg17 = new Document();
		arg17.put("_id", arg14);
		arg17.put("customer", arg15);
		arg17.put("product", arg16);
		assertEquals(arg17, arg13.find(eq(arg17.get("_id"))).first());
		ObjectId arg18 = new ObjectId("000000000000000000000001");
		Document arg19 = new Document();
		arg19.put("income", 12.9);
		arg19.put("status", -3);
		Document arg20 = new Document();
		arg20.put("price", 0);
		Document arg21 = new Document();
		arg21.put("_id", arg18);
		arg21.put("customer", arg19);
		arg21.put("product", arg20);
		assertEquals(arg21, arg13.find(eq(arg21.get("_id"))).first());
		ObjectId arg22 = new ObjectId("000000000000000000000002");
		Document arg23 = new Document();
		arg23.put("income", 12.9);
		arg23.put("status", -3);
		Document arg24 = new Document();
		arg24.put("price", 0);
		Document arg25 = new Document();
		arg25.put("_id", arg22);
		arg25.put("customer", arg23);
		arg25.put("product", arg24);
		assertEquals(arg25, arg13.find(eq(arg25.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
	@Test public void test_updateOrders_0_39() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("income", 0.0);
		arg2.put("status", -4);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		ObjectId arg5 = new ObjectId("000000000000000000000001");
		Document arg6 = new Document();
		arg6.put("income", 0.0);
		arg6.put("status", -4);
		Document arg7 = new Document();
		arg7.put("price", 0);
		Document arg8 = new Document();
		arg8.put("_id", arg5);
		arg8.put("customer", arg6);
		arg8.put("product", arg7);
		arg0.insertOne(arg8);
		ObjectId arg9 = new ObjectId("000000000000000000000002");
		Document arg10 = new Document();
		arg10.put("income", 0.0);
		arg10.put("status", -4);
		Document arg11 = new Document();
		arg11.put("price", 0);
		Document arg12 = new Document();
		arg12.put("_id", arg9);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		arg0.insertOne(arg12);
		// Generate assertEquals for method call
		assertEquals(0.0,scenarios_crud.MongoCreateReadUpdate_1.updateOrders(arg0),10e-6);
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("orders", Document.class);
		ObjectId arg14 = new ObjectId("000000000000000000000000");
		Document arg15 = new Document();
		arg15.put("income", 13.9);
		arg15.put("status", -4);
		Document arg16 = new Document();
		arg16.put("price", 0);
		Document arg17 = new Document();
		arg17.put("_id", arg14);
		arg17.put("customer", arg15);
		arg17.put("product", arg16);
		assertEquals(arg17, arg13.find(eq(arg17.get("_id"))).first());
		ObjectId arg18 = new ObjectId("000000000000000000000001");
		Document arg19 = new Document();
		arg19.put("income", 13.9);
		arg19.put("status", -4);
		Document arg20 = new Document();
		arg20.put("price", 0);
		Document arg21 = new Document();
		arg21.put("_id", arg18);
		arg21.put("customer", arg19);
		arg21.put("product", arg20);
		assertEquals(arg21, arg13.find(eq(arg21.get("_id"))).first());
		ObjectId arg22 = new ObjectId("000000000000000000000002");
		Document arg23 = new Document();
		arg23.put("income", 13.9);
		arg23.put("status", -4);
		Document arg24 = new Document();
		arg24.put("price", 0);
		Document arg25 = new Document();
		arg25.put("_id", arg22);
		arg25.put("customer", arg23);
		arg25.put("product", arg24);
		assertEquals(arg25, arg13.find(eq(arg25.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
}