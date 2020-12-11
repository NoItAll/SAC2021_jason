package gov.nasa.jpf.symbc.mdb.tcg;

import java.util.List;
import java.util.Set;

public interface TestClassGenerator {
	String generateTestClassString(
			String printTestFileTo,
			String packageName,
			Set<String> typesToImport, 
			List<String> testMethodStrings);
	
}
