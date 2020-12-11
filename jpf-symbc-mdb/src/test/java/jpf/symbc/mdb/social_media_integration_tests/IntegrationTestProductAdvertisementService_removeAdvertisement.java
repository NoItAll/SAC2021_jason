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
import social_media_app.service.ProductAdvertisementService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestProductAdvertisementService_removeAdvertisement {
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
	@Test public void test_removeAdvertisement_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		int arg1 = (int) 42;
		ProductAdvertisementService arg2 = new ProductAdvertisementService();
		arg2.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg2.removeAdvertisement(arg1);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.ProductAdvertisementNotFound e) {}
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg3 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg3.countDocuments());
	}
	@Test public void test_removeAdvertisement_0_27() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#38");
		arg1.put("associatedInfluencerName", "DummyString#39");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#40");
		arg2.put("addressStreet", "DummyString#41");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#42");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#43");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "DummyString#60");
		arg6.put("associatedInfluencerName", "DummyString#61");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#62");
		arg7.put("addressStreet", "DummyString#63");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#64");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#65");
		Document arg10 = new Document();
		arg10.put("_id", 1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "DummyString#71");
		arg11.put("associatedInfluencerName", "DummyString#72");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#73");
		arg12.put("addressStreet", "DummyString#74");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "DummyString#75");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 0);
		arg14.put("productName", "DummyString#76");
		Document arg15 = new Document();
		arg15.put("_id", -1);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		int arg16 = (int) -1;
		ProductAdvertisementService arg17 = new ProductAdvertisementService();
		arg17.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg17.removeAdvertisement(arg16);
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg18 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg18.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg18.find(eq(arg10.get("_id"))).first());
		assertEquals(2,arg18.countDocuments());
	}
	@Test public void test_removeAdvertisement_0_28() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#38");
		arg1.put("associatedInfluencerName", "DummyString#39");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#40");
		arg2.put("addressStreet", "DummyString#41");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#42");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#43");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "DummyString#60");
		arg6.put("associatedInfluencerName", "DummyString#61");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#62");
		arg7.put("addressStreet", "DummyString#63");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#64");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#65");
		Document arg10 = new Document();
		arg10.put("_id", 1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "DummyString#71");
		arg11.put("associatedInfluencerName", "DummyString#72");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#73");
		arg12.put("addressStreet", "DummyString#74");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "DummyString#75");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 0);
		arg14.put("productName", "DummyString#76");
		Document arg15 = new Document();
		arg15.put("_id", 2);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		int arg16 = (int) -1;
		ProductAdvertisementService arg17 = new ProductAdvertisementService();
		arg17.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		try {
			arg17.removeAdvertisement(arg16);
			fail("Uncaught exception expected.");
		} catch (social_media_app.exceptions.ProductAdvertisementNotFound e) {}
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg18 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg18.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg18.find(eq(arg10.get("_id"))).first());
		assertEquals(arg15, arg18.find(eq(arg15.get("_id"))).first());
		assertEquals(3,arg18.countDocuments());
	}
}