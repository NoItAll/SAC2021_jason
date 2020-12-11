package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters;
import scenarios_examples_data.Customer;

public class MongoRead_5 {
	
	public Customer readCustomersUsingLTEFilters(
			MongoCollection<Customer> custs, 
			int i,
			String k,
			double d) {
		int countCusts = 0;
		if (custs != null) {
			MongoCursor<Customer> mc = custs.find(MFilters.and(MFilters.lte("status", 13), MFilters.gt("income", 20.1))).iterator();
			while (mc.hasNext()) {
				Customer current = mc.next();
				if ((current.getStatus() > 13) || current.getIncome() <= 20.1) {
					assert false;
				}
				if (current.getStatus() <= 13 && countCusts > 0 && current.getIncome() > 22) {
					return current;
				}
				countCusts++;
			}
			return null;
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		MongoRead_5 mr = new MongoRead_5();
		mr.readCustomersUsingLTEFilters(new MMongoCollection<>("custs", Customer.class), 0, "0", 0.0);
	}

}
