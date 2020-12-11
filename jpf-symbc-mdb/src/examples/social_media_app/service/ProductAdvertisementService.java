package social_media_app.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import social_media_app.entity.Address;
import social_media_app.entity.Business;
import social_media_app.entity.Product;
import social_media_app.entity.ProductAdvertisement;
import social_media_app.exceptions.ProductAdvertisementNotCreated;
import social_media_app.exceptions.ProductAdvertisementNotFound;

public class ProductAdvertisementService {
	protected MongoCollection<ProductAdvertisement> productAdvertisementCollection;
	
	public void setProductAdvertisementCollection(MongoCollection<ProductAdvertisement> mc) {
		this.productAdvertisementCollection = mc;
	}
	
	public void insertRegularProductAdvertisement(
			int id,
			String productName, 
			String businessName, 
			String businessStreet, 
			String businessCity,
			int productPrice, 
			int category,
			String advertisementText) {
		ProductAdvertisement pa = new ProductAdvertisement();
		Business b = new Business();
		Address a = new Address();
		a.setAddressStreet(businessStreet);
		a.setAddressCity(businessCity);
		b.setAddress(a);
		b.setBusinessName(businessName);
		Product p = new Product();
		p.setOwnerBusiness(b);
		p.setPrice(productPrice);
		p.setProductName(productName);
		p.setCategory(category);
		pa.setProduct(p);
		LinkedHashMap<String, String> metadata = new LinkedHashMap<>();
		metadata.put("advertisementText", advertisementText);
		pa.setAdvertisementMetadata(metadata);
		pa.setId(id);
		try {
			productAdvertisementCollection.insertOne(pa);
		} catch (MongoWriteException e) {
			throw new ProductAdvertisementNotCreated("Provided id is not unique.");
		}
	}
	
	public void insertInfluencerProductAdvertisement(
			int id,
			String productName, 
			String businessName, 
			String businessStreet, 
			String businessCity,
			int productPrice,
			int category,
			String influencerName,
			String advertisementText) {
		ProductAdvertisement pa = new ProductAdvertisement();
		Business b = new Business();
		Address a = new Address();
		a.setAddressStreet(businessStreet);
		a.setAddressCity(businessCity);
		b.setAddress(a);
		b.setBusinessName(businessName);
		Product p = new Product();
		p.setOwnerBusiness(b);
		p.setPrice(productPrice);
		p.setProductName(productName);
		p.setCategory(category);
		pa.setProduct(p);
		LinkedHashMap<String, String> metadata = new LinkedHashMap<>();
		metadata.put("associatedInfluencerName", influencerName);
		metadata.put("advertisementPostText", advertisementText);
		pa.setAdvertisementMetadata(metadata);
		pa.setId(id);
		try {
			productAdvertisementCollection.insertOne(pa);
		} catch (MongoWriteException e) {
			throw new ProductAdvertisementNotCreated("Provided id is not unique.");
		}
	}

	public void updateProduct(String productName, String businessName, Product product) {
		Bson productUpdate = Updates.set("product", product);
		Bson filter = Filters.and(Filters.eq("product.productName", productName), 
				Filters.eq("product.ownerBusiness.businessName", businessName));
		productAdvertisementCollection.updateMany(filter, productUpdate);
	}
	
	public void changeInfluencerMetadataText(String productName, String userName, String newText) {
		Bson influencerNameUpdate = Updates.set("advertisementMetadata.advertisementPostText", newText);
		productAdvertisementCollection.updateMany(
				Filters.and(Filters.eq("product.productName", productName),
						Filters.eq("advertisementMetadata.associatedInfluencerName", userName)),
				influencerNameUpdate);
	}

	public List<ProductAdvertisement> getInfluencerAdvertisements(String influencerUserName) {
		Bson influencerFilter = Filters.eq("advertisementMetadata.associatedInfluencerName", influencerUserName);
		MongoCursor<ProductAdvertisement> advertisements = 
				productAdvertisementCollection.find(influencerFilter).cursor();
		
		List<ProductAdvertisement> result = new ArrayList<>();
		
		while (advertisements.hasNext()) {
			result.add(advertisements.next());
		}
		return result;
	}
	
