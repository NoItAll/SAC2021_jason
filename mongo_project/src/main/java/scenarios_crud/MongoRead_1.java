package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import scenarios_examples_data.Customer;

public class MongoRead_1 {
	
	public static int readCustomersUsingOrFilters(MongoCollection<Customer> custs) {
		int countCusts = 0;
		MongoCursor<Customer> mc = custs.find(Filters.or(Filters.lt("status", 13), Filters.gt("status", 15))).iterator();
		while (mc.hasNext()) {
			Customer current = mc.next();
			assert !(current.getStatus() == 13 || current.getStatus() == 14 || current.getStatus() == 15);
			if (current.getStatus() > 22 && countCusts > 1) {
				return 1; // This can possibly fail for integration tests: We cannot assume that inserted objects are in the same order.
			}
			countCusts++;
		}
		return 0;
	}
}
