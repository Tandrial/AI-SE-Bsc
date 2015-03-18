package lala.v02_typed_simple.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;
import lala.v02_typed_simple.parser.parsetree.Type.NumType;

public class ParserV02TTests extends TestCase {

	public void parseStrings(String[] s) {
		for (int i = 0; i < s.length; i++) {
			this.parseString(s[i]);
		}
	}

	public void parseString(String s) {
		LanguageParser p = new ParserV02T();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testParseStrings() throws Exception {
		this.parseStrings(new String[] { "(x)", "((x)(y))", "(λx:Bool.(x))",
				"(λx:(Bool->Bool).(x))", "(λx:(Bool->Num).(x))", });
	}

	public void testParseTypes_01() {
		this.parseString("(λx:Bool.(x))");
	}

	public void testParseTypes() {
		ParserV02T p = new ParserV02T();

		p.init("Bool");
		assertTrue(p.parseType() instanceof BoolType);

		p.init("Num");
		p.parseType();

		p.init("Num");
		assertTrue(p.parseType() instanceof NumType);

		p.init("(Num->Bool)");
		assertTrue(p.parseType() instanceof FunctionType);
	}

}