	public List<ProductAdvertisement> getBusinessAdvertisements(String businessName) {
		Bson businessFilter = Filters.eq("product.ownerBusiness.businessName", businessName);
		MongoCursor<ProductAdvertisement> mc = productAdvertisementCollection.find(businessFilter).cursor();
		
		List<ProductAdvertisement> result = new ArrayList<>();
		
		while (mc.hasNext()) {
			result.add(mc.next());
		}
		
		return result;
	}
	
	public List<ProductAdvertisement> getBusinessAdvertisementsWithPriceGTE(String businessName, int price) {
		Bson businessFilter = Filters.eq("product.ownerBusiness.businessName", businessName);
		Bson priceFilter = Filters.gte("product.price", price);
		MongoCursor<ProductAdvertisement> mc = 
				productAdvertisementCollection.find(Filters.and(businessFilter, priceFilter)).cursor();
		
		List<ProductAdvertisement> result = new ArrayList<>();
		
		while (mc.hasNext()) {
			result.add(mc.next());
		}
		
		return result;
	}
	
	public long countInfluencerAdvertisementsForBusiness(String businessName) {
		Bson businessFilter = Filters.eq("product.ownerBusiness.businessName", businessName);
		Bson isInfluencerEntryFilter = Filters.exists("advertisementMetadata.associatedInfluencerName");
		
		return productAdvertisementCollection.countDocuments(Filters.and(businessFilter, isInfluencerEntryFilter));
		
	}
		
	public void removeAdvertisement(int id) {
		Bson idFilter = Filters.eq(id);
		DeleteResult dr = productAdvertisementCollection.deleteOne(idFilter);
		
		if (dr.getDeletedCount() < 1) {
			throw new ProductAdvertisementNotFound("Advertisement was not found.");
		}
	}
	
	public long removeBusinessAdvertisements(String businessName) {
		Bson businessFilter = Filters.eq("product.ownerBusiness.businessName", businessName);
		DeleteResult dr = productAdvertisementCollection.deleteMany(businessFilter);
		
		if (dr.getDeletedCount() < 1) {
			throw new ProductAdvertisementNotFound("There are no advertisements for this business.");
		}
		return dr.getDeletedCount();
	}
	
	public ProductAdvertisement getCheapestAd(String productName) {
		// Get cheapest product for name
		Bson productNameFilter = Filters.eq("product.productName", productName);
		
		MongoCursor<ProductAdvertisement> mc = productAdvertisementCollection.find(productNameFilter).cursor();
		
		ProductAdvertisement result = null;
		
		int cheaperThan = 0;
		while (mc.hasNext()) {
			ProductAdvertisement current = mc.next();
			if (result == null || result.getProduct().getPrice() < current.getProduct().getPrice()) {
				if (result != null) {
					cheaperThan++;
				}
				result = current;
			} else {
				cheaperThan++;
			}
		}
		
		if (cheaperThan > 0) {
			result.getAdvertisementMetadata().put("cheaperThan", cheaperThan+"");
		}
		
		return result;
	}
	
	/* ##########DRIVER METHODS########## */
	
	public static void main(String[] args) {
		ProductAdvertisementService p = new ProductAdvertisementService();
		MongoCollection<ProductAdvertisement> mc = new MMongoCollection<>("product_advertisement", ProductAdvertisement.class, 3);
		//p.driver_changeInfluencerMetadataText(mc, "chosenProductName", "influencerName", "advertisedProductName");
		//p.driver_countInfluencerAdvertisementsForBusiness(mc, "specificBusiness");
		//p.driver_getBusinessAdvertisements(mc, "specificBusiness");
		//p.driver_getBusinessAdvertisementsWithPriceGTE(mc, "specificBusiness", 10);
		//p.driver_getCheapestAd(mc, "someProduct");
		//p.driver_getInfluencerAdvertisements(mc, "specificInfluencer");
		//p.driver_insertInfluencerProductAdvertisement(mc, 0, "productName", "businessName", "businessStreet", "businessCity", 42, 1, "influencerName", "advertisementText");
		//p.driver_insertRegularProductAdvertisement(mc, 0, "productName", "businessName", "businessStreet", "businessCity", 12, 0, "advertisementText");
		//p.driver_removeAdvertisement(mc, 42);
		//p.driver_removeBusinessAdvertisements(mc, "thisBusiness");
		p.driver_updateProduct(mc, "product", "amazingCompany", new Product());
	}
	
