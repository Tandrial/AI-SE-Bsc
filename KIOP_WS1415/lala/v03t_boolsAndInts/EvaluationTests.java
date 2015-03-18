package lala.v03t_boolsAndInts;

import junit.framework.TestCase;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.v03t_boolsAndInts.parser.ParserV03T;
import lala.v03t_boolsAndInts.reduction.callbyvalue.CallByValueV03;
import lala.v03t_boolsAndInts.typecheck.TypeCheckV03;

public class EvaluationTests extends TestCase {

	TypedLanguage language() {
		return new TypedLanguage(new ParserV03T(), new CallByValueV03(),
				new TypeCheckV03());
	}

	public void runProgNCompareWith(String prog, String result) {
		// System.out.println("Program: " + prog);
		TypedLanguage l = language();
		Term t = (Term) l.parser.start(prog);
		l.typeChecker.typeOf(t);
		Term r = l.run(prog);
		// System.out.println(r.getClass());
		assertEquals(result, r.toString());

	}

	public void test_andTrueFalse_01() throws Exception {

		runProgNCompareWith("((not)(true))", "(false)");
		runProgNCompareWith("((not)(false))", "(true)");

		runProgNCompareWith("((and)(true))", "#WrappedUntypedTerm#");

		runProgNCompareWith("(((and)(true))(true))", "(true)");
		runProgNCompareWith("(((and)(true))(false))", "(false)");
		runProgNCompareWith("(((and)(false))(true))", "(false)");
		runProgNCompareWith("(((and)(false))(false))", "(false)");

		runProgNCompareWith("((not)((not)(true)))", "(true)");

		runProgNCompareWith("((succ)(0))", "(1)");
		runProgNCompareWith("((succ)(1))", "(2)");

		runProgNCompareWith("((succ)((succ)(0)))", "(2)");

		runProgNCompareWith("(((plus)(2))(3))", "(5)");

		runProgNCompareWith("((pred)(1))", "(0)");
	}

}
