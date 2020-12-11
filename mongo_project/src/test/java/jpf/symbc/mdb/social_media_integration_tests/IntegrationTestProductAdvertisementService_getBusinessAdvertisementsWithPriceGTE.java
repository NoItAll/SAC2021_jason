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
public class IntegrationTestProductAdvertisementService_getBusinessAdvertisementsWithPriceGTE {
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
	@Test public void test_getBusinessAdvertisementsWithPriceGTE_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		String arg1 = "specificBusiness";
		int arg2 = (int) 10;
		ProductAdvertisementService arg3 = new ProductAdvertisementService();
		arg3.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		ArrayList arg4 = new ArrayList();
		assertEquals(arg4,arg3.getBusinessAdvertisementsWithPriceGTE(arg1,arg2));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg5 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg5.countDocuments());
	}
	@Test public void test_getBusinessAdvertisementsWithPriceGTE_0_583() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#401");
		arg1.put("associatedInfluencerName", "DummyString#402");
		Document arg2 = new Document();
		arg2.put("addressCity", "DummyString#403");
		arg2.put("addressStreet", "DummyString#404");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "specificBusiness");
		Document arg4 = new Document();
		arg4.put("category", 0);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", -1);
		arg4.put("productName", "DummyString#406");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementPostText", "DummyString#753");
		arg6.put("associatedInfluencerName", "DummyString#754");
		Document arg7 = new Document();
		arg7.put("addressCity", "DummyString#755");
		arg7.put("addressStreet", "DummyString#756");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "specificBusiness");
		Document arg9 = new Document();
		arg9.put("category", 0);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", -1);
		arg9.put("productName", "DummyString#758");
		Document arg10 = new Document();
		arg10.put("_id", -1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		Document arg11 = new Document();
		arg11.put("advertisementPostText", "DummyString#797");
		arg11.put("associatedInfluencerName", "DummyString#798");
		Document arg12 = new Document();
		arg12.put("addressCity", "DummyString#799");
		arg12.put("addressStreet", "DummyString#800");
		Document arg13 = new Document();
		arg13.put("address", arg12);
		arg13.put("businessName", "specificBusiness");
		Document arg14 = new Document();
		arg14.put("category", 0);
		arg14.put("ownerBusiness", arg13);
		arg14.put("price", 1);
		arg14.put("productName", "DummyString#802");
		Document arg15 = new Document();
		arg15.put("_id", -2);
		arg15.put("advertisementMetadata", arg11);
		arg15.put("product", arg14);
		arg0.insertOne(arg15);
		String arg16 = "specificBusiness";
		int arg17 = (int) 0;
		ProductAdvertisementService arg18 = new ProductAdvertisementService();
		arg18.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		ArrayList arg19 = new ArrayList();
		ProductAdvertisement arg20 = new ProductAdvertisement();
		arg20.setId((int) -2);
		Product arg22 = new Product();
		arg22.setProductName("DummyString#802");
		Business arg24 = new Business();
		arg24.setBusinessName("specificBusiness");
		Address arg26 = new Address();
		arg26.setAddressStreet("DummyString#800");
		arg26.setAddressCity("DummyString#799");
		arg24.setAddress(arg26);
		arg22.setOwnerBusiness(arg24);
		arg22.setPrice((int) 1);
		arg22.setCategory((int) 0);
		arg20.setProduct(arg22);
		LinkedHashMap arg31 = new LinkedHashMap();
		String arg32 = "advertisementPostText";
		String arg33 = "DummyString#797";
		arg31.put(arg32,arg33);
		String arg34 = "associatedInfluencerName";
		String arg35 = "DummyString#798";
		arg31.put(arg34,arg35);
		arg20.setAdvertisementMetadata(arg31);
		arg19.add(arg20);
		assertEquals(arg19,arg18.getBusinessAdvertisementsWithPriceGTE(arg16,arg17));
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg36 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(arg5, arg36.find(eq(arg5.get("_id"))).first());
		assertEquals(arg10, arg36.find(eq(arg10.get("_id"))).first());
		assertEquals(arg15, arg36.find(eq(arg15.get("_id"))).first());
		assertEquals(3,arg36.countDocuments());
	}
}