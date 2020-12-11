package gov.nasa.jpf.symbc.mdb.symbolic_collection;

import org.bson.types.ObjectId;

public final class MObjectId implements Comparable<MObjectId> { // TODO Possibly remove in favor of simple int
	/// TODO Simplify, also regard bytecode level
	private int timestamp = 0;
    private int machineIdentifier = 0;
    private short processIdentifier = 0;
    private int counter;
    
    public static int NEXT_COUNTER = 0;
    
    public MObjectId() {
    	counter = NEXT_COUNTER;
    	checkNC();
    }
    
    public MObjectId(String hexString) {
    	isValid(hexString);
    	counter = Integer.parseInt(hexString, 16);
    	checkNC();
    }
    
    public MObjectId(int counter) {
    	this.counter = counter;
    	checkNC();
    }
    
    protected void checkNC() {
    	if (counter == Integer.MAX_VALUE) {
			throw new RuntimeException("Illegal state: counter would overflow."); 
		}
    	if (counter >= NEXT_COUNTER) {
    		NEXT_COUNTER = counter + 1;
    	}
    }
        
    public static boolean isValid(String hexString) {
    	return ObjectId.isValid(hexString);
    }
    
    public static MObjectId fromObjectId(ObjectId objid) {
    	return new MObjectId(objid.getCounter());
    }

    @Override
    public boolean equals(Object o) {
    	if (o == null || !(o instanceof MObjectId)) {
    		return false;
    	} else {
    		MObjectId temp = (MObjectId) o;
    		return temp.counter == this.counter && temp.timestamp == this.timestamp;
    	}
    }
    
    public MObjectId copy() {
    	return new MObjectId(counter);
    }

	public void setTimestamp(int i) {
		this.timestamp = i;
	}
	
	public void setMachineIdentifier(int i) {
		this.machineIdentifier = i;
	}
	
	public void setProcessIdentifier(short i) {
		this.processIdentifier = i;
	}
	
	public void setCounter(int i) {
		this.counter = i;
	}
	
	public int getCounter() {
		return counter;
	}
	
	@Override
	public int compareTo(MObjectId o) {
		if (timestamp < o.timestamp) {
			return -1;
		}
		if (timestamp > o.timestamp) {
			return 1;
		}
		if (counter < o.counter) {
			return -1;
		}
		if (counter > o.counter) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return "MObjectId{counter=" + counter + "}";
	}
	
	public static String toHexString(MObjectId id) {
		return toHexString(id.counter);
	}
	
	public static String toHexString(int counter) {
		String hexString = Integer.toHexString(counter);
		while (hexString.length() < 24) {
			hexString = '0' + hexString;
		}
		assert ObjectId.isValid(hexString);
		return hexString;
	}
	
	public ObjectId toObjectId() { /// TODO Transform function
		return new ObjectId(toHexString(this));
	}
}
