package lala.v04t_if_simple;

import junit.framework.TestCase;
import lala.core.Language;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v04t_if_simple.parser.ParserV04T;
import lala.v04t_if_simple.reduction.callbyvalue.CallByValueV04;
import lala.v04t_if_simple.typecheck.TypeCheckV04;

public class EvaluationTests extends TestCase {

	public void test_UntypedIf() throws Exception {

		Language l;
		Term t;

		l = new Language(new ParserV04T(), new CallByValueV04());
		t = l.run("(if(true)(true)(false))");
		assertEquals(Bool.TRUE, t);

		l = new Language(new ParserV04T(), new CallByValueV04());
		t = l.run("(if(false)(true)(false))");
		assertEquals(Bool.FALSE, t);

		l = new Language(new ParserV04T(), new CallByValueV04());
		t = l.run("(if((not)(false))(true)(false))");
		assertEquals(Bool.TRUE, t);

	}

	public void test_typedIf() throws Exception {

		TypedLanguage l;
		Term t;

		l = new TypedLanguage(new ParserV04T(), new CallByValueV04(),
				new TypeCheckV04());
		t = l.run("(if(true)(true)(false))");
		assertEquals(Bool.TRUE, t);
		assertEquals(new BoolType(), l.typeOf("(if(true)(true)(false))"));

	}

}
