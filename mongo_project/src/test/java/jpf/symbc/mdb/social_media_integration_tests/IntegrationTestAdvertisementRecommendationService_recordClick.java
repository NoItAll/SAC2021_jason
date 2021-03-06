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
import social_media_app.entity.ProductAdvertisement;
import social_media_app.entity.User;
import social_media_app.service.AdvertisementRecommendationService;
import social_media_app.service.UserService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestAdvertisementRecommendationService_recordClick {
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
	@Test public void test_recordClick_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		MongoCollection arg1 = mongoDatabase.getCollection("user",User.class);
		MongoCollection arg2 = mongoDatabase.getCollection("advertisement_click",Document.class);
		int arg3 = (int) 0;
		int arg4 = (int) 0;
		int arg5 = (int) 0;
		AdvertisementRecommendationService arg6 = new AdvertisementRecommendationService();
		arg6.setAdvertisementClickCollection(arg2);
		UserService arg7 = new UserService();
		arg7.setUserCollection(arg1);
		arg6.setUserService(arg7);
		arg6.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg6.recordClick(arg3,arg4,arg5);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.UserNotFound e) {}
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg8 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(0,arg8.countDocuments());
	}
	@Test public void test_recordClick_0_91() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		MongoCollection arg1 = mongoDatabase.getCollection("user",User.class);
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#121");
		arg2.put("addressStreet", "DummyString#122");
		Document arg3 = new Document();
		arg3.put("profileFirstName", "DummyString#123");
		arg3.put("profileLastName", "DummyString#124");
		arg3.put("representedBusinessName", "DummyString#125");
		arg3.put("representedBusinessPosition", "DummyString#126");
		Document arg4 = new Document();
		arg4.put("_id", -1);
		arg4.put("address", arg2);
		arg4.put("interestedInCategory", 0);
		arg4.put("profileInfos", arg3);
		arg4.put("userName", "DummyString#127");
		arg1.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("addressCity", "DummyString#182");
		arg5.put("addressStreet", "DummyString#183");
		Document arg6 = new Document();
		arg6.put("description", "DummyString#184");
		arg6.put("profileFirstName", "DummyString#185");
		arg6.put("profileLastName", "DummyString#186");
		Document arg7 = new Document();
		arg7.put("_id", 0);
		arg7.put("address", arg5);
		arg7.put("interestedInCategory", 0);
		arg7.put("profileInfos", arg6);
		arg7.put("userName", "DummyString#187");
		arg1.insertOne(arg7);
		MongoCollection arg8 = mongoDatabase.getCollection("advertisement_click",Document.class);
		int arg9 = (int) 0;
		int arg10 = (int) 0;
		int arg11 = (int) 0;
		AdvertisementRecommendationService arg12 = new AdvertisementRecommendationService();
		arg12.setAdvertisementClickCollection(arg8);
		UserService arg13 = new UserService();
		arg13.setUserCollection(arg1);
		arg12.setUserService(arg13);
		arg12.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg12.recordClick(arg9,arg10,arg11);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.ProductAdvertisementNotFound e) {}
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg14 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg14.countDocuments());
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg15 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg4, arg15.find(eq(arg4.get("_id"))).first());
		assertEquals(arg7, arg15.find(eq(arg7.get("_id"))).first());
		assertEquals(2,arg15.countDocuments());
	}
	@Test public void test_recordClick_0_113() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#215");
		arg1.put("associatedInfluencerName", "DummyString#216");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#217");
		arg2.put("addressStreet", "DummyString#218");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#219");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#220");
		Document arg5 = new Document();
		arg5.put("_id", -1);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementText", "DummyString#229");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#230");
		arg7.put("addressStreet", "DummyString#231");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#232");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#233");
		Document arg10 = new Document();
		arg10.put("_id", 0);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		MongoCollection arg11 = mongoDatabase.getCollection("user",User.class);
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#121");
		arg12.put("addressStreet", "DummyString#122");
		Document arg13 = new Document();
		arg13.put("profileFirstName", "DummyString#123");
		arg13.put("profileLastName", "DummyString#124");
		arg13.put("representedBusinessName", "DummyString#125");
		arg13.put("representedBusinessPosition", "DummyString#126");
		Document arg14 = new Document();
		arg14.put("_id", -1);
		arg14.put("address", arg12);
		arg14.put("interestedInCategory", 0);
		arg14.put("profileInfos", arg13);
		arg14.put("userName", "DummyString#127");
		arg11.insertOne(arg14);
		Document arg15 = new Document();
		arg15.put("addressCity", "DummyString#182");
		arg15.put("addressStreet", "DummyString#183");
		Document arg16 = new Document();
		arg16.put("description", "DummyString#184");
		arg16.put("profileFirstName", "DummyString#185");
		arg16.put("profileLastName", "DummyString#186");
		Document arg17 = new Document();
		arg17.put("_id", 0);
		arg17.put("address", arg15);
		arg17.put("interestedInCategory", 0);
		arg17.put("profileInfos", arg16);
		arg17.put("userName", "DummyString#187");
		arg11.insertOne(arg17);
		MongoCollection arg18 = mongoDatabase.getCollection("advertisement_click",Document.class);
		int arg19 = (int) 0;
		int arg20 = (int) 0;
		int arg21 = (int) 0;
		AdvertisementRecommendationService arg22 = new AdvertisementRecommendationService();
		arg22.setAdvertisementClickCollection(arg18);
		UserService arg23 = new UserService();
		arg23.setUserCollection(arg11);
		arg22.setUserService(arg23);
		arg22.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg22.recordClick(arg19,arg20,arg21);
		// Generate assertion statements to check content of 'advertisement_click':
		MongoCollection<Document> arg24 = mongoDatabase.getCollection("advertisement_click", Document.class);
		Document arg25 = new Document();
		arg25.put("addressCity", "DummyString#230");
		arg25.put("addressStreet", "DummyString#231");
		Document arg26 = new Document();
		arg26.put("address", arg25);
		arg26.put("businessName", "DummyString#232");
		Document arg27 = new Document();
		arg27.put("category", 0);
		arg27.put("ownerBusiness", arg26);
		arg27.put("price", 0);
		arg27.put("productName", "DummyString#233");
		Document arg28 = new Document();
		arg28.put("_id", 0);
		arg28.put("product", arg27);
		arg28.put("adId", 0);
		arg28.put("wasFavoriteCategory", true);
		arg28.put("userId", 0);
		assertEquals(arg28, arg24.find(eq(arg28.get("_id"))).first());
		assertEquals(1,arg24.countDocuments());
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg29 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg29.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg29.find(eq(arg10.get("_id"))).first());
		assertEquals(2,arg29.countDocuments());
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg30 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg14, arg30.find(eq(arg14.get("_id"))).first());
		assertEquals(arg17, arg30.find(eq(arg17.get("_id"))).first());
		assertEquals(2,arg30.countDocuments());
	}
	@Test public void test_recordClick_0_116() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#215");
		arg1.put("associatedInfluencerName", "DummyString#216");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#217");
		arg2.put("addressStreet", "DummyString#218");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#219");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#220");
		Document arg5 = new Document();
		arg5.put("_id", -1);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementText", "DummyString#229");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#230");
		arg7.put("addressStreet", "DummyString#231");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#232");
		Document arg9 = new Document();
		arg9.put("category", 1);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#233");
		Document arg10 = new Document();
		arg10.put("_id", 0);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		MongoCollection arg11 = mongoDatabase.getCollection("user",User.class);
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#121");
		arg12.put("addressStreet", "DummyString#122");
		Document arg13 = new Document();
		arg13.put("profileFirstName", "DummyString#123");
		arg13.put("profileLastName", "DummyString#124");
		arg13.put("representedBusinessName", "DummyString#125");
		arg13.put("representedBusinessPosition", "DummyString#126");
		Document arg14 = new Document();
		arg14.put("_id", -1);
		arg14.put("address", arg12);
		arg14.put("interestedInCategory", 0);
		arg14.put("profileInfos", arg13);
		arg14.put("userName", "DummyString#127");
		arg11.insertOne(arg14);
		Document arg15 = new Document();
		arg15.put("addressCity", "DummyString#182");
		arg15.put("addressStreet", "DummyString#183");
		Document arg16 = new Document();
		arg16.put("description", "DummyString#184");
		arg16.put("profileFirstName", "DummyString#185");
		arg16.put("profileLastName", "DummyString#186");
		Document arg17 = new Document();
		arg17.put("_id", 0);
		arg17.put("address", arg15);
		arg17.put("interestedInCategory", 0);
		arg17.put("profileInfos", arg16);
		arg17.put("userName", "DummyString#187");
		arg11.insertOne(arg17);
		MongoCollection arg18 = mongoDatabase.getCollection("advertisement_click",Document.class);
		int arg19 = (int) 0;
		int arg20 = (int) 0;
		int arg21 = (int) 0;
		AdvertisementRecommendationService arg22 = new AdvertisementRecommendationService();
		arg22.setAdvertisementClickCollection(arg18);
		UserService arg23 = new UserService();
		arg23.setUserCollection(arg11);
		arg22.setUserService(arg23);
		arg22.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg22.recordClick(arg19,arg20,arg21);
		// Generate assertion statements to check content of 'advertisement_click':
		MongoCollection<Document> arg24 = mongoDatabase.getCollection("advertisement_click", Document.class);
		Document arg25 = new Document();
		arg25.put("addressCity", "DummyString#230");
		arg25.put("addressStreet", "DummyString#231");
		Document arg26 = new Document();
		arg26.put("address", arg25);
		arg26.put("businessName", "DummyString#232");
		Document arg27 = new Document();
		arg27.put("category", 1);
		arg27.put("ownerBusiness", arg26);
		arg27.put("price", 0);
		arg27.put("productName", "DummyString#233");
		Document arg28 = new Document();
		arg28.put("_id", 0);
		arg28.put("product", arg27);
		arg28.put("adId", 0);
		arg28.put("wasFavoriteCategory", false);
		arg28.put("userId", 0);
		assertEquals(arg28, arg24.find(eq(arg28.get("_id"))).first());
		assertEquals(1,arg24.countDocuments());
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg29 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg29.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg29.find(eq(arg10.get("_id"))).first());
		assertEquals(2,arg29.countDocuments());
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg30 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg14, arg30.find(eq(arg14.get("_id"))).first());
		assertEquals(arg17, arg30.find(eq(arg17.get("_id"))).first());
		assertEquals(2,arg30.countDocuments());
	}
	@Test public void test_recordClick_0_117() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#215");
		arg1.put("associatedInfluencerName", "DummyString#216");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#217");
		arg2.put("addressStreet", "DummyString#218");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#219");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#220");
		Document arg5 = new Document();
		arg5.put("_id", -1);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementText", "DummyString#229");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#230");
		arg7.put("addressStreet", "DummyString#231");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#232");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#233");
		Document arg10 = new Document();
		arg10.put("_id", 0);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		MongoCollection arg11 = mongoDatabase.getCollection("user",User.class);
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#121");
		arg12.put("addressStreet", "DummyString#122");
		Document arg13 = new Document();
		arg13.put("profileFirstName", "DummyString#123");
		arg13.put("profileLastName", "DummyString#124");
		arg13.put("representedBusinessName", "DummyString#125");
		arg13.put("representedBusinessPosition", "DummyString#126");
		Document arg14 = new Document();
		arg14.put("_id", -1);
		arg14.put("address", arg12);
		arg14.put("interestedInCategory", 0);
		arg14.put("profileInfos", arg13);
		arg14.put("userName", "DummyString#127");
		arg11.insertOne(arg14);
		Document arg15 = new Document();
		arg15.put("addressCity", "DummyString#182");
		arg15.put("addressStreet", "DummyString#183");
		Document arg16 = new Document();
		arg16.put("description", "DummyString#184");
		arg16.put("profileFirstName", "DummyString#185");
		arg16.put("profileLastName", "DummyString#186");
		Document arg17 = new Document();
		arg17.put("_id", 0);
		arg17.put("address", arg15);
		arg17.put("interestedInCategory", -1);
		arg17.put("profileInfos", arg16);
		arg17.put("userName", "DummyString#187");
		arg11.insertOne(arg17);
		MongoCollection arg18 = mongoDatabase.getCollection("advertisement_click",Document.class);
		Document arg19 = new Document();
		arg19.put("addressCity", "DummyString#238");
		arg19.put("addressStreet", "DummyString#239");
		Document arg20 = new Document();
		arg20.put("address", arg19);
		arg20.put("businessName", "DummyString#240");
		Document arg21 = new Document();
		arg21.put("category", 0);
		arg21.put("ownerBusiness", arg20);
		arg21.put("price", 0);
		arg21.put("productName", "DummyString#241");
		Document arg22 = new Document();
		arg22.put("_id", 0);
		arg22.put("adId", 0);
		arg22.put("product", arg21);
		arg22.put("userId", 0);
		arg22.put("wasFavoriteCategory", false);
		arg18.insertOne(arg22);
		int arg23 = (int) 0;
		int arg24 = (int) 0;
		int arg25 = (int) 0;
		AdvertisementRecommendationService arg26 = new AdvertisementRecommendationService();
		arg26.setAdvertisementClickCollection(arg18);
		UserService arg27 = new UserService();
		arg27.setUserCollection(arg11);
		arg26.setUserService(arg27);
		arg26.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg26.recordClick(arg23,arg24,arg25);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.ProductAdvertisementNotCreated e) {}
		// Generate assertion statements to check content of 'advertisement_click':
		MongoCollection<Document> arg28 = mongoDatabase.getCollection("advertisement_click", Document.class);
		assertEquals(arg22, arg28.find(eq(arg22.get("_id"))).first());
		assertEquals(1,arg28.countDocuments());
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg29 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg29.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg29.find(eq(arg10.get("_id"))).first());
		assertEquals(2,arg29.countDocuments());
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg30 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg14, arg30.find(eq(arg14.get("_id"))).first());
		assertEquals(arg17, arg30.find(eq(arg17.get("_id"))).first());
		assertEquals(2,arg30.countDocuments());
	}
	@Test public void test_recordClick_0_119() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#215");
		arg1.put("associatedInfluencerName", "DummyString#216");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#217");
		arg2.put("addressStreet", "DummyString#218");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#219");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#220");
		Document arg5 = new Document();
		arg5.put("_id", -2);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementText", "DummyString#229");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#230");
		arg7.put("addressStreet", "DummyString#231");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#232");
		Document arg9 = new Document();
		arg9.put("category", 1);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#233");
		Document arg10 = new Document();
		arg10.put("_id", -1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		MongoCollection arg11 = mongoDatabase.getCollection("user",User.class);
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#121");
		arg12.put("addressStreet", "DummyString#122");
		Document arg13 = new Document();
		arg13.put("profileFirstName", "DummyString#123");
		arg13.put("profileLastName", "DummyString#124");
		arg13.put("representedBusinessName", "DummyString#125");
		arg13.put("representedBusinessPosition", "DummyString#126");
		Document arg14 = new Document();
		arg14.put("_id", -1);
		arg14.put("address", arg12);
		arg14.put("interestedInCategory", 0);
		arg14.put("profileInfos", arg13);
		arg14.put("userName", "DummyString#127");
		arg11.insertOne(arg14);
		Document arg15 = new Document();
		arg15.put("addressCity", "DummyString#182");
		arg15.put("addressStreet", "DummyString#183");
		Document arg16 = new Document();
		arg16.put("description", "DummyString#184");
		arg16.put("profileFirstName", "DummyString#185");
		arg16.put("profileLastName", "DummyString#186");
		Document arg17 = new Document();
		arg17.put("_id", 0);
		arg17.put("address", arg15);
		arg17.put("interestedInCategory", 0);
		arg17.put("profileInfos", arg16);
		arg17.put("userName", "DummyString#187");
		arg11.insertOne(arg17);
		MongoCollection arg18 = mongoDatabase.getCollection("advertisement_click",Document.class);
		int arg19 = (int) 0;
		int arg20 = (int) 0;
		int arg21 = (int) 0;
		AdvertisementRecommendationService arg22 = new AdvertisementRecommendationService();
		arg22.setAdvertisementClickCollection(arg18);
		UserService arg23 = new UserService();
		arg23.setUserCollection(arg11);
		arg22.setUserService(arg23);
		arg22.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg22.recordClick(arg19,arg20,arg21);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.ProductAdvertisementNotFound e) {}
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg24 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg24.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg24.find(eq(arg10.get("_id"))).first());
		assertEquals(2,arg24.countDocuments());
		// Generate assertion statements to check content of 'user':
		MongoCollection<Document> arg25 = mongoDatabase.getCollection("user", Document.class);
		assertEquals(arg14, arg25.find(eq(arg14.get("_id"))).first());
		assertEquals(arg17, arg25.find(eq(arg17.get("_id"))).first());
		assertEquals(2,arg25.countDocuments());
	}
}