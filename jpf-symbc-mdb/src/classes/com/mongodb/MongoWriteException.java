package com.mongodb;

public class MongoWriteException extends RuntimeException {
	// Hendrik Winkelmann: Overrode in class path so that during symbolic execution this simple mock is used.

	public MongoWriteException(String msg) {
		super(msg);
	}

}
