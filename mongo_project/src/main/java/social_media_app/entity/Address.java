package social_media_app.entity;

public class Address {
	
	protected String addressStreet;
	protected String addressCity;
	
	public String getAddressStreet() {
		return addressStreet;
	}
	
	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}
	
	public String getAddressCity() {
		return addressCity;
	}
	
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Address)) {
			return false;
		}
		Address a = (Address) o;
		return addressCity.equals(a.addressCity) && 
				addressStreet.equals(a.addressStreet);
	}
	
	public String toString() {
		return "Address{addressStreet="+addressStreet+",addressCity="+addressCity+"}";
	}
}
