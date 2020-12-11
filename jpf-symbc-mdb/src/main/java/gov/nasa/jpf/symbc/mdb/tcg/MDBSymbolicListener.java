package gov.nasa.jpf.symbc.mdb.tcg;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.ARETURN;
import gov.nasa.jpf.jvm.bytecode.DRETURN;
import gov.nasa.jpf.jvm.bytecode.FRETURN;
import gov.nasa.jpf.jvm.bytecode.IRETURN;
import gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction;
import gov.nasa.jpf.jvm.bytecode.LRETURN;
import gov.nasa.jpf.listener.CoverageAnalyzer;
import gov.nasa.jpf.listener.CoverageAnalyzer.ClassCoverage;
import gov.nasa.jpf.listener.CoverageAnalyzer.MethodCoverage;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.bytecode.BytecodeUtils;
import gov.nasa.jpf.symbc.bytecode.INVOKESTATIC;
import gov.nasa.jpf.symbc.concolic.PCAnalyzer;
import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.mdb.MDBClassInfoElementInfoUtility;
import gov.nasa.jpf.symbc.mdb.Utility;
import gov.nasa.jpf.symbc.mdb.tcg.MethodSummary.InputArgument;
import gov.nasa.jpf.symbc.mdb.tcg.test_class_generator.MongoTestClassGenerator;
import gov.nasa.jpf.symbc.mdb.tcg.test_method_generator.MongoTestMethodGenerator;
import gov.nasa.jpf.symbc.mdb.tcg.test_method_generator.StdTestMethodGenerator;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.numeric.RealExpression;
import gov.nasa.jpf.symbc.numeric.SymbolicConstraintsGeneral;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Types;
import gov.nasa.jpf.vm.VM;

/**
 * Class generating test cases for symbolic methods for one class within the
 * test driver. The generation of different {@link MethodSummary}-objects with
 * several {@link TestCase}-objects is initiated here. Other methods, like
 * test-set reduction are initiated as well. For this, this class utilizes
 * {@link CoverageAnalyzer}. Currently, it is necessary that objects in the test
 * driver-method are non-null.
 */
public class MDBSymbolicListener extends PropertyListenerAdapter implements PublisherExtension {
	/*
	 * 29-04-2020 This is mainly inspired by gov.nasa.jpf.symbc.SymbolicListener and
	 * gov.nasa.jpf.symbc.heap.HeapSymbolicListener: Hendrik Winkelmann
	 */

	protected TestCaseGenerator tcg;
	protected final CoverageAnalyzer ca;
	protected final boolean reduceTestSet;
	protected final String testedClassName;

	// TODO Make naming more general
	protected String printTestsTo;
	protected String testClassPackage;
	
	protected final boolean assumeSettersForNonSpecialCases;
	protected final boolean assumePublicEmptyConstructorForNonSpecialCases;

	// If set to true, the convention of "driver_<Real method name>" is assumed. In
	// this specific case,
	// MongoCollection-parameters are removed from the final method call used for
	// test case generation and the
	// name is adjusted to "<Real method name>".
	protected final boolean circumventDriverCall;
	
	protected final long nanoStartTime;

