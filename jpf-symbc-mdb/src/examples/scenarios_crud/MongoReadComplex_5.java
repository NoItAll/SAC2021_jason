package scenarios_crud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import scenarios_examples_data.Customer;
import scenarios_examples_data.Order;

public class MongoReadComplex_5 {
	
	public static Order readOrdersWithLTFilter(MongoCollection<Order> mc) {
		Customer queryForUB = new Customer();
		queryForUB.setStatus(12);
		queryForUB.setIncome(22.2);
		MongoCursor<Order> mco = mc.find(Filters.lt("customer", queryForUB)).cursor();
		while (mco.hasNext()) {
			Order current = mco.next();
			Customer cust = current.getCustomer();
			
			assert cust.getStatus() == null || cust.getStatus() < 12 || cust.getIncome() < 22.2;
		}
		return null;
	}
	
	public static void main(String[] args) {
		readOrdersWithLTFilter(new MMongoCollection<>("orders", Order.class));
	}
}
