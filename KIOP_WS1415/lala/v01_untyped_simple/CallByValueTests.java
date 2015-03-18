package lala.v01_untyped_simple;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v01_untyped_simple.parser.ParserV01U;
import lala.v01_untyped_simple.reduction.callbyvalue.CallByValueV01;

public class CallByValueTests extends TestCase {

	public void testCallByValue_01() throws Exception {
		LanguageParser p = new ParserV01U();
		Term t = p.start("((λx.(x))(y))");
		CallByValueV01 e = new CallByValueV01();
		Term result = e.reduce(t);
		assertEquals("(y)", result.toString());
	}

	public void testCallByValue_02() throws Exception {
		LanguageParser p = new ParserV01U();
		Term t = p.start("((λx.(x))((λz.(z))(y)))");
		CallByValueV01 e = new CallByValueV01();
		Term result = e.reduce(t);
		// Erst Reduktion der rechten Seite
		assertEquals("((λx.(x))(y))", result.toString());
	}

	public void testCallByValue_03() throws Exception {
		LanguageParser p = new ParserV01U();
		Term t = p.start("(λx.((x)((λy.(y))(z))))");
		CallByValueV01 e = new CallByValueV01();
		Term result = e.reduce(t);
		// Keine Reduktion innerhalb einer Abstraktion
		assertEquals("(λx.((x)((λy.(y))(z))))", result.toString());
	}

}
