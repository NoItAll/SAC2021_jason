package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import scenarios_examples_data.Customer;
import scenarios_examples_data.Order;

public class MongoCreateReadUpdate_1 {
	public static double updateOrders(MongoCollection<Order> mc) {
		mc.updateMany(Filters.exists("customer.income"), Updates.set("customer.income", 13.9));
		MongoCursor<Order> mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Order current = mco.next();
			assert current.getCustomer().getIncome() == 13.9;
		}
		
		mc.updateMany(Filters.eq("customer.status", -3), Updates.set("customer.income", 12.9));
		
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
}
