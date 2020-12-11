package org.bson.codecs.pojo;

public class PojoCodecProvider { // TODO Potentially add more functionality
	// Hendrik Winkelmann: Overrode in class path so that during symbolic execution this simple mock is used.

	public PojoCodecProvider() {
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		public Builder automatic(boolean b) {
			return this;
		}
		
		public PojoCodecProvider build() {
			return new PojoCodecProvider();
		}
	}
}
