package scenarios_crud;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class MongoCreateReadUpdate {
	
	public static int updateDocuments(MongoCollection<Document> mc) {
		mc.updateMany(Filters.exists("customer.income2", false), Updates.set("customer.income2", 13.9));
		
		MongoCursor<Document> mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Document current = mco.next();
			assert !current.containsKey("customer") || current.get("customer", Document.class).getDouble("income2").equals(13.9);
		}
		
		return 0;
	}
}
