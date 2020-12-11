package gov.nasa.jpf.symbc.mdb.tcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.vm.ClassInfo;


public class SimpleLosslessTestCaseReducer implements TestCaseReducer {
	protected final MethodSummary reduceFor;
	
	public SimpleLosslessTestCaseReducer(MethodSummary reduceFor) {
		this.reduceFor = reduceFor;
	}
	
	
	private List<TestCase> generateSubsetFromGivenList(List<TestCase> overallSet) {
		List<TestCase> subset = new ArrayList<>(overallSet);
		// Get achievable coverage ratio for current test set.
		final double SET_COVERAGE = reduceFor.overallClassCoverageRatio(overallSet);
		
		// Simple coverage-loss-less procedure
		for (TestCase t : overallSet) {
			List<TestCase> temp = new ArrayList<>(subset);
			temp.remove(t);
			if (!(reduceFor.overallClassCoverageRatio(temp) < SET_COVERAGE)) {
				subset = temp;
			}
		}
		
		return subset;
	}
	
	public List<TestCase> generateSubset(List<TestCase> overallSet) {		
		// Exception-ClassInfo --> List of testcases.
		Map<ClassInfo, List<TestCase>> subsets = new HashMap<>();
		
		for (TestCase tc : overallSet) {
			 ClassInfo tcErrorCi = tc.hasError() ? tc.getErrorClassInfo() : null;
			 List<TestCase> testCasesForError = subsets.get(tcErrorCi);
			 if (testCasesForError == null) {
				 testCasesForError = new ArrayList<>();
				 subsets.put(tcErrorCi, testCasesForError);
			 }
			 testCasesForError.add(tc);
		}
		
		List<TestCase> result = new ArrayList<>();
		for (Map.Entry<ClassInfo, List<TestCase>> entry : subsets.entrySet()) {
			result.addAll(generateSubsetFromGivenList(entry.getValue()));
		}
		
		
		return result;
	}
	
}
