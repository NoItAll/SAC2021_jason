package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import scenarios_examples_data.Customer;
import scenarios_examples_data.Order;

public class MongoReadComplex_4 {
	
	public static Order readOrdersWithLTAndGTFilter(MongoCollection<Order> mc) {
		Customer queryForUB = new Customer();
		queryForUB.setStatus(12);
		queryForUB.setIncome(24.2);
		Customer queryForLB = new Customer();
		queryForLB.setIncome(22.1);
		MongoCursor<Order> mco = mc.find(Filters.and(Filters.lt("customer", queryForUB), Filters.gt("customer", queryForLB))).cursor();
		while (mco.hasNext()) {
			Order current = mco.next();
			Customer cust = current.getCustomer();
			assert (cust.getStatus() == null || cust.getStatus() < 12 || cust.getIncome() < 24.2) && 
				(cust.getStatus() != null || cust.getIncome() > 22.1);
			return current;
		}
		return null;
	}
}
