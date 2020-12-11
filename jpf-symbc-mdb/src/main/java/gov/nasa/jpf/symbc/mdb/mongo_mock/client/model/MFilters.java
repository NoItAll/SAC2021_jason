package gov.nasa.jpf.symbc.mdb.mongo_mock.client.model;

import java.util.ArrayList;

import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument.mField;

public class MFilters {
	
	protected MFilters() {}
	
	/* TODO: 
	 * https://mongodb.github.io/mongo-java-driver/3.12/driver/tutorials/perform-read-operations/
	 collection.find(
    	new Document("stars", new Document("$gte", 2) // String value
          .append("$lt", 5))
          .append("categories", "Bakery")).forEach(printBlock);
	 */

	public static TrueFilter alwaysTrue() {
		return new TrueFilter();
	}
	
	public static <T> MFilter eq(final T value) {
		return eq("_id", value);
	}
	
	public static <T> MFilter eq(final String fieldName, final T value) {
		return new EqualityFilter(fieldName, value, null, null, false);
	}
	
	public static <T> MFilter ne(final String fieldName, final T value) {
		return new EqualityFilter(fieldName, value, null, null, true);
	}
	
	public static <T> MFilter gt(final String fieldName, final T value) {
		return new GTFilter(fieldName, value, null, null);
	}
	
	public static <T> MFilter lt(final String fieldName, final T value) {
		return new LTFilter(fieldName, value, null, null);
	}
	
	public static <T> MFilter gte(final String fieldName, final T value) {
		GTFilter gtf = new GTFilter(fieldName, value, null, null);
		return new EqualityFilter(fieldName, value, gtf, null, false);
	}
	
	public static <T> MFilter lte(final String fieldName, final T value) {
		LTFilter ltf = new LTFilter(fieldName, value, null, null);
		return new EqualityFilter(fieldName, value, ltf, null, false);
	}
	
	protected static <T> MFilter createInFilter(String fieldName, T[] values, boolean negate) {
		EqualityFilter[] eqFilters = new EqualityFilter[values.length];
		for (int i = 0; i < eqFilters.length; i++) {
			eqFilters[i] = new EqualityFilter(fieldName, values[i], null, null, negate);
		}
		return or(eqFilters);
	}
	
	public static <T> MFilter in(final String fieldName, T... values) {
		return createInFilter(fieldName, values, false);
	}
	
	public static <T> MFilter nin(final String fieldName, final T... values) {
		return createInFilter(fieldName, values, true);
	}
	
