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
import social_media_app.entity.Business;
import social_media_app.entity.Product;
import social_media_app.entity.ProductAdvertisement;
import social_media_app.service.ProductAdvertisementService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestProductAdvertisementService_getCheapestAd {
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
	@Test public void test_getCheapestAd_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		String arg1 = "someProduct";
		ProductAdvertisementService arg2 = new ProductAdvertisementService();
		arg2.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		assertEquals(null,arg2.getCheapestAd(arg1));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg3 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg3.countDocuments());
	}
	@Test public void test_getCheapestAd_0_135() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#137");
		arg1.put("associatedInfluencerName", "DummyString#138");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#139");
		arg2.put("addressStreet", "DummyString#140");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#141");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "someProduct");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "DummyString#236");
		arg6.put("associatedInfluencerName", "DummyString#237");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#238");
		arg7.put("addressStreet", "DummyString#239");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#240");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "someProduct");
		Document arg10 = new Document();
		arg10.put("_id", -1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "DummyString#269");
		arg11.put("associatedInfluencerName", "DummyString#270");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#271");
		arg12.put("addressStreet", "DummyString#272");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "DummyString#273");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 1);
		arg14.put("productName", "someProduct");
		Document arg15 = new Document();
		arg15.put("_id", -2);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		String arg16 = "someProduct";
		ProductAdvertisementService arg17 = new ProductAdvertisementService();
		arg17.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		ProductAdvertisement arg18 = new ProductAdvertisement();
		arg18.setId((int) -2);
		Product arg20 = new Product();
		arg20.setProductName("someProduct");
		Business arg22 = new Business();
		arg22.setBusinessName("DummyString#273");
		Address arg24 = new Address();
		arg24.setAddressStreet("DummyString#272");
		arg24.setAddressCity("DummyString#271");
		arg22.setAddress(arg24);
		arg20.setOwnerBusiness(arg22);
		arg20.setPrice((int) 1);
		arg20.setCategory((int) 0);
		arg18.setProduct(arg20);
		LinkedHashMap arg29 = new LinkedHashMap();
		String arg30 = "advertisementPostText";
		String arg31 = "DummyString#269";
		arg29.put(arg30,arg31);
		String arg32 = "associatedInfluencerName";
		String arg33 = "DummyString#270";
		arg29.put(arg32,arg33);
		String arg34 = "cheaperThan";
		String arg35 = "2";
		arg29.put(arg34,arg35);
		arg18.setAdvertisementMetadata(arg29);
		assertEquals(arg18,arg17.getCheapestAd(arg16));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg36 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg36.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg36.find(eq(arg10.get("_id"))).first());
		assertEquals(arg15, arg36.find(eq(arg15.get("_id"))).first());
		assertEquals(3,arg36.countDocuments());
	}
	@Test public void test_getCheapestAd_0_136() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#137");
		arg1.put("associatedInfluencerName", "DummyString#138");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#139");
		arg2.put("addressStreet", "DummyString#140");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#141");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "someProduct");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "DummyString#236");
		arg6.put("associatedInfluencerName", "DummyString#237");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#238");
		arg7.put("addressStreet", "DummyString#239");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#240");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "someProduct");
		Document arg10 = new Document();
		arg10.put("_id", -1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "DummyString#269");
		arg11.put("associatedInfluencerName", "DummyString#270");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#271");
		arg12.put("addressStreet", "DummyString#272");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "DummyString#273");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 0);
		arg14.put("productName", "someProduct");
		Document arg15 = new Document();
		arg15.put("_id", -2);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		String arg16 = "someProduct";
		ProductAdvertisementService arg17 = new ProductAdvertisementService();
		arg17.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		ProductAdvertisement arg18 = new ProductAdvertisement();
		arg18.setId((int) 0);
		Product arg20 = new Product();
		arg20.setProductName("someProduct");
		Business arg22 = new Business();
		arg22.setBusinessName("DummyString#141");
		Address arg24 = new Address();
		arg24.setAddressStreet("DummyString#140");
		arg24.setAddressCity("DummyString#139");
		arg22.setAddress(arg24);
		arg20.setOwnerBusiness(arg22);
		arg20.setPrice((int) 0);
		arg20.setCategory((int) 0);
		arg18.setProduct(arg20);
		LinkedHashMap arg29 = new LinkedHashMap();
		String arg30 = "advertisementPostText";
		String arg31 = "DummyString#137";
		arg29.put(arg30,arg31);
		String arg32 = "associatedInfluencerName";
		String arg33 = "DummyString#138";
		arg29.put(arg32,arg33);
		String arg34 = "cheaperThan";
		String arg35 = "2";
		arg29.put(arg34,arg35);
		arg18.setAdvertisementMetadata(arg29);
		assertEquals(arg18,arg17.getCheapestAd(arg16));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg36 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg36.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg36.find(eq(arg10.get("_id"))).first());
		assertEquals(arg15, arg36.find(eq(arg15.get("_id"))).first());
		assertEquals(3,arg36.countDocuments());
	}
}