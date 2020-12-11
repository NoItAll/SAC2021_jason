package org.bson.codecs.configuration;

import org.bson.codecs.pojo.PojoCodecProvider;

public class CodecRegistries { // TODO Potentially add more functionality
	// Hendrik Winkelmann: Overrode in class path so that during symbolic execution this simple mock is used.
	public static CodecRegistry fromRegistries(CodecRegistry... codecRegistries) {
		return null;
	}
	
	public static CodecRegistry fromProviders(PojoCodecProvider pojoCodecProvider) {
		return null;
	}

}