package jpf.symbc.mdb.social_media_integration_tests;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.junit.*;
import social_media_app.entity.Address;
import social_media_app.entity.User;
import social_media_app.service.UserService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestUserService_getUserIfNonBusiness {
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
	@Test public void test_getUserIfNonBusiness_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		int arg1 = (int) 0;
		UserService arg2 = new UserService();
		arg2.setUserCollection(arg0);
		// Generate assertEquals for method call
		assertEquals(null,arg2.getUserIfNonBusiness(arg1));
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg3 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(0,arg3.countDocuments());
	}
	@Test public void test_getUserIfNonBusiness_0_38() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#58");
		arg1.put("addressStreet", "DummyString#59");
		Document arg2 = new Document();
		arg2.put("profileFirstName", "DummyString#60");
		arg2.put("profileLastName", "DummyString#61");
		arg2.put("representedBusinessName", "DummyString#62");
		arg2.put("representedBusinessPosition", "DummyString#63");
		Document arg3 = new Document();
		arg3.put("_id", 0);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 0);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "DummyString#64");
		arg0.insertOne(arg3);
		Document arg4 = new Document();
		arg4.put("addressCity", "DummyString#123");
		arg4.put("addressStreet", "DummyString#124");
		Document arg5 = new Document();
		arg5.put("profileFirstName", "DummyString#125");
		arg5.put("profileLastName", "DummyString#126");
		arg5.put("representedBusinessName", "DummyString#127");
		arg5.put("representedBusinessPosition", "DummyString#128");
		Document arg6 = new Document();
		arg6.put("_id", 1);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 0);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "DummyString#129");
		arg0.insertOne(arg6);
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#149");
		arg7.put("addressStreet", "DummyString#150");
		Document arg8 = new Document();
		arg8.put("profileFirstName", "DummyString#151");
		arg8.put("profileLastName", "DummyString#152");
		arg8.put("representedBusinessName", "DummyString#153");
		arg8.put("representedBusinessPosition", "DummyString#154");
		Document arg9 = new Document();
		arg9.put("_id", 2);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "DummyString#155");
		arg0.insertOne(arg9);
		int arg10 = (int) -1;
		UserService arg11 = new UserService();
		arg11.setUserCollection(arg0);
		// Generate assertEquals for method call
		assertEquals(null,arg11.getUserIfNonBusiness(arg10));
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg12 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg12.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg12.find(eq(arg6.get("_id"))).first());
		assertEquals(arg9, arg12.find(eq(arg9.get("_id"))).first());
		assertEquals(3,arg12.countDocuments());
	}
}