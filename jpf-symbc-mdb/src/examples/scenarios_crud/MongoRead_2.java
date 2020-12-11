package scenarios_crud;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;

public class MongoRead_2 {

	public static Document readSchemaUsingDocument(MongoCollection<Document> docs) {
		MongoCursor<Document> mc = docs.find(MFilters.lte("status", 13)).iterator();
		while (mc.hasNext()) {
			Document current = mc.next();
			if (current.getInteger("status") > 13) {
				assert false;
			}
			if (current.getDouble("income") > 12 && current.getString("_id") != null) {
				return current;
			}
		}
		
		MongoCursor<Document> mc2 = docs.find(Filters.gt("status", 13)).iterator();
		
		while (mc2.hasNext()) {
			Document current = mc2.next();
			if (current.getInteger("status") <= 13) {
				assert false;
			}
			if (current.getDouble("income") > 12 && current.getString("_id") != null) {
				return current;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		readSchemaUsingDocument(new MMongoCollection<>("custs"));
	}
}
