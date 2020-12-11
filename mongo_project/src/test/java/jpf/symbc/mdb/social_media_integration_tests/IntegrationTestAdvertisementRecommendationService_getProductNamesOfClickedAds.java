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
import social_media_app.service.AdvertisementRecommendationService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestAdvertisementRecommendationService_getProductNamesOfClickedAds {
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
	@Test public void test_getProductNamesOfClickedAds_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("advertisement_click",Document.class);
		int arg1 = (int) 0;
		AdvertisementRecommendationService arg2 = new AdvertisementRecommendationService();
		arg2.setAdvertisementClickCollection(arg0);
		arg2.setUserService(null);
		arg2.setProductAdvertisementCollection(null);
		// Generate assertEquals for method call
		ArrayList arg3 = new ArrayList();
		assertEquals(arg3,arg2.getProductNamesOfClickedAds(arg1));
		// Generate assertion statements to check content of 'advertisement_click':
		MongoCollection<Document> arg4 = mongoDatabase.getCollection("advertisement_click", Document.class);
		assertEquals(0,arg4.countDocuments());
	}
	@Test public void test_getProductNamesOfClickedAds_0_13() {
		MongoCollection arg0 = mongoDatabase.getCollection("advertisement_click",Document.class);
		Document arg1 = new Document();
		arg1.put("addressCity", "DummyString#0");
		arg1.put("addressStreet", "DummyString#1");
		Document arg2 = new Document();
		arg2.put("address", arg1);
		arg2.put("businessName", "DummyString#2");
		Document arg3 = new Document();
		arg3.put("category", 0);
		arg3.put("ownerBusiness", arg2);
		arg3.put("price", 0);
		arg3.put("productName", "DummyString#3");
		Document arg4 = new Document();
		arg4.put("_id", -1);
		arg4.put("adId", 0);
		arg4.put("product", arg3);
		arg4.put("userId", -1);
		arg4.put("wasFavoriteCategory", false);
		arg0.insertOne(arg4);
		Document arg5 = new Document();
		arg5.put("addressCity", "DummyString#16");
		arg5.put("addressStreet", "DummyString#17");
		Document arg6 = new Document();
		arg6.put("address", arg5);
		arg6.put("businessName", "DummyString#18");
		Document arg7 = new Document();
		arg7.put("category", 0);
		arg7.put("ownerBusiness", arg6);
		arg7.put("price", 0);
		arg7.put("productName", "DummyString#19");
		Document arg8 = new Document();
		arg8.put("_id", 0);
		arg8.put("adId", 0);
		arg8.put("product", arg7);
		arg8.put("userId", -1);
		arg8.put("wasFavoriteCategory", false);
		arg0.insertOne(arg8);
		Document arg9 = new Document();
		arg9.put("addressCity", "DummyString#24");
		arg9.put("addressStreet", "DummyString#25");
		Document arg10 = new Document();
		arg10.put("address", arg9);
		arg10.put("businessName", "DummyString#26");
		Document arg11 = new Document();
		arg11.put("category", 0);
		arg11.put("ownerBusiness", arg10);
		arg11.put("price", 0);
		arg11.put("productName", "DummyString#27");
		Document arg12 = new Document();
		arg12.put("_id", 1);
		arg12.put("adId", 0);
		arg12.put("product", arg11);
		arg12.put("userId", 0);
		arg12.put("wasFavoriteCategory", false);
		arg0.insertOne(arg12);
		int arg13 = (int) 0;
		AdvertisementRecommendationService arg14 = new AdvertisementRecommendationService();
		arg14.setAdvertisementClickCollection(arg0);
		arg14.setUserService(null);
		arg14.setProductAdvertisementCollection(null);
		// Generate assertEquals for method call
		ArrayList arg15 = new ArrayList();
		String arg16 = "DummyString#27";
		arg15.add(arg16);
		assertEquals(arg15,arg14.getProductNamesOfClickedAds(arg13));
		// Generate assertion statements to check content of 'advertisement_click':
		MongoCollection<Document> arg17 = mongoDatabase.getCollection("advertisement_click", Document.class);
		assertEquals(arg4, arg17.find(eq(arg4.get("_id"))).first());
		assertEquals(arg8, arg17.find(eq(arg8.get("_id"))).first());
		assertEquals(arg12, arg17.find(eq(arg12.get("_id"))).first());
		assertEquals(3,arg17.countDocuments());
	}
}