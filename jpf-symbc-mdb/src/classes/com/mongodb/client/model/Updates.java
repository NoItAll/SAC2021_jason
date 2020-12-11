package com.mongodb.client.model;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.model.MUpdates;

public class Updates extends MUpdates {
	// Hendrik Winkelmann: Overrode in class path so that during symbolic execution, MFilters is used.
	private Updates() {}
	
}
