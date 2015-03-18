package lala.v01_untyped_simple;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v01_untyped_simple.parser.ParserV01U;
import lala.v01_untyped_simple.reduction.normalorder.NormalOrder;

public class NormalOrderTests extends TestCase {

	public void testNormalOrder_01() throws Exception {
		LanguageParser p = new ParserV01U();
		Term t = p.start("((λx.(x))(y))");
		NormalOrder e = new NormalOrder();
		Term result = e.reduce(t);
		assertEquals("(y)", result.toString());
	}

	public void testNormalOrder_02() throws Exception {
		LanguageParser p = new ParserV01U();
		Term t = p.start("((λx.(x))((λz.(z))(y)))");
		NormalOrder e = new NormalOrder();
		Term result = e.reduce(t);
		assertEquals("((λz.(z))(y))", result.toString());
	}

	public void testNormalOrder_03() throws Exception {
		LanguageParser p = new ParserV01U();
		Term t = p.start("(λx.((x)((λy.(y))(z))))");
		NormalOrder e = new NormalOrder();
		Term result = e.reduce(t);
		assertEquals("(λx.((x)(z)))", result.toString());
	}

}
