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
public class IntegrationTestUserService_updateUser {
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
	@Test public void test_updateUser_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("user",User.class);
		User arg1 = new User();
		arg1.setId((int) 0);
		Address arg3 = new Address();
		arg3.setAddressStreet("street");
		arg3.setAddressCity("city");
		arg1.setAddress(arg3);
		LinkedHashMap arg6 = new LinkedHashMap();
		String arg7 = "profileFirstName";
		String arg8 = "firstName";
		arg6.put(arg7,arg8);
		String arg9 = "profileLastName";
		String arg10 = "lastName";
		arg6.put(arg9,arg10);
		String arg11 = "description";
		String arg12 = "userDescription";
		arg6.put(arg11,arg12);
		arg1.setProfileInfos(arg6);
		arg1.setInterestedInCategory((int) 0);
		arg1.setUserName("newUserName");
		UserService arg15 = new UserService();
		arg15.setUserCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg15.updateUser(arg1);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg16 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(0,arg16.countDocuments());
	}
	@Test public void test_updateUser_0_55() {
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
		arg3.put("_id", -2);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 0);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "newUserName");
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
		arg6.put("_id", -1);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 0);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "newUserName");
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
		arg9.put("_id", 0);
		arg9.put("address", arg7);
		arg9.put("interestedInCategory", 0);
		arg9.put("profileInfos", arg8);
		arg9.put("userName", "newUserName");
		arg0.insertOne(arg9);
		User arg10 = new User();
		arg10.setId((int) 0);
		Address arg12 = new Address();
		arg12.setAddressStreet("street");
		arg12.setAddressCity("city");
		arg10.setAddress(arg12);
		LinkedHashMap arg15 = new LinkedHashMap();
		String arg16 = "profileFirstName";
		String arg17 = "firstName";
		arg15.put(arg16,arg17);
		String arg18 = "profileLastName";
		String arg19 = "lastName";
		arg15.put(arg18,arg19);
		String arg20 = "description";
		String arg21 = "userDescription";
		arg15.put(arg20,arg21);
		arg10.setProfileInfos(arg15);
		arg10.setInterestedInCategory((int) 0);
		arg10.setUserName("newUserName");
		UserService arg24 = new UserService();
		arg24.setUserCollection(arg0);
		// Generate assertEquals for method call
		arg24.updateUser(arg10);
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg25 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg25.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg25.find(eq(arg6.get("_id"))).first());
		Document arg26 = new Document();
		arg26.put("addressCity", "DummyString#84");
		arg26.put("addressStreet", "DummyString#85");
		Document arg27 = new Document();
		arg27.put("profileFirstName", "firstName");
		arg27.put("profileLastName", "lastName");
		arg27.put("description", "userDescription");
		Document arg28 = new Document();
		arg28.put("_id", 0);
		arg28.put("address", arg26);
		arg28.put("interestedInCategory", 0);
		arg28.put("profileInfos", arg27);
		arg28.put("userName", "newUserName");
		assertEquals(arg28, arg25.find(eq(arg28.get("_id"))).first());
		assertEquals(3,arg25.countDocuments());
	}
	@Test public void test_updateUser_0_56() {
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
		arg3.put("_id", -3);
		arg3.put("address", arg1);
		arg3.put("interestedInCategory", 0);
		arg3.put("profileInfos", arg2);
		arg3.put("userName", "newUserName");
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
		arg6.put("_id", -2);
		arg6.put("address", arg4);
		arg6.put("interestedInCategory", 0);
		arg6.put("profileInfos", arg5);
		arg6.put("userName", "newUserName");
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
		arg9.put("userName", "newUserName");
		arg0.insertOne(arg9);
		User arg10 = new User();
		arg10.setId((int) 0);
		Address arg12 = new Address();
		arg12.setAddressStreet("street");
		arg12.setAddressCity("city");
		arg10.setAddress(arg12);
		LinkedHashMap arg15 = new LinkedHashMap();
		String arg16 = "profileFirstName";
		String arg17 = "firstName";
		arg15.put(arg16,arg17);
		String arg18 = "profileLastName";
		String arg19 = "lastName";
		arg15.put(arg18,arg19);
		String arg20 = "description";
		String arg21 = "userDescription";
		arg15.put(arg20,arg21);
		arg10.setProfileInfos(arg15);
		arg10.setInterestedInCategory((int) 0);
		arg10.setUserName("newUserName");
		UserService arg24 = new UserService();
		arg24.setUserCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg24.updateUser(arg10);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg25 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg3, arg25.find(eq(arg3.get("_id"))).first());
		assertEquals(arg6, arg25.find(eq(arg6.get("_id"))).first());
		assertEquals(arg9, arg25.find(eq(arg9.get("_id"))).first());
		assertEquals(3,arg25.countDocuments());
	}
}