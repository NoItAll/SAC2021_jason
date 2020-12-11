package scenarios_crud;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import scenarios_examples_data.Order;

public class MongoReadComplex {
		
	public int readOrdersWithGTFilter(MongoCollection<Order> mc) {
		FindIterable<Order> fi = mc.find(Filters.gt("customer.income", 22.1));
		MongoCursor<Order> mco = fi.cursor();
		while (mco.hasNext()) {
			Order current = mco.next();
			assert current.getCustomer().getIncome() > 22.1;
			if (current.getCustomer().getStatus() != null && current.getCustomer().getStatus() == 3) {
				return 1;
			} else {
				return 2;
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		MongoReadComplex mrc = new MongoReadComplex();
		mrc.readOrdersWithGTFilter(new MMongoCollection<>("orders", Order.class));
	}
}