	/**
	 * The constructor as per specification.
	 * 
	 * @param conf The configuration-object.
	 * @param jpf  The JPF-object.
	 */
	public MDBSymbolicListener(Config conf, JPF jpf) {
		jpf.addPublisherExtension(ConsolePublisher.class, this);
		// TODO Currently, the coverage analyzer resets and deletes all previous covered
		// lines,
		// if backtracking occurs after returning a value. Still, the number of test
		// cases can
		// be efficiently reduced for the exemplary cases, however, this is still not as
		// precise.
		this.ca = new CoverageAnalyzer(conf, jpf);
		ca.publishAtEnd = false;
		String tempTestedClassName = conf.getTarget();
		testedClassName = tempTestedClassName.substring(tempTestedClassName.lastIndexOf('.') + 1);
		reduceTestSet = conf.getBoolean("symbolic.mdb.reduce_coverage_losless", true);
		
		String testPath = conf.getString("symbolic.mdb.test_path");
		printTestsTo = preparePath(testPath, "Test_");
		String testPackage = conf.getString("symbolic.mdb.test_package");

		testClassPackage = preparePackageName(testPackage);
		assumeSettersForNonSpecialCases = conf.getBoolean("symbolic.mdb.assume_setters", true);
		assumePublicEmptyConstructorForNonSpecialCases = conf.getBoolean("symbolic.mdb.assume_public_empty_constructor",
				true);
		StdTestMethodGenerator.assumeSettersForNonSpecialCases = assumeSettersForNonSpecialCases;
		StdTestMethodGenerator.assumePublicEmptyConstructorForNonSpecialCases = assumePublicEmptyConstructorForNonSpecialCases;

		circumventDriverCall = conf.getBoolean("symbolic.mdb.circumvent_driver_call", false);
		tcg = new TestCaseGenerator(
				printTestsTo, testClassPackage, 
				reduceTestSet);
		tcg.setTestMethodGenerator(new MongoTestMethodGenerator(circumventDriverCall));
		tcg.setTestClassGenerator(new MongoTestClassGenerator(assumeSettersForNonSpecialCases, assumePublicEmptyConstructorForNonSpecialCases));
		MDBClassInfoElementInfoUtility.initialize();
		nanoStartTime = System.nanoTime();
	}

	protected String preparePackageName(String givenPackageName) {
		if (givenPackageName == null || !(givenPackageName.length() > 0)) {
			givenPackageName = "";
		} else {
			givenPackageName = "package " + givenPackageName.replace(" ", "") + ";";
		}
		return givenPackageName;
	}

	protected String preparePath(String givenPath, String addition) {
		if (givenPath == null || !(givenPath.length() > 0)) {
			givenPath = addition + "GeneratedTests.java";
		}
		givenPath = givenPath.replace('/', File.separatorChar);
		return givenPath.replace('\\', File.separatorChar);
	}

	@Override
	public void instructionExecuted(VM vm, ThreadInfo ti, Instruction nextInstruction, Instruction insn) {
		ca.instructionExecuted(vm, ti, nextInstruction, insn);
		if (vm.getSystemState().isIgnored()) {
			return;
		}
		Config conf = vm.getConfig();

		if (insn instanceof JVMInvokeInstruction) {
			JVMInvokeInstruction iinsn = (JVMInvokeInstruction) insn;
			MethodInfo mi = iinsn.getInvokedMethod();
			String methodName = iinsn.getInvokedMethodName();
			String methodShortName = methodName.contains("(") ? methodName.substring(0, methodName.indexOf("("))
					: methodName;
			ClassInfo ci = mi.getClassInfo();
			String className = ci.getName();

			StackFrame sf = ti.getTopFrame();
			if (!mi.equals(sf.getMethodInfo())) {
				return;
			}
			Object[] argValues = iinsn.getArgumentValues(ti);
			int numberOfArgs = argValues.length;
			if (!BytecodeUtils.isClassSymbolic(conf, className, mi, methodName)
					&& !BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null)) {
				return;
			}
			ClassCoverage cc = ca.getClassCoverages().get(mi.getClassName());
			Map<MethodInfo, MethodCoverage> methodCoverages = cc.getMethodCoverages();
			BitSet executableClassLines = new BitSet();
			for (MethodCoverage tempMc : methodCoverages.values()) {
				// This denotes executable, not reachable lines.
				executableClassLines.or(tempMc.getExecutableLinesAsBS());
			}
			MethodCoverage mc = cc.getMethodCoverage(mi);
			BitSet executableLines = mc.getExecutableLinesAsBS();
			MethodSummary s = new MethodSummary(vm.getHeap().get(sf.getThis()), className, methodShortName, mi, executableLines, executableClassLines,
					new MongoTestMethodGenerator(circumventDriverCall));

			LocalVarInfo[] argInfos = mi.getArgumentLocalVars();
			if (argInfos == null) {
				throw new RuntimeException("You need to turn debugging on.");
			}
			int sfIndex = iinsn instanceof INVOKESTATIC ? 0 : 1;
			int namesIndex = sfIndex;
			byte[] argTypes = mi.getArgumentTypes();

			for (int i = 0; i < numberOfArgs; i++) {
				Expression expLocal = (Expression) sf.getLocalAttr(sfIndex);
				s.stringArgValues += argValues[i];
				s.stringArgTypes += argTypes[i];
				if (expLocal != null) // In this case, the argument is marked to be symbolic
					s.stringSymValues += expLocal.toString();
				else
					s.stringSymValues += argInfos[namesIndex].getName() + "_CONCRETE";
				if (i < numberOfArgs - 1) {
					s.stringArgValues += ",";
					s.stringArgTypes += ",";
					s.stringSymValues += ",";
				}
				s.addInputArgument(argTypes[i], argInfos[namesIndex].getName(), argValues[i], expLocal);
				sfIndex++;
				namesIndex++;
				// Shift stack index extra, if long or double are represented
				if (argTypes[i] == Types.T_LONG || argTypes[i] == Types.T_DOUBLE)
					sfIndex++;
			}
			tcg.initializeSymbolicMethodInvocation(s);
			return;
		}

