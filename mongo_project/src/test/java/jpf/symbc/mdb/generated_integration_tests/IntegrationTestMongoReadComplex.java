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
import scenarios_crud.MongoReadComplex;
import scenarios_examples_data.Order;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoReadComplex {
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
	@Test public void test_readOrdersWithGTFilter_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		MongoReadComplex arg1 = new MongoReadComplex();
		// Generate assertEquals for method call
		assertEquals(0,arg1.readOrdersWithGTFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg2 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(0,arg2.countDocuments());
	}
	@Test public void test_readOrdersWithGTFilter_0_146() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("income", 23.1);
		arg2.put("status", 3);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		MongoReadComplex arg5 = new MongoReadComplex();
		// Generate assertEquals for method call
		assertEquals(1,arg5.readOrdersWithGTFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg6 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(arg4, arg6.find(eq(arg4.get("_id"))).first());
		assertEquals(1,arg6.countDocuments());
	}
	@Test public void test_readOrdersWithGTFilter_0_147() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Order.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("income", 23.1);
		arg2.put("status", 0);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		MongoReadComplex arg5 = new MongoReadComplex();
		// Generate assertEquals for method call
		assertEquals(2,arg5.readOrdersWithGTFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg6 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(arg4, arg6.find(eq(arg4.get("_id"))).first());
		assertEquals(1,arg6.countDocuments());
	}
}