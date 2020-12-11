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
import scenarios_crud.MongoCreateReadDelete;
import scenarios_examples_data.Order;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoCreateReadDelete {
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
	@Test public void test_insertOrdersThenDelete_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		MongoCreateReadDelete arg1 = new MongoCreateReadDelete();
		// Generate assertEquals for method call
		assertEquals(0,arg1.insertOrdersThenDelete(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg2 = mongoDatabase.getCollection("orders", Document.class);
		ObjectId arg3 = new ObjectId("000000000000000000000000");
		Document arg4 = new Document();
		arg4.put("_id", "Unique123");
		arg4.put("income", 2000.0);
		arg4.put("status", -1);
		Document arg5 = new Document();
		arg5.put("_id", arg3);
		arg5.put("customer", arg4);
		ObjectId arg6 = new ObjectId("000000000000000000000001");
		Document arg7 = new Document();
		arg7.put("_id", "Unique234");
		arg7.put("income", 5000.0);
		arg7.put("status", -1);
		Document arg8 = new Document();
		arg8.put("_id", arg6);
		arg8.put("customer", arg7);
		ObjectId arg9 = new ObjectId("000000000000000000000002");
		Document arg10 = new Document();
		arg10.put("_id", "Unique345");
		arg10.put("income", 5000.0);
		arg10.put("status", -1);
		Document arg11 = new Document();
		arg11.put("name", "P123");
		arg11.put("price", 200);
		Document arg12 = new Document();
		arg12.put("_id", arg9);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		assertEquals(0,arg2.countDocuments());
	}
	@Test public void test_insertOrdersThenDelete_0_232() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		ObjectId arg1 = new ObjectId("000000000000000000000001");
		Document arg2 = new Document();
		arg2.put("income", 0.0);
		arg2.put("status", 0);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		ObjectId arg5 = new ObjectId("000000000000000000000002");
		Document arg6 = new Document();
		arg6.put("income", 0.0);
		arg6.put("status", 0);
		Document arg7 = new Document();
		arg7.put("price", 0);
		Document arg8 = new Document();
		arg8.put("_id", arg5);
		arg8.put("customer", arg6);
		arg8.put("product", arg7);
		arg0.insertOne(arg8);
		ObjectId arg9 = new ObjectId("000000000000000000000003");
		Document arg10 = new Document();
		arg10.put("income", 0.0);
		arg10.put("status", 0);
		Document arg11 = new Document();
		arg11.put("price", 0);
		Document arg12 = new Document();
		arg12.put("_id", arg9);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		arg0.insertOne(arg12);
		MongoCreateReadDelete arg13 = new MongoCreateReadDelete();
		// Generate assertEquals for method call
		assertEquals(0,arg13.insertOrdersThenDelete(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg14 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(arg4, arg14.find(eq(arg4.get("_id"))).first());
		assertEquals(arg8, arg14.find(eq(arg8.get("_id"))).first());
		assertEquals(arg12, arg14.find(eq(arg12.get("_id"))).first());
		ObjectId arg15 = new ObjectId("000000000000000000000000");
		Document arg16 = new Document();
		arg16.put("_id", "Unique123");
		arg16.put("income", 2000.0);
		arg16.put("status", -1);
		Document arg17 = new Document();
		arg17.put("_id", arg15);
		arg17.put("customer", arg16);
		ObjectId arg18 = new ObjectId("000000000000000000000004");
		Document arg19 = new Document();
		arg19.put("_id", "Unique234");
		arg19.put("income", 5000.0);
		arg19.put("status", -1);
		Document arg20 = new Document();
		arg20.put("_id", arg18);
		arg20.put("customer", arg19);
		ObjectId arg21 = new ObjectId("000000000000000000000005");
		Document arg22 = new Document();
		arg22.put("_id", "Unique345");
		arg22.put("income", 5000.0);
		arg22.put("status", -1);
		Document arg23 = new Document();
		arg23.put("name", "P123");
		arg23.put("price", 200);
		Document arg24 = new Document();
		arg24.put("_id", arg21);
		arg24.put("customer", arg22);
		arg24.put("product", arg23);
		assertEquals(3,arg14.countDocuments());
	}
}