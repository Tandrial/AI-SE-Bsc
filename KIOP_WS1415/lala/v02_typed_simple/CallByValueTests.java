package lala.v02_typed_simple;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.ParserV02T;
import lala.v02_typed_simple.reduction.callbyvalue.CallByValueV02;

public class CallByValueTests extends TestCase {

	public void testCallByValue_01() throws Exception {
		LanguageParser p = new ParserV02T();
		Term t = p.start("((λx:Num.(x))(y))");
		CallByValueV02 e = new CallByValueV02();
		Term result = e.reduce(t);
		assertEquals("(y)", result.toString());
	}

	public void testCallByValue_02() throws Exception {
		LanguageParser p = new ParserV02T();
		Term t = p.start("((λx:Num.(x))((λz:Num.(z))(y)))");
		CallByValueV02 e = new CallByValueV02();
		Term result = e.reduce(t);
		// Erst Reduktion der rechten Seite
		assertEquals("((λx:Num.(x))(y))", result.toString());
	}

	public void testCallByValue_03() throws Exception {
		LanguageParser p = new ParserV02T();
		Term t = p.start("(λx:Bool.((x)((λy:Bool.(y))(z))))");
		CallByValueV02 e = new CallByValueV02();
		Term result = e.reduce(t);
		// Keine Reduktion innerhalb einer Abstraktion
		assertEquals("(λx:Bool.((x)((λy:Bool.(y))(z))))", result.toString());
	}

}
