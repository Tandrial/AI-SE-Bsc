package lala.v07t_recursion.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v07t_recursion.parser.parsetree.Eq;
import lala.v07t_recursion.parser.parsetree.Fix;

public class ParserV07TTests extends TestCase {

	public void parseString(String s) {
		LanguageParser p = new ParserV07T();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testEqZero() throws Exception {
		ParserV07T p = new ParserV07T();
		Term t;

		p.init("(=(1)(2))");

		t = p.start();
		assertTrue(t instanceof Eq);

	}

	public void testRecursion() {
		ParserV07T p = new ParserV07T();
		Term t;

		p.init("(fix(λx:Num.(x)))");

		t = p.start();
		assertTrue(t instanceof Fix);

	}
}