		if (insn instanceof JVMReturnInstruction) {
			MethodInfo mi = insn.getMethodInfo();
			generateTestCase(vm, mi, conf, ti, (JVMReturnInstruction) insn, null);
		}
	}

	// Is called from instructionInvoked(...) and propertyViolated(...)
	// In the latter case ti and insn are null and an exception is expected.
	protected void generateTestCase(VM vm, MethodInfo mi, Config conf, ThreadInfo ti, JVMReturnInstruction insn,
			gov.nasa.jpf.Error er) {
		ClassInfo ci = mi.getClassInfo();
		assert ci != null;
		String className = ci.getName();
		String methodName = mi.getName();
		String methodLongName = mi.getLongName();
		int numberOfArgs = mi.getNumberOfArguments();
		if (!BytecodeUtils.isClassSymbolic(conf, className, mi, methodName)
				&& !BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null)) {
			return;
		}
		ChoiceGenerator<?> cg = vm.getChoiceGenerator();
		if (!(cg instanceof PCChoiceGenerator)) {
			ChoiceGenerator<?> prev = cg.getPreviousChoiceGenerator();
			while (prev != null && !(prev instanceof PCChoiceGenerator)) {
				prev = prev.getPreviousChoiceGenerator();
			}
			cg = prev;
		}

		if (cg != null) {
			PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
			if (SymbolicInstructionFactory.concolicMode) {
				SymbolicConstraintsGeneral solver = new SymbolicConstraintsGeneral();
				PCAnalyzer pa = new PCAnalyzer();
				pa.solve(pc, solver);
			} else {
				pc.solve();
			}
			assert PathCondition.flagSolved;
		}
		Object result = null;
		String returnString = "";
		if (er == null) {
			assert insn != null;
			Expression returnAttr = (Expression) ((JVMReturnInstruction) insn).getReturnAttr(ti);
			if (returnAttr instanceof IntegerExpression) { // Check if it is a symbolic integer
				result = returnAttr;
				returnString += ((IntegerExpression) returnAttr).solution();
			} else if (returnAttr instanceof RealExpression) {
				result = returnAttr;
				returnString += ((RealExpression) returnAttr).solution();
			} else if (returnAttr instanceof StringSymbolic) {
				result = returnAttr;
				returnString += ((StringSymbolic) returnAttr).solution;
			} else { // Is not symbolic
				if (insn instanceof IRETURN) {
					int returnValue = ((IRETURN) insn).getReturnValue();
					returnString += returnValue;
					result = new IntegerConstant(returnValue);
				} else if (insn instanceof LRETURN) {
					long returnValue = ((LRETURN) insn).getReturnValue();
					returnString += returnValue;
					result = new IntegerConstant(returnValue);
				} else if (insn instanceof DRETURN) {
					double returnValue = ((DRETURN) insn).getReturnValue();
					returnString += returnValue;
					result = new RealConstant(returnValue);
				} else if (insn instanceof FRETURN) {
					float returnValue = ((FRETURN) insn).getReturnValue();
					returnString += returnValue;
					result = new RealConstant(returnValue);
				} else if (insn instanceof ARETURN) {
					result = ((ARETURN) insn).getReturnValue(ti);
					ElementInfo returnEi = (ElementInfo) result;
					if (returnEi != null && returnEi.getClassInfo().isStringClassInfo()) {
						returnString += returnEi.asString();
					} else {
						returnString += result;
					}
				} else {
					result = new VoidMarker(); // void
				}
			}
		} else {
			result = er; // Error
		}

		HeapChoiceGenerator heapCg = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
		Heap heap = vm.getHeap();
		HeapNode[] allHn;
		if (heapCg != null) {
			allHn = heapCg.getCurrentSymInputHeap().getAllNodes();
		} else {
			allHn = new HeapNode[0];
		}

		MethodSummary s = this.tcg.getMethodSummaryForName(methodLongName);
		List<InputArgument> inputArgs = s.getIA();

		Map<InputArgument, Object> valuesForArgs = new HashMap<>();
		List<InputArgument> symbolicObjectInputs = new ArrayList<>();
		for (InputArgument i : inputArgs) {
			if (!i.isSymbolic()) {
				valuesForArgs.put(i, i.argValue);
			} else if (i.isSymbolicInteger()) {
				if (i.isPrimitiveIntegerArg()) {
					valuesForArgs.put(i, i.getSymbolicIntegerSolution());
				} else if ((i.isWrappingIntegerArg())) {
					valuesForArgs.put(i, getEiForIa(i, heap, allHn));
					symbolicObjectInputs.add(i);
				} else {
					assert false;
				}
			} else if (i.isSymbolicReal()) {
				if (i.isPrimitiveRealArg()) {
					valuesForArgs.put(i, i.getSymbolicRealSolution());
				} else if (i.isWrappingRealArg()) {
					valuesForArgs.put(i, getEiForIa(i, heap, allHn));
					symbolicObjectInputs.add(i);
				} else {
					assert false;
				}
			} else if (i.isSymbolicString()) {
				valuesForArgs.put(i, i.getSymbolicStringSolution());
			} else if (i.isSymbolicObject()) {
				valuesForArgs.put(i, getEiForIa(i, heap, allHn));
				symbolicObjectInputs.add(i);
			} else {
				assert false;
			}
		}

		Map<String, ClassCoverage> ccs = ca.getClassCoverages();
		ClassCoverage sutCov = ccs.get(mi.getClassName());
		BitSet coveredLines = sutCov.getCoveredLinesAsBS();
		// TODO Notice that for the next result only the incremental covered lines are given:
		sutCov.resetMethodCoverages();
		tcg.generateTestCase(valuesForArgs, result, returnString, heap,
				coveredLines, methodLongName);
	}

	protected ElementInfo getEiForIa(InputArgument ia, Heap heap, HeapNode[] nodes) {
		for (HeapNode n : nodes) {
			ElementInfo connectedEi = heap.get(n.getIndex());
			if (ia.argExpr.equals(n.getSymbolic())) {
				return connectedEi;
			}
		}
		
		return ((ElementInfo) ia.argValue);
	}

	@Override
	public void propertyViolated(Search search) {
		if (search.isErrorState()) {
			if (!tcg.hasMethodSummaries()) {
				throw new RuntimeException("Something in the test driver is wrong: "
						+ "An error is thrown without previously invoking a symbolic method." + search.getErrors());
			} else {
				MethodSummary s = tcg.getLastInvokedMethodSummary(); // TODO
				generateTestCase(search.getVM(), s.mi, search.getVM().getConfig(), null, null,
						search.getCurrentError());
			}
		}
	}

	protected void generateTests() {
		tcg.printTests();
	}

	@Override
	public void searchFinished(Search search) {
		generateTests();
		long nanoEndTime = System.nanoTime();
		long nanoDuration = nanoEndTime - nanoStartTime;
		double secondsDuration = ((double) nanoDuration) / 1e9;
		System.out.println("Finished test-case-generation.");
		System.out.println("Duration of execution: " + secondsDuration);
	}

	@Override
	public void classLoaded(VM vm, ClassInfo ci) {
		this.ca.classLoaded(vm, ci);
	}

	public void stateBacktracked(Search s) {
//		System.out.println("-------------BACKTRACK-------------");
//		PCChoiceGenerator pcg = ((PCChoiceGenerator) s.getVM().getLastChoiceGeneratorOfType(PCChoiceGenerator.class));
//		if (pcg != null) {
//			System.out.println(pcg.getCurrentPC());
//		}
//		System.out.println("-------------BACKTRACK END-------------");
	}
}
