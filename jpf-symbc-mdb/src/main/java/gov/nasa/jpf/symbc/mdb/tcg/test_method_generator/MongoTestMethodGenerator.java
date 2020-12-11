package gov.nasa.jpf.symbc.mdb.tcg.test_method_generator;

import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.counterFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.dataFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.entityClassFi;
import static gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility.nameFi;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import gov.nasa.jpf.symbc.mdb.mongo_mock.client.MMongoCollection;
import gov.nasa.jpf.symbc.mdb.symbolic_collection.MObjectId;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;

public class MongoTestMethodGenerator extends MDBTestMethodGenerator {
	
	public static String mongoDatabaseName = "devel";
	public static final String mongoDatabaseParameterName = "mongoDatabase";
	
	
	public MongoTestMethodGenerator(boolean circumventWrappingTestDriver) {
		super(circumventWrappingTestDriver);
		specialCases.add(MMongoCollection.class);
		specialCases.add(ObjectId.class);
		specialCases.add(Document.class);
		this.excludeTypeFromMethodCallParameters.add(MMongoCollection.class.getName());
	}
	
	@Override
	public String treatSpecialCase(StringBuilder sb, ElementInfo ei) {
		ClassInfo ci = ei.getClassInfo();
		String ciName = ci.getName();
		if (ciName.equals(MMongoCollection.class.getName())) {
			return treatOriginalWithMCol(sb, ei, ei.getObjectField("collection"));
		} else if (ciName.equals(ObjectId.class.getName())) {
			return treatObjectId(sb, ei);
		} else if (ciName.equals(Document.class.getName())) {
			constructorFor(sb, ei);
			ElementInfo mapEi = heap.get(ei.getReferenceField("documentAsMap"));
			generateMapPutStringForEi(sb, ei, true, getKeyValueReferencesFromMap(mapEi));
			return getNameForEi(ei);
		}
		return super.treatSpecialCase(sb, ei);
	}
	
	protected String treatObjectId(StringBuilder sb, ElementInfo ei) {
		return constructorFor(sb, ei, '"' + MObjectId.toHexString(ei.getIntField(counterFi)) + '"');
	}

	@Override
	protected String treatMObjectId(StringBuilder sb, ElementInfo ei) {
		return constructorForEiAsClass(sb, ObjectId.class.getName(), ei, '"' + MObjectId.toHexString(ei.getIntField(counterFi)) + '"');
	}

	@Override
	protected String generateDataCollectionForInput(StringBuilder sb, ElementInfo originalDataCollection,
			ElementInfo mcolEi) {
		String collectionName = mcolEi.getStringField(nameFi.getName());
		ElementInfo entityClassEi = mcolEi.getObjectField(entityClassFi.getName());
		String entityType = entityClassEi.getStringField("name");
		addToTypesIfNeeded(entityType);
		String eiName = argn();
		sb.append(MongoCollection.class.getName())
			.append(SP)
			.append(eiName)
			.append(EQ)
			.append(mongoDatabaseParameterName)
			.append(".getCollection(")
			.append('"' + collectionName + "\",")
			.append(entityType + ".class")
			.append(");\r\n");
		return eiName;
	}

	@Override
	protected String generateDataCollectionForAssertions(StringBuilder sb, ElementInfo originalDataCollection,
			ElementInfo mcolEi) {
		ElementInfo dataEi = mcolEi.getObjectField(dataFi.getName());
		if (dataEi == null) {
			return "";
		}
		String mcolName = argn();
		String collectionName = mcolEi.getStringField(nameFi.getName());
		sb.append(MongoCollection.class.getName()).append("<Document>")
			.append(SP)
			.append(mcolName)
			.append(EQ)
			.append(mongoDatabaseParameterName)
			.append(".getCollection(")
			.append('"' + collectionName + "\", ")
			.append("Document.class")
			.append(");\r\n");
		return mcolName;
	}

	@Override
	protected String generateEntityForMDocumentValues(StringBuilder sb, LinkedHashMap<String, String> mdocEiValues) {
		String docName = argn();
		sb.append("Document ").append(docName).append(" = new Document();\r\n");
				
		String putString = docName + ".put(\"%s\", %s);\r\n";
		String idName = mdocEiValues.get("_id");
		if (idName != null) {
			sb.append(String.format(putString, "_id", idName));
		}
		for (Map.Entry<String, String> entry : mdocEiValues.entrySet()) {
			if (entry.getKey().equals("_id")) continue;
			sb.append(String.format(putString, entry.getKey(), entry.getValue()));
		}
		return docName;
	}

	@Override
	protected String generateInsertStatementForDataCollection(String collectionArgName,
			String databaseElementArgName, ElementInfo originalDataCollection) {
		return collectionArgName + ".insertOne(" + databaseElementArgName + ")";
	}

	@Override
	protected String findOneElementWithIdFunction(String mdocArgName) {
		return "find(eq(" + mdocArgName + ".get(\"_id\"))).first()";
	}

	@Override
	protected String countDocumentsInDataCollectionFunction() {
		return "countDocuments()";
	}
	
}