package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;
import scenarios_examples_data.Customer;

public class MongoRead_6 {
	
	public static int readCustomersUsingAndFilters(MongoCollection<Customer> custs) {
		int countCusts = 0;
		if (custs != null) {
			MongoCursor<Customer> mc = custs.find(MFilters.and(MFilters.lt("status", 15), MFilters.gt("status", 13))).iterator();
			while (mc.hasNext()) {
				Customer current = mc.next();
				if (current.getStatus() != 14) {
					assert false;
				}
				if (current.getStatus() > 12 && countCusts > 1) {
					return 3;
				}
				countCusts++;
			}
			return 1;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		readCustomersUsingAndFilters(new MMongoCollection<>("custs", Customer.class));
	}
}
