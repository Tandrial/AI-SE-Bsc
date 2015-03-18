package lala.v02_typed_simple;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.ParserV02T;
import lala.v02_typed_simple.reduction.normalorder.NormalOrder;

public class NormalOrderTests extends TestCase {

	public void testNormalOrder_01() throws Exception {
		LanguageParser p = new ParserV02T();
		Term t = p.start("((λx:Num.(x))(y))");
		NormalOrder e = new NormalOrder();
		Term result = e.reduce(t);
		assertEquals("(y)", result.toString());
	}

	public void testNormalOrder_02() throws Exception {
		LanguageParser p = new ParserV02T();
		Term t = p.start("((λx:Num.(x))((λz:Num.(z))(y)))");
		NormalOrder e = new NormalOrder();
		Term result = e.reduce(t);
		assertEquals("((λz:Num.(z))(y))", result.toString());
	}

	public void testNormalOrder_03() throws Exception {
		LanguageParser p = new ParserV02T();
		Term t = p.start("(λx:Bool.((x)((λy:Bool.(y))(z))))");
		NormalOrder e = new NormalOrder();
		Term result = e.reduce(t);
		assertEquals("(λx:Bool.((x)(z)))", result.toString());
	}
}
