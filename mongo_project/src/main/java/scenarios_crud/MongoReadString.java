package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import scenarios_examples_data.Customer;

public class MongoReadString {
	public static Customer searchCustomerWithId(MongoCollection<Customer> custs, String id) {
		MongoCursor<Customer> customerCursor = custs.find(Filters.eq(id)).cursor();
		if (customerCursor.hasNext()) {
			return customerCursor.next();
		} else {
			return null;
		}
	}
}
