package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MUpdates;
import scenarios_examples_data.Customer;
import scenarios_examples_data.Order;

public class MongoCreateReadUpdate_1 {
	public static double updateOrders(MongoCollection<Order> mc) {
		mc.updateMany(Filters.exists("customer.income"), MUpdates.set("customer.income", 13.9));
		MongoCursor<Order> mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Order current = mco.next();
			assert current.getCustomer().getIncome() == 13.9;
		}
		
		mc.updateMany(Filters.eq("customer.status", -3), MUpdates.set("customer.income", 12.9));
		
		mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Order current = mco.next();
			Customer customer = current.getCustomer();
			assert customer != null;
			if (customer.getStatus() != null) {
				assert (customer.getStatus() != -3 && customer.getIncome() == 13.9) ||
					customer.getStatus() == -3 && customer.getIncome() == 12.9;
			}
		}
		
		return 0;
	}
	
	public static void main(String[] args) {
		updateOrders(new MMongoCollection<>("orders", Order.class));
	}
}
