package lala.v02_typed_simple;

import junit.framework.TestCase;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.ParserV02T;
import lala.v02_typed_simple.reduction.callbyvalue.CallByValueV02;
import lala.v02_typed_simple.typecheck.TypeCheckV02;

public class TypeCheckTests extends TestCase {

	TypedLanguage language() {
		return new TypedLanguage(new ParserV02T(), new CallByValueV02(),
				new TypeCheckV02());
	}

	public void testTypeCheck_01() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("(λx:Num.(x))");
		assertEquals("(Num->Num)", l.typeChecker.typeOf(t).toString());
	}

	public void testTypeCheck_02() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("(λx:(Num->Num).(x))");
		assertEquals("((Num->Num)->(Num->Num))", l.typeChecker.typeOf(t)
				.toString());
	}

	public void testTypeCheck_03() throws Exception {
		TypedLanguage l = language();
		Term t = l.parser.start("(λx:(Bool->Num).(x))");
		assertEquals("((Bool->Num)->(Bool->Num))", l.typeChecker.typeOf(t)
				.toString());
	}

}
