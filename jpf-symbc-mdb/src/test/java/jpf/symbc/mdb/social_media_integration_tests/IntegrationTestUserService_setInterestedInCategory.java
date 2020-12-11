package jpf.symbc.mdb.social_media_integration_tests;

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
import social_media_app.entity.User;
import social_media_app.service.UserService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestUserService_setInterestedInCategory {
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
	@Test public void test_setInterestedInCategory_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		int arg1 = (int) 42;
		int arg2 = (int) 0;
		UserService arg3 = new UserService();
		arg3.setUserCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg3.setInterestedInCategory(arg1,arg2);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg4 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(0,arg4.countDocuments());
	}
	@Test public void test_setInterestedInCategory_0_60() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#110");
		arg1.put("addressStreet", "DummyString#111");
		Document arg2 = new Document();
		arg2.put("profileFirstName", "DummyString#112");
		arg2.put("profileLastName", "DummyString#113");
		arg2.put("representedBusinessName", "DummyString#114");
		arg2.put("representedBusinessPosition", "DummyString#115");
		Document arg3 = new Document();
		arg3.put("_id", 0);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 1);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "DummyString#116");
		arg0.insertOne(arg3);
		Document arg4 = new Document();
		arg4.put("addressCity", "DummyString#188");
		arg4.put("addressStreet", "DummyString#189");
		Document arg5 = new Document();
		arg5.put("profileFirstName", "DummyString#190");
		arg5.put("profileLastName", "DummyString#191");
		arg5.put("representedBusinessName", "DummyString#192");
		arg5.put("representedBusinessPosition", "DummyString#193");
		Document arg6 = new Document();
		arg6.put("_id", 1);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 1);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "DummyString#194");
		arg0.insertOne(arg6);
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#214");
		arg7.put("addressStreet", "DummyString#215");
		Document arg8 = new Document();
		arg8.put("profileFirstName", "DummyString#216");
		arg8.put("profileLastName", "DummyString#217");
		arg8.put("representedBusinessName", "DummyString#218");
		arg8.put("representedBusinessPosition", "DummyString#219");
		Document arg9 = new Document();
		arg9.put("_id", -1);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "DummyString#220");
		arg0.insertOne(arg9);
		int arg10 = (int) -1;
		int arg11 = (int) 0;
		UserService arg12 = new UserService();
		arg12.setUserCollection(arg0);
		// Generate assertEquals for method call
		arg12.setInterestedInCategory(arg10,arg11);
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg13.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg13.find(eq(arg6.get("_id"))).first());
		Document arg14 = new Document();
		arg14.put("addressCity", "DummyString#214");
		arg14.put("addressStreet", "DummyString#215");
		Document arg15 = new Document();
		arg15.put("profileFirstName", "DummyString#216");
		arg15.put("profileLastName", "DummyString#217");
		arg15.put("representedBusinessName", "DummyString#218");
		arg15.put("representedBusinessPosition", "DummyString#219");
		Document arg16 = new Document();
		arg16.put("_id", -1);
		arg16.put("address", arg14);
		arg16.put("interestedInCategory", 0);
		arg16.put("profileInfos", arg15);
		arg16.put("userName", "DummyString#220");
		assertEquals(arg16, arg13.find(eq(arg16.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
	@Test public void test_setInterestedInCategory_0_61() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#110");
		arg1.put("addressStreet", "DummyString#111");
		Document arg2 = new Document();
		arg2.put("profileFirstName", "DummyString#112");
		arg2.put("profileLastName", "DummyString#113");
		arg2.put("representedBusinessName", "DummyString#114");
		arg2.put("representedBusinessPosition", "DummyString#115");
		Document arg3 = new Document();
		arg3.put("_id", 0);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 1);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "DummyString#116");
		arg0.insertOne(arg3);
		Document arg4 = new Document();
		arg4.put("addressCity", "DummyString#188");
		arg4.put("addressStreet", "DummyString#189");
		Document arg5 = new Document();
		arg5.put("profileFirstName", "DummyString#190");
		arg5.put("profileLastName", "DummyString#191");
		arg5.put("representedBusinessName", "DummyString#192");
		arg5.put("representedBusinessPosition", "DummyString#193");
		Document arg6 = new Document();
		arg6.put("_id", 1);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 1);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "DummyString#194");
		arg0.insertOne(arg6);
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#214");
		arg7.put("addressStreet", "DummyString#215");
		Document arg8 = new Document();
		arg8.put("profileFirstName", "DummyString#216");
		arg8.put("profileLastName", "DummyString#217");
		arg8.put("representedBusinessName", "DummyString#218");
		arg8.put("representedBusinessPosition", "DummyString#219");
		Document arg9 = new Document();
		arg9.put("_id", -1);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "DummyString#220");
		arg0.insertOne(arg9);
		int arg10 = (int) -1;
		int arg11 = (int) -1;
		UserService arg12 = new UserService();
		arg12.setUserCollection(arg0);
		// Generate assertEquals for method call
		arg12.setInterestedInCategory(arg10,arg11);
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg13.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg13.find(eq(arg6.get("_id"))).first());
		Document arg14 = new Document();
		arg14.put("addressCity", "DummyString#214");
		arg14.put("addressStreet", "DummyString#215");
		Document arg15 = new Document();
		arg15.put("profileFirstName", "DummyString#216");
		arg15.put("profileLastName", "DummyString#217");
		arg15.put("representedBusinessName", "DummyString#218");
		arg15.put("representedBusinessPosition", "DummyString#219");
		Document arg16 = new Document();
		arg16.put("_id", -1);
		arg16.put("address", arg14);
		arg16.put("interestedInCategory", -1);
		arg16.put("profileInfos", arg15);
		arg16.put("userName", "DummyString#220");
		assertEquals(arg16, arg13.find(eq(arg16.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
	@Test public void test_setInterestedInCategory_0_62() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#110");
		arg1.put("addressStreet", "DummyString#111");
		Document arg2 = new Document();
		arg2.put("profileFirstName", "DummyString#112");
		arg2.put("profileLastName", "DummyString#113");
		arg2.put("representedBusinessName", "DummyString#114");
		arg2.put("representedBusinessPosition", "DummyString#115");
		Document arg3 = new Document();
		arg3.put("_id", 0);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 1);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "DummyString#116");
		arg0.insertOne(arg3);
		Document arg4 = new Document();
		arg4.put("addressCity", "DummyString#188");
		arg4.put("addressStreet", "DummyString#189");
		Document arg5 = new Document();
		arg5.put("profileFirstName", "DummyString#190");
		arg5.put("profileLastName", "DummyString#191");
		arg5.put("representedBusinessName", "DummyString#192");
		arg5.put("representedBusinessPosition", "DummyString#193");
		Document arg6 = new Document();
		arg6.put("_id", 1);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 1);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "DummyString#194");
		arg0.insertOne(arg6);
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#214");
		arg7.put("addressStreet", "DummyString#215");
		Document arg8 = new Document();
		arg8.put("profileFirstName", "DummyString#216");
		arg8.put("profileLastName", "DummyString#217");
		arg8.put("representedBusinessName", "DummyString#218");
		arg8.put("representedBusinessPosition", "DummyString#219");
		Document arg9 = new Document();
		arg9.put("_id", 2);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "DummyString#220");
		arg0.insertOne(arg9);
		int arg10 = (int) -1;
		int arg11 = (int) -1;
		UserService arg12 = new UserService();
		arg12.setUserCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg12.setInterestedInCategory(arg10,arg11);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg13.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg13.find(eq(arg6.get("_id"))).first());
		assertEquals(arg9, arg13.find(eq(arg9.get("_id"))).first());
		assertEquals(3,arg13.countDocuments());
	}
}