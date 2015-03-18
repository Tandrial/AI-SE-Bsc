package lala.v03t_boolsAndInts.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v03t_boolsAndInts.parser.parsetree.And;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v03t_boolsAndInts.parser.parsetree.Not;
import lala.v03t_boolsAndInts.parser.parsetree.Num;
import lala.v03t_boolsAndInts.parser.parsetree.Or;
import lala.v03t_boolsAndInts.parser.parsetree.Plus;
import lala.v03t_boolsAndInts.parser.parsetree.Succ;

public class ParserV03TTests extends TestCase {

	public void parseStrings(String[] s) {
		for (int i = 0; i < s.length; i++) {
			this.parseString(s[i]);
		}
	}

	public void parseString(String s) {
		LanguageParser p = new ParserV03T();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testParseStrings() throws Exception {
		this.parseStrings(new String[] { "(λx:Bool.(true))",
				"(λx:Bool.(false))", "(λx:Bool.((and)(true)))",
				"(λx:Bool.(and))", "(λx:Bool.((not)(x)))", "((1)(2))",
				"((λx:Num.(x))(1))", "((λx:(Bool->Bool).((x)(true)))(not))" });
	}

	public void testParseExpressionConstants() {
		ParserV03T p = new ParserV03T();

		p.init("(true)");
		assertTrue(p.start() instanceof Bool);

		p.init("(false)");
		assertTrue(p.start() instanceof Bool);

		p.init("(and)");
		Term t = p.start();
		assertTrue(t instanceof And);

		p.init("(not)");
		assertTrue(p.start() instanceof Not);

		p.init("(or)");
		assertTrue(p.start() instanceof Or);

		p.init("(1)");
		assertTrue(p.start() instanceof Num);

		p.init("(succ)");
		assertTrue(p.start() instanceof Succ);

		p.init("(0)");
		assertTrue(p.start() instanceof Num);

		p.init("(1)");
		assertTrue(p.start() instanceof Num);

		p.init("(plus)");
		assertTrue(p.start() instanceof Plus);

	}

}
