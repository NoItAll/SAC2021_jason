package gov.nasa.jpf.symbc.mdb.tcg.test_class_generator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.tcg.TestClassGenerator;

public class StdTestClassGenerator implements TestClassGenerator {
	
	protected final boolean assumeSettersForNonSpecialCases;
	protected final boolean assumePublicEmptyConstructorForNonSpecialCases;
	
	public StdTestClassGenerator(
			boolean assumeSettersForNonSpecialCases,
			boolean assumePublicEmptyConstructorForNonSpecialCases) {
		this.assumeSettersForNonSpecialCases = assumeSettersForNonSpecialCases;
		this.assumePublicEmptyConstructorForNonSpecialCases = assumePublicEmptyConstructorForNonSpecialCases;
	}
	
	public String generateTestClassString(
			String printTestFileTo,
			String packageName, 
			Set<String> typesToImport, 
			List<String> testMethodStrings) {
		String testClassStart = generateTestClassStart(packageName, printTestFileTo, typesToImport);
		String testClassSetUp = generateTestClassSetUps();
		StringBuilder testMethods = new StringBuilder();
		for (String tm : testMethodStrings) {
			String tmWithReplacedTypesInMethodBody = this.replaceTypes(tm, typesToImport);
			tmWithReplacedTypesInMethodBody = tmWithReplacedTypesInMethodBody.replace("java.lang.", "");
			testMethods.append(tmWithReplacedTypesInMethodBody);
		}
		String testClassEnd = generateTestClassEnd();
		return testClassStart + testClassSetUp + testMethods.toString() + testClassEnd;
	}
	
	protected String generateTestClassStart(String packageName, String printTestFileTo, Set<String> typesToImport) {
		String header = packageName + "\r\n\r\n";
		String className = printTestFileTo;
		int indexOfSep = className.lastIndexOf(File.separatorChar);
		if (indexOfSep != -1) {
			className = className.substring(indexOfSep + 1);
		}
		int indexOfDot = className.lastIndexOf('.');
		if (indexOfDot != -1) {
			className = className.substring(0, indexOfDot);
		}

		String suppress = "@SuppressWarnings(\"all\")\r\n";
		return header + importTypes(typesToImport).toString() + "\r\n" + suppress + "public class " + className
				+ " {\r\n";
	}
	
	protected String generateTestClassSetUps() {
		String reflectionSetterMethod = "";
		if (!assumeSettersForNonSpecialCases) {
			reflectionSetterMethod = "\tprotected void setWithReflection(Object setFor, String fieldName, Object setTo) {\r\n"
					+ "\t\tif (fieldName.equals(\"this$0\")) {\r\n" + "\t\t\treturn;\r\n" + "\t\t}\r\n"
					+ "\t\ttry { \r\n" + "\t\t\tClass<?> setForClass = setFor.getClass();\r\n"
					+ "\t\t\tField setForField = setForClass.getDeclaredField(fieldName);\r\n"
					+ "\t\t\tboolean accessible = setForField.isAccessible();\r\n"
					+ "\t\t\tsetForField.setAccessible(true);\r\n" + "\t\t\tsetForField.set(setFor, setTo);\r\n"
					+ "\t\t\tsetForField.setAccessible(accessible);\r\n" + "\t\t} catch (Exception e) {\r\n"
					+ "\t\t\tthrow new RuntimeException(e);\r\n" + "\t\t}\r\n" + "\t}\r\n";
		}
		String reflectionConstructorMethod = "";
		if (!assumePublicEmptyConstructorForNonSpecialCases) {
			reflectionConstructorMethod = "\tprotected Object generateInstance(Class<?> c) {\r\n" + "\t\ttry {\r\n"
					+ "\t\t\tConstructor<?> constr = c.getDeclaredConstructor(new Class<?>[0]);\r\n"
					+ "\t\t\tboolean accessible = constr.isAccessible();\r\n" + "\t\t\tconstr.setAccessible(true);\r\n"
					+ "\t\t\tObject result = constr.newInstance(new Object[constr.getParameters().length]);\r\n"
					+ "\t\t\tconstr.setAccessible(accessible);\r\n" + "\t\t\treturn result;\r\n"
					+ "\t\t} catch (Exception e) {\r\n" + "\t\t\tthrow new IllegalArgumentException(e);\r\n"
					+ "\t\t}\r\n" + "\t}\r\n\r\n";
		}

		return reflectionConstructorMethod + reflectionSetterMethod;
	}
	
	private StringBuilder importTypes(Set<String> types) {
		if (!assumeSettersForNonSpecialCases) {
			types.add(Field.class.getName());
		}
		if (!assumePublicEmptyConstructorForNonSpecialCases) {
			types.add(Constructor.class.getName());
		}
		addSpecificTypesToImport(types);
		List<String> sortedTypes = Utility.sortStrings(types);
		
		StringBuilder result = new StringBuilder();
		for (String t : sortedTypes) {
			if (t.length() > 0) {
				result.append("import ").append(t).append(";\r\n");
			}
		}
		return result;
	}
	
	protected void addSpecificTypesToImport(Set<String> types) {}

	private String replaceTypes(String replaceIn, Set<String> types) {
		for (String t : types) {
			replaceIn = replaceIn.replace(t, t.substring(t.lastIndexOf('.') + 1));
		}
		return replaceIn;
	}
	
	protected String generateTestClassEnd() {
		return "}";
	}
}
