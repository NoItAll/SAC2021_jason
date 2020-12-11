package gov.nasa.jpf.symbc.mdb.mongo_mock.client.model;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Set;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.MongoWriteException;
import com.mongodb.lang.Nullable;

import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;

public class MUpdates {	
	protected static final String setOp = "$set";
	protected static final String unsetOp = "$unset";
	protected static final String setOnInsertOp = "$setOnInsert";
	protected static final String renameOp = "$rename";
	protected static final String incOp = "$inc";
	protected static final String mulOp = "$mul";
	protected static final String minOp = "$min";
	protected static final String maxOp = "$max";
	protected static final String currentDateOp = "$currentDate";
	
	protected MUpdates() {}
	
	public static MUpdate toUpdate(Document document) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public static Bson combine(final Bson... updates) {
        return combine(asList(updates));
    }
	
	public static Bson combine(final List<? extends Bson> updates) {
		return new CompositeUpdate(updates);
    }
	
	public static <T> Bson set(final String fieldName, @Nullable final T value) {
		return new SimpleUpdate(fieldName, value, setOp);
    }
	
	public static Bson unset(final String fieldName) {
		return new SimpleUpdate(fieldName, null, unsetOp);
    }
	
	public static Bson setOnInsert(final Bson value) {
		// TODO Only set if upsert
		return new SimpleUpdate("", value, setOnInsertOp); // Is SimpleBsonKeyValue in original
    }
	
	public static <TItem> Bson setOnInsert(final String fieldName, @Nullable final TItem value) {
		// TODO only set if upsert
		return new SimpleUpdate(fieldName, value, setOnInsertOp);
    }
	
	public static Bson rename(final String fieldName, final String newFieldName) {
		return new SimpleUpdate(fieldName, newFieldName, renameOp);
    }
	
	public static Bson inc(final String fieldName, final Number number) {
		return new SimpleUpdate(fieldName, number, incOp);
    }

	public static Bson mul(final String fieldName, final Number number) {
		return new SimpleUpdate(fieldName, number, mulOp);
    }
	
	public static <TItem> Bson min(final String fieldName, final TItem value) {
		return new SimpleUpdate(fieldName, value, minOp);
    }
	
	public static <TItem> Bson max(final String fieldName, final TItem value) {
		return new SimpleUpdate(fieldName, value, maxOp);
    }
	
	public static Bson currentDate(final String fieldName) {
		return new SimpleUpdate(fieldName, "date", currentDateOp); // TODO
    }
	
	public static Bson currentTimestamp(final String fieldName) {
		return new SimpleUpdate(fieldName, "timestamp", currentDateOp); // TODO
    }
	
    public interface MUpdate extends Bson {
    	
    	boolean update(MDocument toUpdate);
    	
