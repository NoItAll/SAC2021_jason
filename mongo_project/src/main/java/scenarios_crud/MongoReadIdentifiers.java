package scenarios_crud;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

public class MongoReadIdentifiers {
	
	public static Document tryToReadManyDocsWithOneId(MongoCollection<Document> mc) {
		MongoCursor<Document> mcd = mc.find(Filters.eq(13)).cursor();
		boolean foundOnce = false;
		Document result = null;
		while (mcd.hasNext()) {
			result = mcd.next();
			assert !foundOnce;
			foundOnce = true;
		}
		return result;
	}
}
