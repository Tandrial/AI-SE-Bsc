package lala.v01_untyped_simple.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.lib.UntypedChurchConstants;

public class ParserV01UTests extends TestCase {

	public void parseStrings(String[] s) {
		for (int i = 0; i < s.length; i++) {
			this.parseString(s[i]);
		}
	}

	public void parseString(String s) {
		LanguageParser p = new ParserV01U();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testpred() throws Exception {
		LanguageParser p = new ParserV01U();
		assertTrue(p.start(UntypedChurchConstants.PRED) instanceof Term);
	}

	// public void testParseStrings() throws Exception {
	// this.parseStrings(new String[] { "(x)", "((x)(y))", "(λx.(x))",
	// "((x)((y)(z)))", "(λx.(λy.(y)))" });
	// }
}
