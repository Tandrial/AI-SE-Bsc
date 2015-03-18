package lala.v07t_recursion;

import junit.framework.TestCase;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v07t_recursion.parser.ParserV07T;
import lala.v07t_recursion.reduction.callbyvalue.CallByValueV07;
import lala.v07t_recursion.typcheck.TypeCheckV07;

public class EvaluationTests extends TestCase {

	public void test_typedEqZero() throws Exception {
		TypedLanguage l;

		String program = "(=(1)(1))";

		l = new TypedLanguage(new ParserV07T(), new CallByValueV07(),
				new TypeCheckV07());

		assertEquals(new BoolType(), l.typeOf(program));
		assertEquals(Bool.TRUE, l.run(program));

		program = "(=(1)(0))";
		assertEquals(new BoolType(), l.typeOf(program));
		assertEquals(Bool.FALSE, l.run(program));

		program = "(=(1)((succ)(0)))";
		assertEquals(new BoolType(), l.typeOf(program));
		assertEquals(Bool.TRUE, l.run(program));

	}

	public void test_complexEqZero() throws Exception {
		TypedLanguage l;
		String program;

		program = "(" + "(fix" + "(λsum:(Num->Num).(" + "λn:Num.(" + "if("
				+ "=(n)(5)" + ")" + "(sum)" + "(1)" + ")" + ")" + ")" + ")"
				+ "(4)" + ")";

		// String iff = "(if(=(n)(5))(true)(false))";
		// iff = "(sum((succ)(n)))";

		l = new TypedLanguage(new ParserV07T(), new CallByValueV07(),
				new TypeCheckV07());
		// assertEquals(new NumType(), l.typeOf(program));
		Type type = l.typeOf(program);
		System.out.println(type);
		Term t = l.parseProgram(program);
		System.out.println(t);
		t = l.run(program);
		System.out.println(t);
	}
}
