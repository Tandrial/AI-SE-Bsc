package lala.v05t_sumTypes.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Abstraction;
import lala.v05t_sumTypes.parser.parsetree.Case;
import lala.v05t_sumTypes.parser.parsetree.Inl;
import lala.v05t_sumTypes.parser.parsetree.Inr;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class ParserV05TTests extends TestCase {

	public void parseString(String s) {
		LanguageParser p = new ParserV05T();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testIf() {
		ParserV05T p = new ParserV05T();

		p.init("(inl^(Bool+Num)(false))");
		assertTrue(p.start() instanceof Inl);

		p.init("(inr^(Bool+Num)(false))");
		assertTrue(p.start() instanceof Inr);

		p.init("(inr^(Bool+Num)((true)(false)))");
		assertTrue(p.start() instanceof Inr);

		p.init("(λx:(Bool+Num).(x))");
		assertTrue((((Abstraction) p.start()).type) instanceof SumType);

		p.init("(case(x)inl^(Bool+Num) y=>(y)||inr^(Bool+Num) z=>(z))");
		assertTrue(p.start() instanceof Case);
	}
}
