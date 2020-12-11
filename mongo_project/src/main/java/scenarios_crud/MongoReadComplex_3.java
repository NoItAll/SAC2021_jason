package scenarios_crud;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import scenarios_examples_data.Customer;
import scenarios_examples_data.Order;

public class MongoReadComplex_3 {

	public static Order readOrdersWithEQFilter(MongoCollection<Order> mc) {
		MongoCursor<Order> mco = mc.find(
				and(eq("customer.status", 12),
						eq("customer.enlistedBy.status", 13),
						eq("customer.enlistedBy.enlistedBy.status", 14))
				).cursor();
		
		while (mco.hasNext()) {
			Order current = mco.next();
			Customer customer = current.getCustomer();
			assert customer.getStatus() == 12 &&
					customer.getEnlistedBy().getStatus() == 13 && 
					customer.getEnlistedBy().getEnlistedBy().getStatus() == 14;
			return current;
		}
		return null;
	}
}
