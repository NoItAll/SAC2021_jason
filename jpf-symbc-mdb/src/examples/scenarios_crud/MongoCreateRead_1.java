package scenarios_crud;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;

public class MongoCreateRead_1 {

	public static byte insertAndReadDoc(MongoCollection<Document> mc, double symD) {
		Document d0 = new Document();
		d0.put("income2", symD);
		d0.put("name", "Turing123");
		d0.put("_id", new ObjectId("000000000000000000000000")); // For the other documents, a String is the _id.
		
		FindIterable<Document> find = mc.find();
		MongoCursor<Document> cursor0 = find.cursor();
		mc.insertOne(d0);
		MongoCursor<Document> cursor1 = find.cursor();
		
		boolean found = false;
		boolean foundOnce = false;
		while (cursor0.hasNext()) {
			Document doc = cursor0.next();
			if (d0.get("_id").equals(doc.get("_id"))) {
				found = true;
				break;
			} else {
				System.out.println("In while 1: not found for: " + doc);
			}
		}
		
		assert !found;

		while (cursor1.hasNext()) {
			Document doc = cursor1.next();
			if (d0.get("_id").equals(doc.get("_id"))) {
				assert !foundOnce;
				foundOnce = true;
				System.out.println("In while 2: found for: " + doc);
				assert doc.getDouble("income") == d0.getDouble("income");
				assert doc.getInteger("status") == d0.getInteger("status");
				found = true;
			} else {
				System.out.println("In while 2: not found for: " + doc);
			}
		}
		
		assert found;
		return 0;
	}
	
	public static void main(String[] args) {
		insertAndReadDoc(new MMongoCollection<>("custs"), 0);
	}
}
