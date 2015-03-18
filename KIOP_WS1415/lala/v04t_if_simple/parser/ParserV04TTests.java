package lala.v04t_if_simple.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v04t_if_simple.parser.parsetree.If;

public class ParserV04TTests extends TestCase {

	public void parseString(String s) {
		LanguageParser p = new ParserV04T();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testIf() {
		ParserV04T p = new ParserV04T();

		p.init("(if(true)(true)(false))");
		assertTrue(p.start() instanceof If);

	}
}
