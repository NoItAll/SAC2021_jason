package scenarios_crud;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;

public class MongoReadComplex_1 {
	
	public static int readDocsWithLTAndGTFilter(MongoCollection<Document> mc) {
		Document queryForUB = new Document();
		queryForUB.put("income", 24.2);
		queryForUB.put("status", 12);
		MongoCursor<Document> mco = mc.find(Filters.and(Filters.lt("customer", queryForUB), Filters.gt("customer.income", 22.1))).cursor();
		while (mco.hasNext()) {
			Document current = mco.next();
			assert ((!current.get("customer", Document.class).containsKey("status") || 
					current.get("customer", Document.class).getInteger("status") < 12) || 
					current.get("customer", Document.class).getDouble("income") < 24.2) && 
						current.get("customer", Document.class).getDouble("income") > 22.1;
			return 0;
		}
		MongoCursor<Document> mco2 = mc.find(Filters.and(Filters.eq("product.price", 203), Filters.gt("customer.income", 13))).cursor();
		while (mco2.hasNext()) {
			Document current = mco2.next();
			assert current.get("product", Document.class).getInteger("price") == 203 && current.get("customer", Document.class).getDouble("income") > 13;
			return 1;
		}
		return 2;
	}
	
	
	public static void main(String[] args) {
		readDocsWithLTAndGTFilter(new MMongoCollection<>("orders"));
	}

}
