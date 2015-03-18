package lala.v06t_records;

import junit.framework.TestCase;
import lala.core.TypedLanguage;
import lala.core.syntaxtree.Term;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.NumType;
import lala.v03t_boolsAndInts.parser.parsetree.Num;
import lala.v06t_records.parser.ParserV06T;
import lala.v06t_records.parser.parsetree.types.RecordType;
import lala.v06t_records.parser.parsetree.types.RecordTypeElement;
import lala.v06t_records.reduction.callbyvalue.CallByValueV06;
import lala.v06t_records.typecheck.TypeCheckV06;

public class EvaluationTests extends TestCase {

	public void test_typedRecord() throws Exception {
		TypedLanguage l;

		String program = "{a=(1),b=(2)}";
		l = new TypedLanguage(new ParserV06T(), new CallByValueV06(),
				new TypeCheckV06());

		assertEquals(
				recordType(recordTypeElem("a", new NumType()),
						recordTypeElem("b", new NumType())), l.typeOf(program));
	}

	public void test_typedProjection() throws Exception {
		TypedLanguage l;
		Term t;

		String program = "({a=(1),b=(true)}.a)";
		l = new TypedLanguage(new ParserV06T(), new CallByValueV06(),
				new TypeCheckV06());

		t = l.parseProgram(program);
		assertEquals(new NumType(), l.typeOf(t));

		t = l.run(t);
		assertEquals(new Num(1), t);

	}

	public void test_complexTests() throws Exception {
		TypedLanguage l;
		Term t;
		String program;

		program = "(((λx:{a:Num}.(x)){a=(1),b=(true)}).a)";

		l = new TypedLanguage(new ParserV06T(), new CallByValueV06(),
				new TypeCheckV06());

		assertEquals(new NumType(), l.typeOf(program));
		t = l.run(program);
		assertEquals(new Num(1), t);
	}

	private RecordTypeElement recordTypeElem(String name, Type type) {
		return new RecordTypeElement(name, type);
	}

	private Object recordType(RecordTypeElement t1, RecordTypeElement t2) {
		RecordType t = new RecordType();
		t.elems.add(t1);
		t.elems.add(t2);
		return t;
	}
}
