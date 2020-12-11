package social_media_app.entity;

import java.util.LinkedHashMap;

public class User {
	
	protected int id;
	protected Address address;
	protected LinkedHashMap<String, String> profileInfos;
	protected int interestedInCategory;
	protected String userName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getInterestedInCategory() {
		return interestedInCategory;
	}

	public void setInterestedInCategory(int interestedInCategory) {
		this.interestedInCategory = interestedInCategory;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public LinkedHashMap<String, String> getProfileInfos() {
		return profileInfos;
	}

	public void setProfileInfos(LinkedHashMap<String, String> profileInfos) {
		this.profileInfos = profileInfos;
	}

	public boolean equals(Object o) {
		if (!(o instanceof User)) {
			return false;
		}
		User temp = (User) o; 
		
		return id == temp.id && 
				profileInfos.equals(temp.profileInfos) && 
				address.equals(temp.address) && 
				interestedInCategory == temp.interestedInCategory && 
				userName.equals(userName);
	}
}