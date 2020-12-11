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
public class IntegrationTestMongoReadComplex_1 {
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
	@Test public void test_readDocsWithLTAndGTFilter_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Document.class);
		// Generate assertEquals for method call
		assertEquals(2,scenarios_crud.MongoReadComplex_1.readDocsWithLTAndGTFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_readDocsWithLTAndGTFilter_0_4835() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Document.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("income", 25.2);
		arg2.put("status", 12);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		ObjectId arg5 = new ObjectId("000000000000000000000001");
		Document arg6 = new Document();
		arg6.put("income", 25.2);
		arg6.put("status", 12);
		Document arg7 = new Document();
		arg7.put("price", 0);
		Document arg8 = new Document();
		arg8.put("_id", arg5);
		arg8.put("customer", arg6);
		arg8.put("product", arg7);
		arg0.insertOne(arg8);
		ObjectId arg9 = new ObjectId("000000000000000000000002");
		Document arg10 = new Document();
		arg10.put("income", 23.1);
		arg10.put("status", 12);
		Document arg11 = new Document();
		arg11.put("price", 0);
		Document arg12 = new Document();
		arg12.put("_id", arg9);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		arg0.insertOne(arg12);
		// Generate assertEquals for method call
		assertEquals(0,scenarios_crud.MongoReadComplex_1.readDocsWithLTAndGTFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(arg4, arg13.find(eq(arg4.get("_id"))).first());
		assertEquals(arg8, arg13.find(eq(arg8.get("_id"))).first());
		assertEquals(arg12, arg13.find(eq(arg12.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
	@Test public void test_readDocsWithLTAndGTFilter_0_4848() {
		MongoCollection arg0 = mongoDatabase.getCollection("orders",Document.class);
		ObjectId arg1 = new ObjectId("000000000000000000000000");
		Document arg2 = new Document();
		arg2.put("income", 25.2);
		arg2.put("status", 12);
		Document arg3 = new Document();
		arg3.put("price", 0);
		Document arg4 = new Document();
		arg4.put("_id", arg1);
		arg4.put("customer", arg2);
		arg4.put("product", arg3);
		arg0.insertOne(arg4);
		ObjectId arg5 = new ObjectId("000000000000000000000001");
		Document arg6 = new Document();
		arg6.put("income", 25.2);
		arg6.put("status", 12);
		Document arg7 = new Document();
		arg7.put("price", 0);
		Document arg8 = new Document();
		arg8.put("_id", arg5);
		arg8.put("customer", arg6);
		arg8.put("product", arg7);
		arg0.insertOne(arg8);
		ObjectId arg9 = new ObjectId("000000000000000000000002");
		Document arg10 = new Document();
		arg10.put("income", 25.2);
		arg10.put("status", 12);
		Document arg11 = new Document();
		arg11.put("price", 203);
		Document arg12 = new Document();
		arg12.put("_id", arg9);
		arg12.put("customer", arg10);
		arg12.put("product", arg11);
		arg0.insertOne(arg12);
		// Generate assertEquals for method call
		assertEquals(1,scenarios_crud.MongoReadComplex_1.readDocsWithLTAndGTFilter(arg0));
		// Generate assertion statements to check content of 'orders':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("orders", Document.class);
		assertEquals(arg4, arg13.find(eq(arg4.get("_id"))).first());
		assertEquals(arg8, arg13.find(eq(arg8.get("_id"))).first());
		assertEquals(arg12, arg13.find(eq(arg12.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
}