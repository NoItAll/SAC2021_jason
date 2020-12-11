package scenarios_examples_data;

public class Order {
	
	private Customer customer;
	private Product product;
	
	public Order() {}

	public Customer getCustomer() {
		return customer;
	}

	public Order setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public Product getProduct() {
		return product;
	}

	public Order setProduct(Product product) {
		this.product = product;
		return this;
	}
	
	public String toString() {
		return "Order{customer="+customer+",product="+product+"}";
	}
	
	public boolean equals(Object o) {
		if (o instanceof Order) {
			Order temp = (Order) o;
			return temp.getCustomer().equals(customer) && temp.getProduct().equals(product);
		} else {
			return false;
		}
	}
}
