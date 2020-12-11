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
public class IntegrationTestUserService_getUser {
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
	@Test public void test_getUser_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		int arg1 = (int) 0;
		UserService arg2 = new UserService();
		arg2.setUserCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg2.getUser(arg1);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg3 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(0,arg3.countDocuments());
	}
	@Test public void test_getUser_0_27() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#45");
		arg1.put("addressStreet", "DummyString#46");
		Document arg2 = new Document();
		arg2.put("profileFirstName", "DummyString#47");
		arg2.put("profileLastName", "DummyString#48");
		arg2.put("representedBusinessName", "DummyString#49");
		arg2.put("representedBusinessPosition", "DummyString#50");
		Document arg3 = new Document();
		arg3.put("_id", 0);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 0);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "DummyString#51");
		arg0.insertOne(arg3);
		Document arg4 = new Document();
		arg4.put("addressCity", "DummyString#71");
		arg4.put("addressStreet", "DummyString#72");
		Document arg5 = new Document();
		arg5.put("profileFirstName", "DummyString#73");
		arg5.put("profileLastName", "DummyString#74");
		arg5.put("representedBusinessName", "DummyString#75");
		arg5.put("representedBusinessPosition", "DummyString#76");
		Document arg6 = new Document();
		arg6.put("_id", 2);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 0);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "DummyString#77");
		arg0.insertOne(arg6);
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#84");
		arg7.put("addressStreet", "DummyString#85");
		Document arg8 = new Document();
		arg8.put("profileFirstName", "DummyString#86");
		arg8.put("profileLastName", "DummyString#87");
		arg8.put("representedBusinessName", "DummyString#88");
		arg8.put("representedBusinessPosition", "DummyString#89");
		Document arg9 = new Document();
		arg9.put("_id", -1);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "DummyString#90");
		arg0.insertOne(arg9);
		int arg10 = (int) -1;
		UserService arg11 = new UserService();
		arg11.setUserCollection(arg0);
		// Generate assertEquals for method call
		User arg12 = new User();
		arg12.setId((int) -1);
		Address arg14 = new Address();
		arg14.setAddressStreet("DummyString#85");
		arg14.setAddressCity("DummyString#84");
		arg12.setAddress(arg14);
		LinkedHashMap arg17 = new LinkedHashMap();
		String arg18 = "profileFirstName";
		String arg19 = "DummyString#86";
		arg17.put(arg18,arg19);
		String arg20 = "profileLastName";
		String arg21 = "DummyString#87";
		arg17.put(arg20,arg21);
		String arg22 = "representedBusinessName";
		String arg23 = "DummyString#88";
		arg17.put(arg22,arg23);
		String arg24 = "representedBusinessPosition";
		String arg25 = "DummyString#89";
		arg17.put(arg24,arg25);
		arg12.setProfileInfos(arg17);
		arg12.setInterestedInCategory((int) 0);
		arg12.setUserName("DummyString#90");
		assertEquals(arg12,arg11.getUser(arg10));
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg28 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg28.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg28.find(eq(arg6.get("_id"))).first());
		assertEquals(arg9, arg28.find(eq(arg9.get("_id"))).first());
		assertEquals(3,arg28.countDocuments());
	}
	@Test public void test_getUser_0_28() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#45");
		arg1.put("addressStreet", "DummyString#46");
		Document arg2 = new Document();
		arg2.put("profileFirstName", "DummyString#47");
		arg2.put("profileLastName", "DummyString#48");
		arg2.put("representedBusinessName", "DummyString#49");
		arg2.put("representedBusinessPosition", "DummyString#50");
		Document arg3 = new Document();
		arg3.put("_id", 0);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 0);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "DummyString#51");
		arg0.insertOne(arg3);
		Document arg4 = new Document();
		arg4.put("addressCity", "DummyString#71");
		arg4.put("addressStreet", "DummyString#72");
		Document arg5 = new Document();
		arg5.put("profileFirstName", "DummyString#73");
		arg5.put("profileLastName", "DummyString#74");
		arg5.put("representedBusinessName", "DummyString#75");
		arg5.put("representedBusinessPosition", "DummyString#76");
		Document arg6 = new Document();
		arg6.put("_id", 2);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 0);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "DummyString#77");
		arg0.insertOne(arg6);
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#84");
		arg7.put("addressStreet", "DummyString#85");
		Document arg8 = new Document();
		arg8.put("profileFirstName", "DummyString#86");
		arg8.put("profileLastName", "DummyString#87");
		arg8.put("representedBusinessName", "DummyString#88");
		arg8.put("representedBusinessPosition", "DummyString#89");
		Document arg9 = new Document();
		arg9.put("_id", -1);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "DummyString#90");
		arg0.insertOne(arg9);
		int arg10 = (int) -2;
		UserService arg11 = new UserService();
		arg11.setUserCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg11.getUser(arg10);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg12 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg12.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg12.find(eq(arg6.get("_id"))).first());
		assertEquals(arg9, arg12.find(eq(arg9.get("_id"))).first());
		assertEquals(3,arg12.countDocuments());
	}
}