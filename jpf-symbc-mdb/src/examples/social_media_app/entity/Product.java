package social_media_app.entity;

public class Product {
	
	protected String productName;
	protected Business ownerBusiness;
	protected int price;
	protected int category;
			
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Business getOwnerBusiness() {
		return ownerBusiness;
	}
	
	public void setOwnerBusiness(Business ownerBusiness) {
		this.ownerBusiness = ownerBusiness;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Product)) {
			return false;
		}
		Product temp = (Product) o;
		return productName.equals(temp.productName) && 
				ownerBusiness.equals(temp.ownerBusiness) &&
				price == temp.price && 
				category == temp.category;
	}
	
	public String toString() {
		return "Product{productName="+productName+",ownerBusiness="+ownerBusiness+",price="+price+"}";
	}
}
