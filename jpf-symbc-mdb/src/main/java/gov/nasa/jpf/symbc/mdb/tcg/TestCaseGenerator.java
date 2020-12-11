package gov.nasa.jpf.symbc.mdb.tcg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nasa.jpf.symbc.mdb.tcg.MethodSummary.InputArgument;
import gov.nasa.jpf.vm.Heap;

public class TestCaseGenerator {
	
	// Method summaries for which test cases are to be generated
	protected List<MethodSummary> summaries = new ArrayList<>();
	protected final boolean reduceTestSet;
	protected final String printTestFileTo;
	protected final String packageName;
	protected TestMethodGenerator tmg;
	protected TestClassGenerator tcg;
	protected TestCaseReducer tcr;
	
	public TestCaseGenerator(
			String printTestFileTo,
			String packageName,
			boolean reduceTestSet) {
		this.printTestFileTo = printTestFileTo;
		this.packageName = packageName;
		this.reduceTestSet = reduceTestSet;
	}
	
	public void setTestMethodGenerator(TestMethodGenerator tmg) {
		this.tmg = tmg;
	}
	
	public void setTestClassGenerator(TestClassGenerator tcg) {
		this.tcg = tcg;
	}
	
	public void setTestCaseReducer(TestCaseReducer tcr) {
		this.tcr = tcr;
	}
	
	public boolean hasMethodSummaries() {
		return summaries.size() > 0;
	}
	
	public MethodSummary getLastInvokedMethodSummary() {
		return summaries.get(summaries.size() - 1);
	}
	
	public void initializeSymbolicMethodInvocation(MethodSummary ms) {
		assert !summaries.contains(ms) : "Methods must not be called multiple times in the driver. Symbolic methods must not invoke each other.";
		assert summaries.size() < 1 : "Currently, only one method must be called from the main-method.";
		summaries.add(ms);
	}
	
	public MethodSummary getMethodSummaryForName(String methodLongName) {
		for (MethodSummary m : summaries) {
			if (m.mi.getLongName().equals(methodLongName)) {
				return m;
			}
		}
		throw new RuntimeException("Something is wrong, the symbolic method should have been invoked before returning a result.");
	}
	
	public void generateTestCase(Map<InputArgument, Object> valuesForArgs, Object returnValue, String returnValueString, Heap heap, BitSet coveredLines, String methodLongName) {
		MethodSummary ms = getMethodSummaryForName(methodLongName);
		ms.generateTestCase(valuesForArgs, returnValue, returnValueString, heap, coveredLines);
	}
	
	public void printTests() {
		String result = "";
		Set<String> typesToImport = getTypesFromMethodSummaries();
		for (MethodSummary s : this.summaries) {
			if (reduceTestSet) {
				s.reduceTestCases();
			}
			List<String> testMethodStrings = s.getTestMethodStrings();
			result = tcg.generateTestClassString(printTestFileTo, packageName, typesToImport, testMethodStrings);
		}
		File f = new File(printTestFileTo);
		if (f.exists()) {
			f.delete();
		}
		try {
			if (!f.createNewFile()) {
				System.out.println(result);
			} else {
				try {
					PrintWriter pw = new PrintWriter(f);
					pw.append(result);
					pw.flush();
					pw.close();
				} catch (FileNotFoundException e) {
					System.out.println(result);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println(result);
			e.printStackTrace();
		}
	}
	
	protected Set<String> getTypesFromMethodSummaries() {
		Set<String> result = new LinkedHashSet<>();
		for (MethodSummary s : summaries) {
			result.addAll(s.getTypesToImportForIntegrationTests());
		}
		return result;
	}
}
