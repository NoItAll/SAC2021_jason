package social_media_app.service;

import static gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.and;
import static gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.eq;
import static gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MFilters.exists;
import static gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MUpdates.combine;
import static gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MUpdates.set;

import java.util.LinkedHashMap;

import org.bson.conversions.Bson;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import social_media_app.entity.Address;
import social_media_app.entity.User;
import social_media_app.exceptions.UserNotCreated;
import social_media_app.exceptions.UserNotFound;

public class UserService {
	protected MongoCollection<User> userCollection;
	
	public void setUserCollection(MongoCollection<User> mc) {
		this.userCollection = mc;
	}

	public void insertUser(User user) {
		try {
			userCollection.insertOne(user);
		} catch (MongoWriteException e) {
			throw new UserNotCreated("Provided id is not unique.");
		}
	}
	
	public User getUser(int id) {
		User result = userCollection.find(eq(id)).first();
		if (result == null) {
			throw new UserNotFound("User not found.");
		}
		return result;
	}
	
	public void setInterestedInCategory(int id, int interestedInCategory) {
		User user = userCollection.find(eq(id)).first();
		if (user == null) {
			throw new UserNotFound("User not found.");
		}
		Bson updatePm = set("interestedInCategory", interestedInCategory);
		userCollection.updateOne(eq(id), updatePm);
	}
	
	public void updateUser(User user) {
		Bson updatePi = set("profileInfos", user.getProfileInfos());
		Bson updateUserName = set("userName", user.getUserName());
		UpdateResult ur = userCollection.updateOne(eq(user.getId()), combine(updatePi, updateUserName));
		
		if (ur.getMatchedCount() < 1) {
			throw new UserNotFound("User was not found.");
		}
	}
	
	public User getUserIfNonBusiness(int id) {
		Bson filter = 
				and(eq(id), exists("profileInfos.representedBusinessName", false));
		
		User result = userCollection.find(filter).first();
		
		return result;
	}
	
	public static void main(String[] args) {
		UserService u = new UserService();
		MongoCollection<User> mc = new MMongoCollection<>("user", User.class, 3);
		u.driver_getUser(mc, 0);
		//u.driver_getUserIfNonBusiness(mc, 0);
		//u.driver_insertUser(mc, new User());
		//u.driver_setInterestedInCategory(mc, 42, 0);
		//u.driver_updateUser(mc, new User());
	}
	
	public void driver_insertUser(MongoCollection<User> mc, User user) {
		setUserCollection(mc);
		user.setId(42);
		user.setInterestedInCategory((byte) 0);
		user.setUserName("specificUser");
		LinkedHashMap<String, String> profileInfos = new LinkedHashMap<>();
		profileInfos.put("profileFirstName", "firstName");
		profileInfos.put("profileLastName", "lastName");
		profileInfos.put("description", "userDescription");
		Address address = new Address();
		address.setAddressCity("city");
		address.setAddressStreet("street");
		user.setProfileInfos(profileInfos);
		user.setAddress(address);
		insertUser(user);
	}
	
	public User driver_getUser(MongoCollection<User> mc, int id) {
		setUserCollection(mc);
		return getUser(id);
	}
	
	public void driver_setInterestedInCategory(MongoCollection<User> mc, int id, int interestedInCategory) {
		setUserCollection(mc);
		setInterestedInCategory(id, interestedInCategory);
	}
	
	public void driver_updateUser(MongoCollection<User> mc, User user) {
		setUserCollection(mc);
		user.setUserName("newUserName");
		LinkedHashMap<String, String> profileInfos = new LinkedHashMap<>();
		profileInfos.put("profileFirstName", "firstName");
		profileInfos.put("profileLastName", "lastName");
		profileInfos.put("description", "userDescription");
		Address address = new Address();
		address.setAddressCity("city");
		address.setAddressStreet("street");
		user.setAddress(address);
		user.setProfileInfos(profileInfos);
		updateUser(user);
	}
	
	public User driver_getUserIfNonBusiness(MongoCollection<User> mc, int id) {
		setUserCollection(mc);
		return getUserIfNonBusiness(id);
	}	
}
