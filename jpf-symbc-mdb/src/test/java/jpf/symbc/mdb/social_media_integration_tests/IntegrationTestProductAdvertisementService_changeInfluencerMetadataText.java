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
public class IntegrationTestProductAdvertisementService_changeInfluencerMetadataText {
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
	@Test public void test_changeInfluencerMetadataText_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		String arg1 = "chosenProductName";
		String arg2 = "influencerName";
		String arg3 = "advertisedProductName";
		ProductAdvertisementService arg4 = new ProductAdvertisementService();
		arg4.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg4.changeInfluencerMetadataText(arg1,arg2,arg3);
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg5 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg5.countDocuments());
	}
	@Test public void test_changeInfluencerMetadataText_0_258() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "advertisedProductName");
		arg1.put("associatedInfluencerName", "influencerName");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#161");
		arg2.put("addressStreet", "DummyString#162");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#163");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "chosenProductName");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "advertisedProductName");
		arg6.put("associatedInfluencerName", "influencerName");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#425");
		arg7.put("addressStreet", "DummyString#426");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#427");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "chosenProductName");
		Document arg10 = new Document();
		arg10.put("_id", 1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "advertisedProductName");
		arg11.put("associatedInfluencerName", "influencerName");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#469");
		arg12.put("addressStreet", "DummyString#470");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "DummyString#471");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 0);
		arg14.put("productName", "chosenProductName");
		Document arg15 = new Document();
		arg15.put("_id", 2);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		String arg16 = "chosenProductName";
		String arg17 = "influencerName";
		String arg18 = "advertisedProductName";
		ProductAdvertisementService arg19 = new ProductAdvertisementService();
		arg19.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg19.changeInfluencerMetadataText(arg16,arg17,arg18);
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg20 = mongoDatabase.getCollection("product_advertisement", Document.class);
		Document arg21 = new Document();
		arg21.put("advertisementPostText", "advertisedProductName");
		arg21.put("associatedInfluencerName", "influencerName");
		Document arg22 = new Document();
		arg22.put("addressCity", "DummyString#161");
		arg22.put("addressStreet", "DummyString#162");
		Document arg23 = new Document();
		arg23.put("address", arg22);
		arg23.put("businessName", "DummyString#163");
		Document arg24 = new Document();
		arg24.put("category", 0);
		arg24.put("ownerBusiness", arg23);
		arg24.put("price", 0);
		arg24.put("productName", "chosenProductName");
		Document arg25 = new Document();
		arg25.put("_id", 0);
		arg25.put("advertisementMetadata", arg21);
		arg25.put("product", arg24);
		assertEquals(arg25, arg20.find(eq(arg25.get("_id"))).first());
		Document arg26 = new Document();
		arg26.put("advertisementPostText", "advertisedProductName");
		arg26.put("associatedInfluencerName", "influencerName");
		Document arg27 = new Document();
		arg27.put("addressCity", "DummyString#425");
		arg27.put("addressStreet", "DummyString#426");
		Document arg28 = new Document();
		arg28.put("address", arg27);
		arg28.put("businessName", "DummyString#427");
		Document arg29 = new Document();
		arg29.put("category", 0);
		arg29.put("ownerBusiness", arg28);
		arg29.put("price", 0);
		arg29.put("productName", "chosenProductName");
		Document arg30 = new Document();
		arg30.put("_id", 1);
		arg30.put("advertisementMetadata", arg26);
		arg30.put("product", arg29);
		assertEquals(arg30, arg20.find(eq(arg30.get("_id"))).first());
		Document arg31 = new Document();
		arg31.put("advertisementPostText", "advertisedProductName");
		arg31.put("associatedInfluencerName", "influencerName");
		Document arg32 = new Document();
		arg32.put("addressCity", "DummyString#469");
		arg32.put("addressStreet", "DummyString#470");
		Document arg33 = new Document();
		arg33.put("address", arg32);
		arg33.put("businessName", "DummyString#471");
		Document arg34 = new Document();
		arg34.put("category", 0);
		arg34.put("ownerBusiness", arg33);
		arg34.put("price", 0);
		arg34.put("productName", "chosenProductName");
		Document arg35 = new Document();
		arg35.put("_id", 2);
		arg35.put("advertisementMetadata", arg31);
		arg35.put("product", arg34);
		assertEquals(arg35, arg20.find(eq(arg35.get("_id"))).first());
		assertEquals(3,arg20.countDocuments());
	}
}