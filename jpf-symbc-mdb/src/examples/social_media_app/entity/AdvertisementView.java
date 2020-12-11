package social_media_app.entity;

import java.util.Map;

public class AdvertisementView {
	
	protected Map<String, String> viewMetadata;
	
	public String get(String key) {
		return viewMetadata.get(key);
	}

	public Map<String, String> getViewMetadata() {
		return viewMetadata;
	}

	public void setViewMetadata(Map<String, String> viewMetadata) {
		this.viewMetadata = viewMetadata;
	}
}
