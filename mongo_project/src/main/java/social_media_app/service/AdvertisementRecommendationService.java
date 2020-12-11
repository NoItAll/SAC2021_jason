package social_media_app.service;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import social_media_app.entity.ProductAdvertisement;
import social_media_app.entity.User;
import social_media_app.exceptions.ProductAdvertisementNotCreated;
import social_media_app.exceptions.ProductAdvertisementNotFound;
import social_media_app.exceptions.UserNotFound;

public class AdvertisementRecommendationService {
	protected MongoCollection<Document> advertisementClickCollection;
	protected UserService userService;
	protected MongoCollection<ProductAdvertisement> productAdvertisementCollection;
	
	public void setAdvertisementClickCollection(MongoCollection<Document> advertisementClickCollection) {
		this.advertisementClickCollection = advertisementClickCollection;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setProductAdvertisementCollection(MongoCollection<ProductAdvertisement> productAdvertisementCollection) {
		this.productAdvertisementCollection = productAdvertisementCollection;
	}
	
	public ProductAdvertisement decideShowAdvertisement(int userId) {
		User user = userService.getUserIfNonBusiness(userId);
		if (user == null) {
			return null;
		}
		// Try to show ad from suiting category
		Bson filter = Filters.eq("product.category", 
				user.getInterestedInCategory());
		ProductAdvertisement shownAd = productAdvertisementCollection.find(filter).first();
		if (shownAd != null) {
			return shownAd;
		} else {
			// Show arbitrary other ad
			return productAdvertisementCollection.find().first();
		}
	}
	
	public void recordClick(int id, int userId, int productAdvertisementId) {
		User user = null;
		ProductAdvertisement productAd = null;
		try {
			user = userService.getUser(userId);
		} catch (Exception e) {
			throw new UserNotFound("User could not be found.");
		}
		
		productAd = productAdvertisementCollection.find(Filters.eq(productAdvertisementId)).first();
		if (productAd == null) {
			throw new ProductAdvertisementNotFound("Product ad could not be found.");
		}
		boolean wasFavoritedCategory = user.getInterestedInCategory() == productAd.getProduct().getCategory();
		Document adClick = new Document();
		adClick.put("_id", id);
		adClick.put("userId", user.getId());
		adClick.put("adId", productAd.getId());
		adClick.put("product", productAd.getProduct());
		adClick.put("wasFavoriteCategory", wasFavoritedCategory);
		try {
			advertisementClickCollection.insertOne(adClick);
		} catch (MongoWriteException e) {
			throw new ProductAdvertisementNotCreated("Provided id is not unique.");
		}
	}
	
	public List<String> getProductNamesOfClickedAds(int userId) {
		List<String> result = new ArrayList<>();
		MongoCursor<Document> clickedAds = advertisementClickCollection.find(Filters.eq("userId", userId)).cursor();
		while (clickedAds.hasNext()) {
			Document current = clickedAds.next();
			result.add(current.get("product", Document.class).getString("productName"));
		}
		return result;
	}
}
