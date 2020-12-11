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
import social_media_app.entity.Address;
import social_media_app.entity.Business;
import social_media_app.entity.Product;
import social_media_app.entity.ProductAdvertisement;
import social_media_app.service.ProductAdvertisementService;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestProductAdvertisementService_updateProduct {
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
	@Test public void test_updateProduct_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		String arg1 = "product";
		String arg2 = "amazingCompany";
		Product arg3 = new Product();
		arg3.setProductName("changedName");
		Business arg5 = new Business();
		arg5.setBusinessName("newBusiness");
		Address arg7 = new Address();
		arg7.setAddressStreet("newStreet");
		arg7.setAddressCity("newCity");
		arg5.setAddress(arg7);
		arg3.setOwnerBusiness(arg5);
		arg3.setPrice((int) 42);
		arg3.setCategory((int) 0);
		ProductAdvertisementService arg12 = new ProductAdvertisementService();
		arg12.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg12.updateProduct(arg1,arg2,arg3);
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg13 = mongoDatabase.getCollection("product_advertisement", Document.class);
		assertEquals(0,arg13.countDocuments());
	}
	@Test public void test_updateProduct_0_180() {
		MongoCollection arg0 = mongoDatabase.getCollection("product_advertisement",ProductAdvertisement.class);
		Document arg1 = new Document();
		arg1.put("advertisementPostText", "DummyString#59");
		arg1.put("associatedInfluencerName", "DummyString#60");
		Document arg2 = new Document();
		arg2.put("addressCity", "newCity");
		arg2.put("addressStreet", "newStreet");
		Document arg3 = new Document();
		arg3.put("address", arg2);
		arg3.put("businessName", "amazingCompany");
		Document arg4 = new Document();
		arg4.put("category", -1);
		arg4.put("ownerBusiness", arg3);
		arg4.put("price", 0);
		arg4.put("productName", "product");
		Document arg5 = new Document();
		arg5.put("_id", 0);
		arg5.put("advertisementMetadata", arg1);
		arg5.put("product", arg4);
		arg0.insertOne(arg5);
		Document arg6 = new Document();
		arg6.put("advertisementText", "DummyString#105");
		Document arg7 = new Document();
		arg7.put("addressCity", "newCity");
		arg7.put("addressStreet", "newStreet");
		Document arg8 = new Document();
		arg8.put("address", arg7);
		arg8.put("businessName", "amazingCompany");
		Document arg9 = new Document();
		arg9.put("category", -1);
		arg9.put("ownerBusiness", arg8);
		arg9.put("price", 0);
		arg9.put("productName", "product");
		Document arg10 = new Document();
		arg10.put("_id", 1);
		arg10.put("advertisementMetadata", arg6);
		arg10.put("product", arg9);
		arg0.insertOne(arg10);
		String arg11 = "product";
		String arg12 = "amazingCompany";
		Product arg13 = new Product();
		arg13.setProductName("changedName");
		Business arg15 = new Business();
		arg15.setBusinessName("newBusiness");
		Address arg17 = new Address();
		arg17.setAddressStreet("newStreet");
		arg17.setAddressCity("newCity");
		arg15.setAddress(arg17);
		arg13.setOwnerBusiness(arg15);
		arg13.setPrice((int) 42);
		arg13.setCategory((int) 0);
		ProductAdvertisementService arg22 = new ProductAdvertisementService();
		arg22.setProductAdvertisementCollection(arg0);
		// Generate assertEquals for method call
		arg22.updateProduct(arg11,arg12,arg13);
		// Generate assertion statements to check content of 'product_advertisement':
		MongoCollection<Document> arg23 = mongoDatabase.getCollection("product_advertisement", Document.class);
		Document arg24 = new Document();
		arg24.put("advertisementPostText", "DummyString#59");
		arg24.put("associatedInfluencerName", "DummyString#60");
		Document arg25 = new Document();
		arg25.put("addressCity", "newCity");
		arg25.put("addressStreet", "newStreet");
		Document arg26 = new Document();
		arg26.put("address", arg25);
		arg26.put("businessName", "newBusiness");
		Document arg27 = new Document();
		arg27.put("category", 0);
		arg27.put("ownerBusiness", arg26);
		arg27.put("price", 42);
		arg27.put("productName", "changedName");
		Document arg28 = new Document();
		arg28.put("_id", 0);
		arg28.put("advertisementMetadata", arg24);
		arg28.put("product", arg27);
		assertEquals(arg28, arg23.find(eq(arg28.get("_id"))).first());
		Document arg29 = new Document();
		arg29.put("advertisementText", "DummyString#105");
		Document arg30 = new Document();
		arg30.put("_id", 1);
		arg30.put("advertisementMetadata", arg29);
		arg30.put("product", arg27);
		assertEquals(arg30, arg23.find(eq(arg30.get("_id"))).first());
		assertEquals(2,arg23.countDocuments());
	}
}