	public void driver_insertRegularProductAdvertisement(MongoCollection<ProductAdvertisement> mc, int id, String productName, String businessName, String businessStreet, 
			String businessCity, int productPrice, int category, String advertisementText) {
		setProductAdvertisementCollection(mc);
		insertRegularProductAdvertisement(id, productName, businessName, businessStreet, businessCity, productPrice, category, advertisementText);
	}
	
	public void driver_insertInfluencerProductAdvertisement(
			MongoCollection<ProductAdvertisement> mc, int id, String productName, String businessName, 
			String businessStreet, String businessCity, int productPrice, int category, String influencerName, String advertisementText) {
		setProductAdvertisementCollection(mc);
		insertInfluencerProductAdvertisement(id, productName, businessName, businessStreet, businessCity, productPrice, category, influencerName, advertisementText);
	}
	
	public void driver_updateProduct(MongoCollection<ProductAdvertisement> mc, String productName, String businessName, Product product) {
		setProductAdvertisementCollection(mc);
		product.setPrice(42);
		product.setProductName("changedName");
		product.setCategory(0); 
		Business b = new Business();
		Address a = new Address();
		a.setAddressCity("newCity");
		a.setAddressStreet("newStreet");
		b.setAddress(a);
		b.setBusinessName("newBusiness");
		product.setOwnerBusiness(b);
		updateProduct(productName, businessName, product);
	}
	
	public void driver_changeInfluencerMetadataText(MongoCollection<ProductAdvertisement> mc, String productName, String userName, String newText) {
		setProductAdvertisementCollection(mc);
		changeInfluencerMetadataText(productName, userName, newText);
	}
	
	public List<ProductAdvertisement> driver_getInfluencerAdvertisements(MongoCollection<ProductAdvertisement> mc, String influencerUserName) {
		setProductAdvertisementCollection(mc);
		return getInfluencerAdvertisements(influencerUserName);
	}
	
	public List<ProductAdvertisement> driver_getBusinessAdvertisements(MongoCollection<ProductAdvertisement> mc, String businessName) {
		setProductAdvertisementCollection(mc);
		return getBusinessAdvertisements(businessName);
	}
	
	public List<ProductAdvertisement> driver_getBusinessAdvertisementsWithPriceGTE(MongoCollection<ProductAdvertisement> mc, String businessName, int price) {
		setProductAdvertisementCollection(mc);
		return getBusinessAdvertisementsWithPriceGTE(businessName, price);
	}
	
	public void driver_removeAdvertisement(MongoCollection<ProductAdvertisement> mc, int id) {
		setProductAdvertisementCollection(mc);
		removeAdvertisement(id);
	}
	
	public long driver_removeBusinessAdvertisements(MongoCollection<ProductAdvertisement> mc, String businessName) {
		setProductAdvertisementCollection(mc);
		return removeBusinessAdvertisements(businessName);
	}
	
	public long driver_countInfluencerAdvertisementsForBusiness(MongoCollection<ProductAdvertisement> mc, String businessName) {
		setProductAdvertisementCollection(mc);
		return countInfluencerAdvertisementsForBusiness(businessName);
	}
	
	public ProductAdvertisement driver_getCheapestAd(MongoCollection<ProductAdvertisement> mc, String productName) {
		setProductAdvertisementCollection(mc);
		return getCheapestAd(productName);
	}

}
