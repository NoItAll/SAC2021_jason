package social_media_app.entity;

import java.util.LinkedHashMap;

public class ProductAdvertisement {
	protected int id;
	protected Product product;
	protected LinkedHashMap<String, String> advertisementMetadata;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAdvertisementMetadata(LinkedHashMap<String, String> advertisementMetadata) {
		this.advertisementMetadata = advertisementMetadata;
	}
	
	public LinkedHashMap<String, String> getAdvertisementMetadata() {
		return advertisementMetadata;
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof ProductAdvertisement)) {
			return false;
		}
		ProductAdvertisement temp = (ProductAdvertisement) o;
		return id == temp.id && 
				advertisementMetadata.equals(temp.advertisementMetadata) &&
				product.equals(temp.product);
	}
	
	public String toString() {
		return "ProductAdvertisement{id="+id+",advertisementMetadata="+advertisementMetadata+",product="+product+"}";
	}
}
