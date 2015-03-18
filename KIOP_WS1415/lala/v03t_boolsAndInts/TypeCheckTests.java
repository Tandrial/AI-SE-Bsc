package lala.v03t_boolsAndInts;

import junit.framework.TestCase;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.v03t_boolsAndInts.parser.ParserV03T;
import lala.v03t_boolsAndInts.reduction.callbyvalue.CallByValueV03;
import lala.v03t_boolsAndInts.typecheck.TypeCheckV03;

public class TypeCheckTests extends TestCase {

	TypedLanguage language() {
		return new TypedLanguage(new ParserV03T(), new CallByValueV03(),
				new TypeCheckV03());
	}

	public void testTypeCheck_01() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("(1)");
		assertEquals("Num", l.typeChecker.typeOf(t).toString());
	}

	public void testTypeCheck_02() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("((λx:Num.(x))(1))");
		assertEquals("Num", l.typeChecker.typeOf(t).toString());
	}

	public void testTypeCheck_03() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("((λx:(Bool->Bool).((x)(true)))(not))");
		assertEquals("Bool", l.typeChecker.typeOf(t).toString());
	}

	public void testTypeCheck_04() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("((λx:(Bool->Bool).(and))(not))");
		assertEquals("(Bool->(Bool->Bool))", l.typeChecker.typeOf(t).toString());
	}

}
