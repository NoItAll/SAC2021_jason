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
public class IntegrationTestProductAdvertisementService_getInfluencerAdvertisements {
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
	@Test public void test_getInfluencerAdvertisements_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		String arg1 = "specificInfluencer";
		ProductAdvertisementService arg2 = new ProductAdvertisementService();
		arg2.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		ArrayList arg3 = new ArrayList();
		assertEquals(arg3,arg2.getInfluencerAdvertisements(arg1));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg4 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg4.countDocuments());
	}
	@Test public void test_getInfluencerAdvertisements_0_39() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#49");
		arg1.put("associatedInfluencerName", "specificInfluencer");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#51");
		arg2.put("addressStreet", "DummyString#52");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "DummyString#53");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "DummyString#54");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "DummyString#115");
		arg6.put("associatedInfluencerName", "specificInfluencer");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#117");
		arg7.put("addressStreet", "DummyString#118");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "DummyString#119");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "DummyString#120");
		Document arg10 = new Document();
		arg10.put("_id", 1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "DummyString#137");
		arg11.put("associatedInfluencerName", "specificInfluencer");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#139");
		arg12.put("addressStreet", "DummyString#140");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "DummyString#141");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 0);
		arg14.put("productName", "DummyString#142");
		Document arg15 = new Document();
		arg15.put("_id", 2);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		String arg16 = "specificInfluencer";
		ProductAdvertisementService arg17 = new ProductAdvertisementService();
		arg17.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		ArrayList arg18 = new ArrayList();
		ProductAdvertisement arg19 = new ProductAdvertisement();
		arg19.setId((int) 0);
		Product arg21 = new Product();
		arg21.setProductName("DummyString#54");
		Business arg23 = new Business();
		arg23.setBusinessName("DummyString#53");
		Address arg25 = new Address();
		arg25.setAddressStreet("DummyString#52");
		arg25.setAddressCity("DummyString#51");
		arg23.setAddress(arg25);
		arg21.setOwnerBusiness(arg23);
		arg21.setPrice((int) 0);
		arg21.setCategory((int) 0);
		arg19.setProduct(arg21);
		LinkedHashMap arg30 = new LinkedHashMap();
		String arg31 = "advertisementPostText";
		String arg32 = "DummyString#49";
		arg30.put(arg31,arg32);
		String arg33 = "associatedInfluencerName";
		String arg34 = "specificInfluencer";
		arg30.put(arg33,arg34);
		arg19.setAdvertisementMetadata(arg30);
		arg18.add(arg19);
		ProductAdvertisement arg35 = new ProductAdvertisement();
		arg35.setId((int) 1);
		Product arg37 = new Product();
		arg37.setProductName("DummyString#120");
		Business arg39 = new Business();
		arg39.setBusinessName("DummyString#119");
		Address arg41 = new Address();
		arg41.setAddressStreet("DummyString#118");
		arg41.setAddressCity("DummyString#117");
		arg39.setAddress(arg41);
		arg37.setOwnerBusiness(arg39);
		arg37.setPrice((int) 0);
		arg37.setCategory((int) 0);
		arg35.setProduct(arg37);
		LinkedHashMap arg46 = new LinkedHashMap();
		String arg47 = "advertisementPostText";
		String arg48 = "DummyString#115";
		arg46.put(arg47,arg48);
		String arg49 = "associatedInfluencerName";
		String arg50 = "specificInfluencer";
		arg46.put(arg49,arg50);
		arg35.setAdvertisementMetadata(arg46);
		arg18.add(arg35);
		ProductAdvertisement arg51 = new ProductAdvertisement();
		arg51.setId((int) 2);
		Product arg53 = new Product();
		arg53.setProductName("DummyString#142");
		Business arg55 = new Business();
		arg55.setBusinessName("DummyString#141");
		Address arg57 = new Address();
		arg57.setAddressStreet("DummyString#140");
		arg57.setAddressCity("DummyString#139");
		arg55.setAddress(arg57);
		arg53.setOwnerBusiness(arg55);
		arg53.setPrice((int) 0);
		arg53.setCategory((int) 0);
		arg51.setProduct(arg53);
		LinkedHashMap arg62 = new LinkedHashMap();
		String arg63 = "advertisementPostText";
		String arg64 = "DummyString#137";
		arg62.put(arg63,arg64);
		String arg65 = "associatedInfluencerName";
		String arg66 = "specificInfluencer";
		arg62.put(arg65,arg66);
		arg51.setAdvertisementMetadata(arg62);
		arg18.add(arg51);
		assertEquals(arg18,arg17.getInfluencerAdvertisements(arg16));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg67 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg67.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg67.find(eq(arg10.get("_id"))).first());
		assertEquals(arg15, arg67.find(eq(arg15.get("_id"))).first());
		assertEquals(3,arg67.countDocuments());
	}
}