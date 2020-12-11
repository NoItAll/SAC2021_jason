package org.bson.types;

import java.io.Serializable;
import java.util.Date;

import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;


// Hendrik Winkelmann: Overrode in class path so that during symbolic execution this simple mock is used.

// Model class for ObjectId. Needed to avoid the static initializer block using SecureRandom and for consistency with MObjectId
// Taken from the original implementation https://github.com/mongodb/mongo-java-driver/blob/master/bson/src/main/org/bson/types/ObjectId.java and adapted to 
// regard the initialization-process of the MObjectId-class; Furthermore, "difficult" functionality like the SecureRandom-usage is removed.
public final class ObjectId implements Comparable<ObjectId>, Serializable {

    private static final long serialVersionUID = 3670079982654483072L;

    private int timestamp = 0;
    private int counter;

    /**
     * Gets a new object id.
     *
     * @return the new id
     */
    public static ObjectId get() {
        return new ObjectId();
    }

    /**
     * Checks if a string could be an {@code ObjectId}.
     *
     * @param hexString a potential ObjectId as a String.
     * @return whether the string could be an object id
     * @throws IllegalArgumentException if hexString is null
     */
    public static boolean isValid(final String hexString) {
        if (hexString == null) {
            throw new IllegalArgumentException();
        }

        int len = hexString.length();
        if (len != 24) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            char c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Create a new object id.
     */
    public ObjectId() {
    	counter = MObjectId.NEXT_COUNTER;
        MObjectId.NEXT_COUNTER++;
    }

    /**
     * Constructs a new instance using the given date.
     *
     * @param date the date
     */
    @Deprecated
    public ObjectId(final Date date) {
        this(); // Dummy
    }

    /**
     * Creates an ObjectId using the given counter. The other arguments are not used and solely for there for consistency.
     */
    @Deprecated
    public ObjectId(final Date date, final int counter) {
        this(counter);
    }

    public ObjectId(final int counter) {
    	this.counter = counter;
    	checkNC();
    }
    
    /**
     * Creates an ObjectId using the given counter. The other arguments are not used and solely for there for consistency.
     */
    @Deprecated
    public ObjectId(final Date date, final int machineIdentifier, final short processIdentifier, final int counter) {
        this(counter); // Dummy
    }

    /**
     * Creates an ObjectId using the given counter. The other arguments are not used and solely for there for consistency.
     */
    @Deprecated
    public ObjectId(final int timestamp, final int machineIdentifier, final short processIdentifier, final int counter) {
        this(counter);
    }

    /**
     * Creates an ObjectId using the given time, machine identifier, process identifier, and counter.
     *
     * @param timestamp         the time in seconds
     * @param counter           the counter
     * @throws IllegalArgumentException if the high order byte of counter is not zero
     */
    public ObjectId(final int timestamp, final int counter) {
        this(counter);
    }

    /**
     * Constructs a new instance from a 24-byte hexadecimal string representation.
     *
     * @param hexString the string to convert
     * @throws IllegalArgumentException if the string is not a valid hex string representation of an ObjectId
     */
    public ObjectId(final String hexString) {
    	counter = Integer.parseInt(hexString, 16);
    	checkNC();
    }

    protected void checkNC() {
    	if (counter == Integer.MAX_VALUE) {
			throw new RuntimeException("Illegal state: counter would overflow."); 
		}
    	if (counter >= MObjectId.NEXT_COUNTER) {
    		MObjectId.NEXT_COUNTER = counter + 1;
    	}
    }

    /**
     * Gets the timestamp (number of seconds since the Unix epoch).
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the timestamp as a {@code Date} instance.
     *
     * @return the Date
     */
    public Date getDate() {
        return new Date((timestamp & 0xFFFFFFFFL) * 1000L);
    }

    /**
     * Converts this instance into a 24-byte hexadecimal string representation.
     *
     * @return a string representation of the ObjectId in hexadecimal format
     */
    public String toHexString() {
        return Integer.toHexString(getCounter());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectId objectId = (ObjectId) o;

        if (counter != objectId.counter) {
            return false;
        }
        if (timestamp != objectId.timestamp) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp;
        result = 31 * result + counter;
        return result;
    }

    @Override
	public int compareTo(ObjectId o) {
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
		return "ObjectId{counter=" + counter + "}";
	}


    /**
     * Gets the counter.
     *
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }
    
    public MObjectId toMObjectId() {
    	return new MObjectId(counter);
    }

}