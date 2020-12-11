package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import scenarios_examples_data.Order;

public class MongoReadComplex_2 {

	public static Order readOrders(MongoCollection<Order> mc) {
		MongoCursor<Order> mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Order current = mco.next();
			// We expect a NullPointerException in some cases, since the versioned schema dictates that status can be missing.
			if (current.getCustomer().getStatus() > 2) {
				return current;
			} else if (current.getProduct().getPrice() > 12) {
				return current;
			}
			
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		readOrders(new MMongoCollection<>("orders", Order.class));
	}
}
