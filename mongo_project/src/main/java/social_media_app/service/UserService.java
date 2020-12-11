package social_media_app.service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import org.bson.conversions.Bson;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

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
}
