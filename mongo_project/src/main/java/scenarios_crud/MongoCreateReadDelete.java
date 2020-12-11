package scenarios_crud;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import scenarios_examples_data.Customer;
import scenarios_examples_data.Order;
import scenarios_examples_data.Product;

public class MongoCreateReadDelete {

	public int insertOrdersThenDelete(MongoCollection<Order> mc) {
		Order order1 = new Order()
				.setCustomer(new Customer().setId("Unique123").setStatus(-1).setIncome(2000));
		Order order2 = new Order()
				.setCustomer(new Customer().setId("Unique234").setStatus(-1).setIncome(5000));
		Order order3 = new Order()
				.setCustomer(new Customer().setId("Unique345").setStatus(-1).setIncome(5000))
				.setProduct(new Product().setName("P123").setPrice(200));
		
		List<Order> many = new ArrayList<>();
		many.add(order1);
		many.add(order2);
		many.add(order3);
		mc.insertMany(many);
		
		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;
		
		MongoCursor<Order> mco = mc.find().cursor();
		
		while (mco.hasNext()) {
			Order current = mco.next();
			if (current.getCustomer() == null || current.getCustomer().getId() == null) {
				continue;
			}
			if (current.getCustomer().getId().equals("Unique123")) {
				found1 = true;
			} else if (current.getCustomer().getId().equals("Unique234")) {
				found2 = true;
			} else if (current.getProduct().getName().equals("P123")) {
				found3 = true;
			}
		}
		
		assert found1 &&
			found2 &&
			found3;
		
		
		mc.deleteOne(Filters.eq("customer._id", "Unique123"));
		
		mco = mc.find().cursor();
		while (mco.hasNext()) {
			Order current = mco.next();
			assert current.getCustomer() == null || 
					current.getCustomer().getId() == null || 
					!current.getCustomer().getId().equals("Unique123");
		}
		
		mc.deleteMany(Filters.gt("customer.income", 4000));
		
		mco = mc.find().cursor();
		while (mco.hasNext()) {
			Order current = mco.next();
			assert current.getCustomer().getIncome() <= 4000;
		}		
		return 0;
	}	
}
