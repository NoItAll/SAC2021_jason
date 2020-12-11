package gov.nasa.jpf.symbc.mdb.tcg;

import java.util.Set;

public interface TestMethodGenerator {
	String generateTestCaseString(TestCase tc);
	String getString(TestCase tc);
	Set<String> getImports();
}
