package gov.nasa.jpf.symbc.mdb.tcg.test_class_generator;

import java.util.Set;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import gov.nasa.jpf.symbc.mdb.tcg.test_method_generator.MongoTestMethodGenerator;

public class MongoTestClassGenerator extends StdTestClassGenerator {
	public MongoTestClassGenerator(
			boolean assumeSettersForNonSpecialCases,
			boolean assumePublicEmptyConstructorForNonSpecialCases) {
		super(assumeSettersForNonSpecialCases, assumePublicEmptyConstructorForNonSpecialCases);
	}
	
	@Override
	public String generateTestClassSetUps() {
		String databaseDecAndInit = "\tprotected static MongoDatabase "
				+ MongoTestMethodGenerator.mongoDatabaseParameterName + ";\r\n";
		String beforeClassMethodString = "\t@BeforeClass\r\n\tpublic static void setUpClass() {\r\n"
				+ "\t\tCodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),\r\n"
				+ "\t\t\tCodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));\r\n"
				+ "\t\tMongoClient mongoClient = new MongoClient(\"localhost\", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());\r\n"
				+ "\t\t" + MongoTestMethodGenerator.mongoDatabaseParameterName
				+ " = mongoClient.getDatabase(\"" + MongoTestMethodGenerator.mongoDatabaseName + "\");\r\n"
				+ "\t\t" + MongoTestMethodGenerator.mongoDatabaseParameterName + ".drop();" + "\r\n\t}\r\n";

		String afterMethodString = "\t@After\r\n\tpublic void tearDown() {\r\n" + "\t\t"
				+ MongoTestMethodGenerator.mongoDatabaseParameterName + ".drop();\r\n" + "\t}\r\n";
		
		return super.generateTestClassSetUps() + databaseDecAndInit + beforeClassMethodString
				+ afterMethodString;
	}
	
	@Override
	protected void addSpecificTypesToImport(Set<String> typesToImport) {
		typesToImport.add("static com.mongodb.client.model.Filters.eq");
		typesToImport.add(CodecRegistry.class.getName());
		typesToImport.add(PojoCodecProvider.class.getName());
		typesToImport.add(CodecRegistries.class.getName());
		typesToImport.add(MongoClient.class.getName());
		typesToImport.add(MongoClientOptions.class.getName());
		typesToImport.add(Document.class.getName());
		typesToImport.add(ObjectId.class.getName());
		typesToImport.add(MongoDatabase.class.getName());
		typesToImport.add(MongoCollection.class.getName());
	}
}