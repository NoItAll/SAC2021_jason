package social_media_app.entity;

public class Business {
	
	protected String businessName;
	protected Address address;

	public String getBusinessName() {
		return businessName;
	}
	
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Business)) {
			return false;
		}
		Business b = (Business) o;
		return address.equals(b.address) &&
				businessName.equals(b.businessName);
				
	}
	
	public String toString() {
		return "Business{businessName="+businessName+",address="+address+"}";
	}
}
