package scenarios_crud;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import scenarios_examples_data.Customer;

public class MongoCreateRead {
		
	public static Customer insertAndReadCustomer(MongoCollection<Customer> mc, double symD) {
		Customer customer = new Customer();
		customer.setId("Turing123");
		customer.setIncome(symD);
		customer.setStatus(2);
		
		FindIterable<Customer> find = mc.find();
		
		MongoCursor<Customer> cursor0 = find.cursor();
		mc.insertOne(customer);
		MongoCursor<Customer> cursor1 = find.cursor();
		System.out.println("Trying to find " + customer);
				
		boolean found = false;
		boolean foundOnce = false;
		while (cursor0.hasNext()) {
			Customer cust = (Customer) cursor0.next();
			if (customer.getId().equals(cust.getId())) {
				System.out.println("In while 1: found for: " + cust);
				found = true;
				break;
			} else {
				System.out.println("In while 1: not found for: " + cust);
			}
		}
		
		assert !found;

		while (cursor1.hasNext()) {
			Customer cust = cursor1.next();
			if (customer.getId().equals(cust.getId())) {
				assert !foundOnce;
				foundOnce = true;
				assert cust.getIncome() == customer.getIncome();
				assert cust.getStatus() == customer.getStatus();
				System.out.println("In while 2: found for: " + cust);
				found = true;
			} else {
				System.out.println("In while 2: not found for: " + cust);
			}
		}
		
		assert found;
		return customer;
	}
	
	public static void main(String[] args) {
		insertAndReadCustomer(new MMongoCollection<>("custs", Customer.class), 0);
	}
}
