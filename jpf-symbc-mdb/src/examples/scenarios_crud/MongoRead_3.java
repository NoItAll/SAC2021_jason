package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;
import scenarios_examples_data.Customer;

public class MongoRead_3 {
	
	public static int readCustomersUsingNeFilters(MongoCollection<Customer> custs) {
		int countCusts = 0;
		if (custs != null) {
			MongoCursor<Customer> mc = custs.find(MFilters.ne("status", 13)).iterator();
			while (mc.hasNext()) {
				Customer current = mc.next();
				if (current.getStatus() == 13) {
					assert false;
				} else {
					System.out.print("");
				}
				if (current.getStatus() > 13 && countCusts > 1) {
					return 3;
				}
				countCusts++;
			}
			return 1;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		readCustomersUsingNeFilters(new MMongoCollection<>("custs", Customer.class));
	}

}