    	@Override
		default <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
			throw new UnsupportedOperationException("The mock-update"
					+ " should not be transformed to a BsonDocument.");
		}
    }
    
    protected static class SimpleUpdate implements MUpdate {
    	private final String fieldName;
    	private final Object value;
    	private final String operator;
    	protected final String[] fieldNames;
    	
    	public SimpleUpdate(String fieldName, Object value, String operator) {
    		String[] fieldNames = fieldName.split("\\.");
			this.fieldName = fieldName;
			this.fieldNames = fieldNames.length > 1 ? fieldNames : null;
    		this.operator = operator;
    		this.value = Utility.transformToNumberStringMObjectIdOrMDocument(value);
    	}
    	
    	@Override
    	public boolean update(MDocument toUpdate) {
    		boolean mdocChanged = false;
			toUpdate.setIsUpdated();
    		if (operator.equals(setOp)) {
    			Object previousValue;
    			if (fieldNames == null) {
    				previousValue = toUpdate.put(fieldName, value);
    			} else {
    				MDocument current = toUpdate;
    				Object currentVal;
    				for (int i = 0; i < fieldNames.length - 1; i++) {
    					currentVal = current.get(fieldNames[i]);
    					if (currentVal instanceof MDocument) {
    						current = (MDocument) currentVal;
    					} else if (currentVal == null) {
    						currentVal = new MDocument();
    						current.put(fieldNames[i], currentVal);
    						current = (MDocument) currentVal;
    					} else {
    						throw new MongoWriteException("Cannot create field {" + fieldNames[i + 1] + ": " + value + "}");
    					}
    				}
    				previousValue = current.put(fieldNames[fieldNames.length - 1], value);
    			}
    			if (previousValue == null) {
    				mdocChanged = value != null;
    			} else {
    				mdocChanged = !previousValue.equals(value);
    			}
    		} else if (operator.equals(unsetOp)) {
    			MDocument current = toUpdate;
				Object currentVal;
				for (int i = 0; i < fieldNames.length - 1; i++) {
					currentVal = current.get(fieldNames[i]);
					if (currentVal instanceof MDocument) {
						current = (MDocument) currentVal;
					} else {
						return false;
					}
				}
  
    			Set<String> keys = current.keySet();
    			mdocChanged = keys.contains(fieldNames[fieldNames.length - 1]);
    			if (mdocChanged) {
    				current.remove(fieldNames[fieldNames.length - 1]);
    			}
    		} else if (operator.equals(setOnInsertOp)) {
    			throw new UnsupportedOperationException("Operator " + operator + " is not yet implemented");
    		} else if (operator.equals(renameOp)) {
    			throw new UnsupportedOperationException("Operator " + operator + " is not yet implemented");
    		} else if (operator.equals(incOp)) {
    			Object val = toUpdate.get(fieldName);
    			if (val instanceof Number) {
    				mdocChanged = true;
    				Number temp = (Number) val;
    				// Avoid cast of wrapped value due to JPF-symbc-issue with casting
    				if (temp instanceof Double) {
    					temp = temp.doubleValue() + 1.0;
    				} else if (temp instanceof Integer) {
    					temp = temp.intValue() + 1;
    				} else if (temp instanceof Float){
    					temp = temp.floatValue() + (float) 1.0;
    				} else if (temp instanceof Short) {
    					temp = temp.shortValue() + (short) 1;
    				} else if (temp instanceof Byte) {
    					temp = temp.byteValue() + (byte) 1;
    				} else {
    					assert false;
    				}
    				toUpdate.put(fieldName, temp);
    			} else {
    				mdocChanged = false;
    				// TODO Error?
    			}
    		} else if (operator.equals(mulOp)) {
    			throw new UnsupportedOperationException("Operator " + operator + " is not yet implemented");
    		} else if (operator.equals(minOp)) {
    			throw new UnsupportedOperationException("Operator " + operator + " is not yet implemented");
    		} else if (operator.equals(maxOp)) {
    			throw new UnsupportedOperationException("Operator " + operator + " is not yet implemented");
    		} else if (operator.equals(currentDateOp)) {
    			throw new UnsupportedOperationException("Operator " + operator + " is not yet implemented");
    		} else {
    			throw new UnsupportedOperationException("Operator " + operator + " is not implemented");
    		}
    		return mdocChanged;
    	}
    			
    }
    
    protected static class CompositeUpdate implements MUpdate {
    	protected final List<? extends Bson> updates;
    	
    	public CompositeUpdate(List<? extends Bson> updates) {
    		this.updates = updates;
    	}
    	
    	@Override
    	public boolean update(MDocument toUpdate) {
    		boolean mdocChanged = false;
    		for (Bson update : updates ) {
    			if (update instanceof MUpdate) {
    				boolean newUpdate = ((MUpdate) update).update(toUpdate);
    				mdocChanged = mdocChanged ? mdocChanged : newUpdate;
    			} else {
    				throw new UnsupportedOperationException("Not yet implemented."); // TODO
    			}
    		}
    		return mdocChanged;
    		
    	}
    }
    
}
