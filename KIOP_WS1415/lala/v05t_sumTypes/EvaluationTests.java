package lala.v05t_sumTypes;

import junit.framework.TestCase;
import lala.core.Language;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v02_typed_simple.parser.parsetree.Type.NumType;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v05t_sumTypes.parser.ParserV05T;
import lala.v05t_sumTypes.parser.parsetree.Inl;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;
import lala.v05t_sumTypes.reduction.callbyvalue.CallByValueV05;
import lala.v05t_sumTypes.typecheck.TypeCheckV05;

public class EvaluationTests extends TestCase {

	public void test_InlInr() throws Exception {
		Language l;
		Term t;

		l = new Language(new ParserV05T(), new CallByValueV05());
		t = l.run("(inl^(Bool+Num)(true))");
		assertEquals(Bool.TRUE, ((Inl) t).term);
	}

	public void test_typedInl() throws Exception {

		TypedLanguage l;

		String program = "(inl^(Bool+Num)(true))";
		l = new TypedLanguage(new ParserV05T(), new CallByValueV05(),
				new TypeCheckV05());
		assertEquals(sumType(boolType(), numType()), l.typeOf(program));
	}

	public void test_typedCase() throws Exception {

		TypedLanguage l;
		Term t;

		String program;

		program = "(case(inl^(Bool+Num)(true))inl^(Bool+Num) y=>(y)||inr^(Bool+Num) z=>(true))";
		l = new TypedLanguage(new ParserV05T(), new CallByValueV05(),
				new TypeCheckV05());
		assertEquals(boolType(), l.typeOf(program));

		t = l.run(program);

		assertEquals(Bool.TRUE, t);

		program = "(case(inr^(Bool+Num)(0))inl^(Bool+Num) y=>(1)||inr^(Bool+Num) z=>(2))";
		l = new TypedLanguage(new ParserV05T(), new CallByValueV05(),
				new TypeCheckV05());
		assertEquals(numType(), l.typeOf(program));
		t = l.run(program);
		assertEquals("(2)", t.toString());

	}

	public void test_typedInr() throws Exception {

		TypedLanguage l;

		String program = "(inr^(Bool+Num)(1))";
		l = new TypedLanguage(new ParserV05T(), new CallByValueV05(),
				new TypeCheckV05());
		assertEquals(sumType(boolType(), numType()), l.typeOf(program));
	}

	public void test_sumTypeIf() throws Exception {
		TypedLanguage l;

		String program = "(if(true)(1)(false))";
		l = new TypedLanguage(new ParserV05T(), new CallByValueV05(),
				new TypeCheckV05());

		assertEquals("inl^(Num+Bool)(1)", l.run(program).toString());
	}

	private Object sumType(Type t1, Type t2) {
		return new SumType(t1, t2);
	}

	private NumType numType() {
		return new NumType();
	}

	private BoolType boolType() {
		return new BoolType();
	}
}
