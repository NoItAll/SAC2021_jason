package com.mongodb;

import org.bson.codecs.configuration.CodecRegistry;

public class MongoClientOptions {// TODO Potentially add more functionality
	
	// Hendrik Winkelmann: Overrode in class path so that during symbolic execution this simple mock is used.
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		public Builder codecRegistry(CodecRegistry cr) {
			return this; 
		}
		
		public MongoClientOptions build() {
			return new MongoClientOptions();
		}
	}
	
}
