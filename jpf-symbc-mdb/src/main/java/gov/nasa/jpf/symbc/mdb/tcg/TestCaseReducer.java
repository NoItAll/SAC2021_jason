package gov.nasa.jpf.symbc.mdb.tcg;

import java.util.List;

public interface TestCaseReducer {
	List<TestCase> generateSubset(List<TestCase> overallSet);
}
