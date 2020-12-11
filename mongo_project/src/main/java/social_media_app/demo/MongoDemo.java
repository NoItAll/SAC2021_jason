package social_media_app.demo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import social_media_app.entity.ProductAdvertisement;

// Not part of the service classes but used in the paper as a demo to explain the different path conditions.
public class MongoDemo {
	
	public int demo(MongoCollection<ProductAdvertisement> mc, int i) {
		// Condition type 1:
		if (i < 0) {
			i = -i;
		}
		MongoCursor<ProductAdvertisement> mcpd = mc.find(
		    // Condition type 2:
		    Filters.gt("product.price", i)).cursor();
		int count = 0;
		// Condition type 3:
		while (mcpd.hasNext()) {
			ProductAdvertisement pa = mcpd.next();
			// Condition type 4:
			if (pa.getProduct().getPrice() > 200) {
				count++;
			}
		}
		return count;
	}
}
