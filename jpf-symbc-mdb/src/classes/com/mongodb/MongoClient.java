package com.mongodb;

import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.client.MongoDatabase;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoDatabase;

public class MongoClient {// TODO Potentially add more functionality
	// Hendrik Winkelmann: Overrode in class path so that during symbolic execution this simple mock is used.
	public MongoClient(String host, MongoClientOptions options) {
		
	}
	
	public static CodecRegistry getDefaultCodecRegistry() {
		return new CodecRegistry();
	}
	
	public MongoDatabase getDatabase(String name) {
		return new MMongoDatabase(name);
	}
}
