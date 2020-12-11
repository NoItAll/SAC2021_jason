package scenarios_examples_data;

public class Product {
	private String name;
	private int price;
	
	public Product() {}

	public String getName() {
		return name;
	}

	public Product setName(String name) {
		this.name = name;
		return this;
	}

	public int getPrice() {
		return price;
	}

	public Product setPrice(int price) {
		this.price = price;
		return this;
	}
	
	public String toString() {
		return "Product{name="+name+",price="+price+"}";
	}
	
	public boolean equals(Object o) {
		if (o instanceof Product) {
			Product temp = (Product) o;
			if (temp.name == null) {
				return name == null;
			}
			return temp.name.equals(name) && temp.price == price;
		} else {
			return false;
		}
	}
}
