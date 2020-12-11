package gov.nasa.jpf.symbc.mdb;

import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MCollection.mData;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MDocument.mField;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.FieldInfo;

public class MDBClassInfoElementInfoUtility {
	public static ClassInfo mDataCi;
	public static ClassInfo mFieldCi;
	public static ClassInfo MDocumentCi;
	public static ClassInfo MCollectionCi;
	public static ClassInfo MObjectIdCi;
	public static ClassInfo IntegerCi;
	public static FieldInfo enclosingMCollectionFi;
	public static FieldInfo depthFi;
	public static FieldInfo fieldsFi;
	public static FieldInfo isInsertedFi;
	public static FieldInfo isDeletedFi;
	public static FieldInfo isUpdatedFi;
	public static FieldInfo originalVersionFi;
	public static FieldInfo schemaVersionFi;
	public static FieldInfo containingMCollectionFi;
	public static FieldInfo curFi;
	public static FieldInfo tailFi;
	public static FieldInfo dataFi;
	public static FieldInfo pojoNameFi;
	public static FieldInfo collectionNameFi;
	public static FieldInfo maxNumberInitialDocumentsFi;
	public static FieldInfo _idFi;
	public static FieldInfo keyFi;
	public static FieldInfo valFi;
	public static FieldInfo timestampFi;
	public static FieldInfo machineIdentifierFi;
	public static FieldInfo processIdentifierFi; 
	public static FieldInfo counterFi;
	public static FieldInfo staticNextCounterFi;
	public static FieldInfo integerValueFi; // For Java's Integer-class
	public static FieldInfo entityClassFi;
	public static FieldInfo nameFi;
	public static FieldInfo enclosingMDocumentFi;
	
	private static FieldInfo getEnclosingFieldInfo(ClassInfo ci) {
		for (FieldInfo fi : ci.getDeclaredInstanceFields()) { // Adapted from from DynamicElementInfo#getEnclosingElementInfo()
			if (fi.getName().startsWith("this$")){
				return fi;
		    }
		}
		return null;
	}
	public static void initialize() {
		mDataCi = ClassLoaderInfo.getSystemResolvedClassInfo(mData.class.getName());//.getCurrentResolvedClassInfo(mData.class.getName());
		assert mDataCi != null;
		curFi = mDataCi.getDeclaredInstanceField("cur");
		tailFi = mDataCi.getDeclaredInstanceField("tail");
		depthFi = mDataCi.getDeclaredInstanceField("depth");
		enclosingMCollectionFi = getEnclosingFieldInfo(mDataCi);
		assert curFi != null;
		assert tailFi != null;
		assert depthFi != null;
		assert enclosingMCollectionFi != null;
		
		MCollectionCi = ClassLoaderInfo.getSystemResolvedClassInfo(MCollection.class.getName());//.getCurrentResolvedClassInfo(MCollection.class.getName());
		assert MCollectionCi != null;
		dataFi = MCollectionCi.getDeclaredInstanceField("data");
		maxNumberInitialDocumentsFi = MCollectionCi.getDeclaredInstanceField("maxNumberInitialDocuments");
		entityClassFi = MCollectionCi.getDeclaredInstanceField("entityClass");
		nameFi = MCollectionCi.getDeclaredInstanceField("name");
		assert dataFi != null;
		assert maxNumberInitialDocumentsFi != null;
		assert entityClassFi != null;
		assert nameFi != null;
		
		MDocumentCi = ClassLoaderInfo.getSystemResolvedClassInfo(MDocument.class.getName());//.getCurrentResolvedClassInfo(MDocument.class.getName());
		assert MDocumentCi != null;
		_idFi = MDocumentCi.getDeclaredInstanceField("_id");
		fieldsFi = MDocumentCi.getDeclaredInstanceField("fields");
		collectionNameFi = MDocumentCi.getDeclaredInstanceField("collectionName");
		pojoNameFi = MDocumentCi.getDeclaredInstanceField("pojoName");
		isInsertedFi = MDocumentCi.getDeclaredInstanceField("isInserted");
		isUpdatedFi = MDocumentCi.getDeclaredInstanceField("isUpdated");
		isDeletedFi = MDocumentCi.getDeclaredInstanceField("isDeleted");
		originalVersionFi = MDocumentCi.getDeclaredInstanceField("originalVersion");
		schemaVersionFi = MDocumentCi.getDeclaredInstanceField("schemaVersion");
		containingMCollectionFi = MDocumentCi.getDeclaredInstanceField("containingMCollection");
		assert _idFi != null;
		assert fieldsFi != null;
		assert collectionNameFi != null;
		assert pojoNameFi != null;
		assert isInsertedFi != null;
		assert isUpdatedFi != null;
		assert isDeletedFi != null;
		assert originalVersionFi != null;
		assert schemaVersionFi != null;
		assert containingMCollectionFi != null;
		
		mFieldCi = ClassLoaderInfo.getSystemResolvedClassInfo(mField.class.getName());//.getCurrentResolvedClassInfo(mField.class.getName());
		assert mFieldCi != null;
		keyFi = mFieldCi.getDeclaredInstanceField("key");
		valFi = mFieldCi.getDeclaredInstanceField("val");
		enclosingMDocumentFi = getEnclosingFieldInfo(mFieldCi);
		assert keyFi != null;
		assert valFi != null;
		assert enclosingMDocumentFi != null;
		
		MObjectIdCi = ClassLoaderInfo.getSystemResolvedClassInfo(MObjectId.class.getName());//.getCurrentResolvedClassInfo(MObjectId.class.getName());
		assert MObjectIdCi != null;
		timestampFi = MObjectIdCi.getDeclaredInstanceField("timestamp");
		machineIdentifierFi = MObjectIdCi.getDeclaredInstanceField("machineIdentifier");
		processIdentifierFi = MObjectIdCi.getDeclaredInstanceField("processIdentifier"); 
		counterFi = MObjectIdCi.getDeclaredInstanceField("counter");
		staticNextCounterFi = MObjectIdCi.getStaticField("NEXT_COUNTER");
		assert timestampFi != null;
		assert machineIdentifierFi != null;
		assert processIdentifierFi != null;
		assert counterFi != null;
		assert staticNextCounterFi != null;
		
		IntegerCi = ClassLoaderInfo.getSystemResolvedClassInfo(Integer.class.getName());//.getCurrentResolvedClassInfo(Integer.class.getName());
		integerValueFi = IntegerCi.getDeclaredInstanceField("value");
		assert integerValueFi != null;
	}
}
