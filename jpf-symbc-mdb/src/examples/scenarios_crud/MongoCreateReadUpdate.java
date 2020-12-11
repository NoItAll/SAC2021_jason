package scenarios_crud;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MUpdates;

public class MongoCreateReadUpdate {
	
	public static int updateDocuments(MongoCollection<Document> mc) {
		mc.updateMany(Filters.exists("customer.income2", false), MUpdates.set("customer.income2", 13.9));
		
		MongoCursor<Document> mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Document current = mco.next();
			assert !current.containsKey("customer") || current.get("customer", Document.class).getDouble("income2").equals(13.9);
		}
		
		return 0;
	}
	
	
	public static void main(String[] args) {
		updateDocuments(new MMongoCollection<>("orders"));
	}

}
