package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;
import scenarios_examples_data.Customer;

public class MongoRead_4 {

	public static double readCustomersUsingGTEFilters(MongoCollection<Customer> custs) {
		int countCusts = 0;
		if (custs != null) {
			MongoCursor<Customer> mc = custs.find(MFilters.gte("status", 13)).iterator();
			while (mc.hasNext()) {
				Customer current = mc.next();
				if ((current.getStatus() < 13)) {
					assert false;
				}
				if (current.getStatus() > 13 && countCusts > 1) {
					return current.getIncome();
				}
				countCusts++;
			}
			return 1;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		readCustomersUsingGTEFilters(new MMongoCollection<>("custs", Customer.class));
	}
	
}
