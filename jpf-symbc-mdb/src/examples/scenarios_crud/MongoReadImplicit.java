package scenarios_crud;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import scenarios_examples_data.Customer;

// Implicitly use Mock-Objects without declaring them by 
// using their replacement in src/classes
public class MongoReadImplicit {
	
	public int printMDocumentsUsingLTEFilters(MongoCollection<Document> mc) {
		int countCusts = 0;
		MongoCursor<Document> cursor = mc.find(Filters.lte("status", 13)).iterator();
		List<Document> documents = new ArrayList<>();
		while (cursor.hasNext()) {
			Document current = cursor.next();
			documents.add(current);
			if ((current.getInteger("status") > 13)) {
				assert false;
			}
			if (current.getInteger("status") <= 13 && countCusts > 0 && current.getDouble("income") > 22) {
				assert current.containsKey("income");
				return 1;
			}
			countCusts++;
		}
		return 0;
	}
	
	public static void main(String[] args ) {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoClient mc = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
		MongoDatabase mdb = mc.getDatabase("devel"); 
		MongoCollection<Document> docCollection = mdb.getCollection("custs");
		(new MongoReadImplicit()).printMDocumentsUsingLTEFilters(docCollection);
	}
}