	protected static <T> ArrayList<T> newArrayList(Iterable<T> values) {
		ArrayList<T> result = new ArrayList<>();
		for (T value : values) {
			result.add(value);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> MFilter in(final String fieldName, final Iterable<T> values) {
		return createInFilter(fieldName, (T[]) newArrayList(values).toArray(new Object[0]), false);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> MFilter nin(final String fieldName, final Iterable<T> values) {
		return createInFilter(fieldName, (T[]) newArrayList(values).toArray(new Object[0]), true);
	}
	
	public static <T> MFilter and(final Iterable<Bson> filters) {
		return _and((Iterable<MFilter>) (Object) filters);
	}
	
	public static <T> MFilter and(final Bson... filters) {
		MFilter[] castArray = new MFilter[filters.length];
		for (int i = 0; i < filters.length; i++) {
			castArray[i] = (MFilter) filters[i];
		}
		return _and(castArray);
	}
	
	protected static <T> MFilter _and(final Iterable<MFilter> filters) {
		return and(newArrayList(filters).toArray(new MFilter[0]));
	}
	
	protected static <T> MFilter _and(final MFilter... filters) {
		return and(filters);
	}
	
	public static <T> MFilter and(final MFilter... filters) {
		assert filters != null && filters.length > 0;
		MFilter firstFilter = filters[0].copy();
		MFilter setAndForFilter = firstFilter;
		MFilter newFilterCopy;
		for (int i = 1; i < filters.length; i++) {
			newFilterCopy = filters[i].copy();
			setAndForFilter.setAnd(newFilterCopy);
			setAndForFilter = newFilterCopy;
		}
		return firstFilter;
	}

	public static <T> MFilter or(final Iterable<Bson> filters) {
		return _or((Iterable<MFilter>) (Object) filters);
	}
	
	public static <T> MFilter or(final Bson... filters) {
		MFilter[] castArray = new MFilter[filters.length];
		for (int i = 0; i < filters.length; i++) {
			castArray[i] = (MFilter) filters[i];
		}
		return _or(castArray);
	}
	
	protected static <T> MFilter _or(final Iterable<MFilter> filters) {
		return or(newArrayList(filters).toArray(new MFilter[0]));
	}
	
	protected static <T> MFilter _or(final MFilter... filters) {
		return or(filters);
	}
	
	
	public static <T> MFilter or(final MFilter... filters) {
		assert filters != null && filters.length > 0;
		MFilter firstFilter = filters[0].copy();
		MFilter setOrForFilter = firstFilter;
		MFilter newFilterCopy;
		for (int i = 1; i < filters.length; i++) {
			newFilterCopy = filters[i].copy();
			setOrForFilter.setOr(newFilterCopy);
			setOrForFilter = newFilterCopy;
		}
		return firstFilter;
	}
	
	public static MFilter exists(final String fieldName) {
		return exists(fieldName, true);
	}
	
	public static MFilter exists(final String fieldName, final boolean exists) {
		// !exists because we check whether to invert the result: 
		return new ExistsFilter(fieldName, null, null, !exists); 
	}
	
	public static MFilter nor(final Iterable<Bson> filters) {
		return nor(newArrayList(filters).toArray(new MFilter[0]));
	}
	
	public static MFilter nor(final Bson... filters) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	/* FILTER CLASSES */
	
	public abstract static class MFilter implements Bson { // For possible injection.
		protected final String fieldName;
		protected final String[] fieldNames; // Is not null if fieldName contains a '.'
		protected final Object val;
		protected MFilter or;
		protected MFilter and;
		
		protected MFilter(
				String fieldName, 
				Object value, 
				MFilter or, 
				MFilter and) {
			String[] fieldNames = fieldName.split("\\.");
			this.fieldName = fieldName;
			this.fieldNames = fieldNames.length > 1 ? fieldNames : null;
			this.val = Utility.transformToNumberStringMObjectIdOrMDocument(value);
			this.or = or;
			this.and = and;
			assert val instanceof MDocument || 
				val instanceof Number || 
				val instanceof String ||
				val instanceof MObjectId ||
				val == null;
		}
		
		
		public boolean filter(MDocument toCheck) {
			assert toCheck != null;
			Object toCheckVal;
			if (fieldNames == null) {
				toCheckVal = toCheck.get(fieldName);
			} else {
				MDocument current = toCheck;
				Object currentVal;
				for (int i = 0; i < fieldNames.length - 1; i++) {
					currentVal = current.get(fieldNames[i]);
					if (currentVal instanceof MDocument) {
						current = (MDocument) currentVal;
					} else {
						return false;
					}
				}
				toCheckVal = current.get(fieldNames[fieldNames.length - 1]);
			}
			if (this.passesFilter(toCheckVal)) {
				/* If the documents's value passes this filter, 
				 * we must check if there is an and-condition. 
				 * If there is an and-condition, this must pass as well.
				 */
				return and != null ? and.filter(toCheck) : true;
			} else {
				/* If the document does not pass the current filter ,
				 * we see if there are alternatives (or-conditions) which pass the check.
				 */
				boolean alternativePasses = or != null ? or.filter(toCheck) : false;
				if (alternativePasses) {
					return and != null ? and.filter(toCheck) : true;
				} else {
					return false;
				}
			}
		}
		
		protected void setAnd(MFilter mf) {
			this.and = mf;
		}
		
		protected void setOr(MFilter mf) {
			this.or = mf;
		}
		
		protected abstract boolean passesFilter(Object toCheckVal);
		
		protected abstract MFilter copy();
		
		@Override
		public String toString() {
			return this.getClass().getSimpleName()+
					"{fieldName="+fieldName+",val="+val+",and="+and+",or="+or+"}";
		}
		
		
		@Override
		public <TDocument> BsonDocument toBsonDocument(
				Class<TDocument> documentClass, 
				CodecRegistry codecRegistry) {
			throw new UnsupportedOperationException("The mock-filter"
					+ " should not be transformed to a BsonDocument.");
		}		
	}
	
	protected static class TrueFilter extends MFilter {
		protected TrueFilter() {
			super("", new MDocument(null), null, null);
		}
		
		public boolean filter(MDocument toCheck) {
			return true;
		}
		
		protected boolean passesFilter(Object toCheckVal) {
			return true;
		}
		
		protected TrueFilter copy() {
			return this;
		}
	}
	
	protected static class EqualityFilter extends MFilter {
		protected boolean invert;
		
		protected EqualityFilter(
				String fieldName, 
				Object value, 
				MFilter or, 
				MFilter and, 
				boolean invert) {
			super(fieldName, value, or, and);
			this.invert = invert;
		}
		
		protected boolean passesFilter(Object toCheckVal) {
			if (toCheckVal == null) {
				return val == null ? true : false;
			}
			
			if (val == null) {
				return false;
			}
			boolean isEqual = val.equals(toCheckVal);
			return ((!invert && isEqual) || (invert && !isEqual));
		}
		
		protected EqualityFilter copy() {
			return new EqualityFilter(
					fieldName, 
					val, 
					or != null ? or.copy() : null, 
					and != null ? and.copy() : null, 
					invert
			);
		}
	}
	
	protected static class LTFilter extends MFilter {
		public LTFilter(
				String fieldName, 
				Object value, 
				MFilter or, 
				MFilter and) {
			super(fieldName, value, or, and);
			assert !(val instanceof String) && !(val instanceof MObjectId) : "$lt has not been implemented for Strings and MObjectId yet.";
		}
		protected LTFilter copy() {
			return new LTFilter(
					fieldName, 
					val, 
					or != null ? or.copy() : null, 
					and != null ? and.copy() : null
			);
		}
		protected boolean passesFilter(Object toCheckVal) {
			if (toCheckVal == null) {
				return val == null ? true : false;
			}
			
			if (val == null) {
				return false;
			}
			
			// Compare objects
			if (!(val instanceof Number)) {
				if (toCheckVal instanceof Number) {
					return false;
				}
				assert toCheckVal instanceof MDocument;
				return isLT((MDocument) toCheckVal, (MDocument) val);
			}
			
			if (!(toCheckVal instanceof Number)) {
				return false;
			}
			Number valn = (Number) val;
			Number objn = (Number) toCheckVal;
			return numberIsLT(objn, valn);
		}
	}
	
	protected static class GTFilter extends MFilter {
		public GTFilter(
				String fieldName, 
				Object value, 
				MFilter or, 
				MFilter and) {
			super(fieldName, value, or, and);
			assert !(val instanceof String) && !(val instanceof MObjectId) : "$gt has not been implemented for Strings and MObjectId yet.";
		}
		protected GTFilter copy() {
			return new GTFilter(
					fieldName, 
					val, 
					or != null ? or.copy() : null, 
					and != null ? and.copy() : null
			);
		}
		protected boolean passesFilter(Object toCheckVal) {
			if (toCheckVal == null) {
				return val == null ? true : false;
			}
			if (val == null) {
				return false;
			}
			// Compare objects
			if (!(val instanceof Number)) {
				if (!(toCheckVal instanceof MDocument)) {
					return false;
				}
				assert toCheckVal instanceof MDocument;
				return isGT((MDocument) toCheckVal, (MDocument) val);
			}
			if (!(toCheckVal instanceof Number)) {
				return false;
			}
			Number valn = (Number) val;
			Number objn = (Number) toCheckVal;
			return numberIsGT(objn, valn);
		}
	}
	
	protected static class ExistsFilter extends MFilter {
		protected final boolean invert;
		public ExistsFilter(String fieldName, MFilter or, MFilter and, boolean invert) {
			super(fieldName, new MDocument(null), or, and);
			this.invert = invert;
		}

		@Override
		public boolean filter(MDocument toCheck) {
			if (fieldNames != null) {
				MDocument current = toCheck;
				Object currentVal;
				for (int i = 0; i < fieldNames.length - 1; i++) {
					currentVal = current.get(fieldNames[i]);
					if (currentVal instanceof MDocument) {
						current = (MDocument) currentVal;
					} else {
						return !invert;
					}
				}
				boolean containsKey = current.containsKey(fieldNames[fieldNames.length - 1]);
				return invert && !containsKey || !invert && containsKey;
			} else {
				boolean containsKey = toCheck.containsKey(fieldName);
				return invert && !containsKey || !invert && containsKey;
			}
		}
		
		@Override
		protected boolean passesFilter(Object toCheckVal) {
			throw new UnsupportedOperationException("Should not be called for ExistsFilter");
		}

		@Override
		protected MFilter copy() {
			return new ExistsFilter(fieldName, or != null ? or : null, and != null ? and : null, invert);
		}
	}
	
	protected static boolean numberIsLT(Number n0, Number n1) {
		// This is done rather complex because Symbolic Pathfinder cannot deal
		// easily with casts on symbolic values. This procedure avoids casts
		if (n1 instanceof Double || n1 instanceof Float) {
			double dvaln = n1.doubleValue();
			if (n0 instanceof Double || n0 instanceof Float) {
				return n0.doubleValue() < dvaln;
			} else {
				return n0.intValue() < dvaln;
			}
		} else {
			int ivaln = n1.intValue();
			if (n0 instanceof Double || n0 instanceof Float) {
				return n0.doubleValue() < ivaln;
			} else {
				return n0.intValue() < ivaln;
			}
		}
	}
	
	protected static boolean numberIsGT(Number n0, Number n1) {
		// This is done rather complex because Symbolic Pathfinder cannot deal
		// easily with casts on symbolic values. This procedure avoids casts.
		if (n1 instanceof Double || n1 instanceof Float) {
			double dvaln = n1.doubleValue();
			if (n0 instanceof Double || n0 instanceof Float) {
				return n0.doubleValue() > dvaln;
			} else {
				return n0.intValue() > dvaln;
			}
		} else {
			int ivaln = n1.intValue();
			if (n0 instanceof Double || n0 instanceof Float) {
				return n0.doubleValue() > ivaln;
			} else {
				return n0.intValue() > ivaln;
			}
		}
	}	
	
	// Checks if toCheckMDoc is < than comparisonMDoc
	protected static boolean isLT(MDocument toCheckMDoc, MDocument comparisonMDoc) {		
		if (toCheckMDoc.equals(comparisonMDoc)) {
			return false;
		}
		
		mField[] comparisonFields = comparisonMDoc.getFields();
		mField[] toCheckFields = toCheckMDoc.getFields();
		
		for (int i = 0; i < comparisonFields.length; i++) {
			mField cf = comparisonFields[i];
			if (i >= toCheckFields.length) 
				return true;
			mField tcf = toCheckFields[i];
			if (!cf.getKey().equals(tcf.getKey())) {
				return true;
			}
			if (cf.getVal() instanceof MDocument) {
				if (!(tcf.getVal() instanceof MDocument)) {
					return false;
				}
				if (!isLT((MDocument) tcf.getVal(), (MDocument) cf.getVal())) {
					return false;
				}
				continue;
			}
			
			if (cf.getVal() instanceof Number) {
				if (!(tcf.getVal() instanceof Number)) {
					return false;
				}
				Number tcfn = (Number) tcf.getVal();
				Number cfn = (Number) cf.getVal();
				if (numberIsLT(tcfn, cfn)) {
					return true;
				} else if (numberIsGT(tcfn, cfn)) {
					return false;
				} else {
					continue;
				}
			}
		}
		
		if (toCheckMDoc.size() < comparisonMDoc.size()) {
			return true;
		} else if (toCheckMDoc.size() > comparisonMDoc.size()) {
			return false;
		}
		return false;
	}
	
	// Check if toCheckMDoc is > than comparisonMDoc
	protected static boolean isGT(MDocument toCheckMDoc, MDocument comparisonMDoc) {
		if (toCheckMDoc.equals(comparisonMDoc)) {
			return false;
		}
		
		mField[] comparisonFields = comparisonMDoc.getFields();
		mField[] toCheckFields = toCheckMDoc.getFields();
		
		for (int i = 0; i < comparisonFields.length; i++) {
			mField cf = comparisonFields[i];
			if (i >= toCheckFields.length) 
				return false;
			mField tcf = toCheckFields[i];
			if (!cf.getKey().equals(tcf.getKey())) {
				return false;
			}
			if (cf.getVal() instanceof MDocument) {
				if (!(tcf.getVal() instanceof MDocument)) {
					return false;
				}
				if (!isGT((MDocument) tcf.getVal(), (MDocument) cf.getVal())) {
					return false;
				}
				continue;
			}
			
			if (cf.getVal() instanceof Number) {
				if (!(tcf.getVal() instanceof Number)) {
					return false;
				}
				Number tcfn = (Number) tcf.getVal();
				Number cfn = (Number) cf.getVal();
				if (numberIsGT(tcfn, cfn)) {
					return true;
				} else if (numberIsLT(tcfn, cfn)) {
					return false;
				} else {
					continue;
				}
			}
		}
		
		if (toCheckMDoc.size() < comparisonMDoc.size()) {
			return false;
		} else if (toCheckMDoc.size() > comparisonMDoc.size()) {
			return true;
		}
		return false;
	}
	
}