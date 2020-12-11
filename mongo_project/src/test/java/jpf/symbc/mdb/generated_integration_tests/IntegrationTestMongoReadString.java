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
public class IntegrationTestMongoReadString {
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
	@Test public void test_searchCustomerWithId_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		String arg1 = "SomeRandomId";
		// Generate assertEquals for method call
		assertEquals(null,scenarios_crud.MongoReadString.searchCustomerWithId(arg0,arg1));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg2 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(0,arg2.countDocuments());
	}
	@Test public void test_searchCustomerWithId_0_2() {
		MongoCollection arg0 = mongoDatabase.getCollection("custs",Customer.class);
		Document arg1 = new Document();
		arg1.put("_id", "DummyString#0");
		Document arg2 = new Document();
		arg2.put("_id", "SomeRandomId");
		arg2.put("enlistedBy", arg1);
		arg2.put("income", 0.0);
		arg2.put("status", 0);
		arg0.insertOne(arg2);
		String arg3 = "SomeRandomId";
		// Generate assertEquals for method call
		Customer arg4 = new Customer();
		arg4.setId("SomeRandomId");
		arg4.setIncome((double) 0.0);
		Integer arg7 = new Integer(0);
		arg4.setStatus(arg7);
		Customer arg8 = new Customer();
		arg8.setId("DummyString#0");
		arg8.setIncome((double) 0.0);
		arg8.setStatus(null);
		arg8.setEnlistedBy(null);
		arg4.setEnlistedBy(arg8);
		assertEquals(arg4,scenarios_crud.MongoReadString.searchCustomerWithId(arg0,arg3));
		// Generate assertion statements to check content of 'custs':
		MongoCollection<Document> arg11 = mongoDatabase.getCollection("custs", Document.class);
		assertEquals(arg2, arg11.find(eq(arg2.get("_id"))).first());
		assertEquals(1,arg11.countDocuments());
	}
}