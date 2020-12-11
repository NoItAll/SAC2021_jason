package jpf.symbc.mdb.generated_integration_tests;

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
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@SuppressWarnings("all")
public class IntegrationTestMongoReadIdentifiers {
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
	@Test public void test_tryToReadManyDocsWithOneId_0_0() {
		MongoCollection arg0 = mongoDatabase.getCollection("id_containers",Document.class);
		// Generate assertEquals for method call
		assertEquals(null,scenarios_crud.MongoReadIdentifiers.tryToReadManyDocsWithOneId(arg0));
		// Generate assertion statements to check content of 'id_containers':
		MongoCollection<Document> arg1 = mongoDatabase.getCollection("id_containers", Document.class);
		assertEquals(0,arg1.countDocuments());
	}
	@Test public void test_tryToReadManyDocsWithOneId_0_8() {
		MongoCollection arg0 = mongoDatabase.getCollection("id_containers",Document.class);
		Document arg1 = new Document();
		arg1.put("_id", 0);
		arg0.insertOne(arg1);
		Document arg2 = new Document();
		arg2.put("_id", 1);
		arg0.insertOne(arg2);
		Document arg3 = new Document();
		arg3.put("_id", 13);
		arg0.insertOne(arg3);
		// Generate assertEquals for method call
		Document arg4 = new Document();
		String arg5 = "_id";
		Integer arg6 = new Integer(13);
		arg4.put(arg5,arg6);
		assertEquals(arg4,scenarios_crud.MongoReadIdentifiers.tryToReadManyDocsWithOneId(arg0));
		// Generate assertion statements to check content of 'id_containers':
		MongoCollection<Document> arg7 = mongoDatabase.getCollection("id_containers", Document.class);
		assertEquals(arg1, arg7.find(eq(arg1.get("_id"))).first());
		assertEquals(arg2, arg7.find(eq(arg2.get("_id"))).first());
		assertEquals(arg3, arg7.find(eq(arg3.get("_id"))).first());
		assertEquals(3,arg7.countDocuments());
	}
}