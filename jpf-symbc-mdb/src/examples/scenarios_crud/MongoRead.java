package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import scenarios_examples_data.Customer;

//Represents service-invocation...
public class MongoRead {
	
	/* METHODS USED FOR DEMONSTRATION */ 
	
	public static void checkFirstCustomer(MongoCollection<Customer> custs) {
		MongoCursor<Customer> mc = custs.find().iterator();
		Customer test = mc.next(); // Exception is expected for in one case.
		if (test.getIncome() > 12) {
			return;
		} else {
			return;
		}
	}
	
	public static void main(String[] args) {
		checkFirstCustomer(new MMongoCollection<>("custs", Customer.class));
	}
}
