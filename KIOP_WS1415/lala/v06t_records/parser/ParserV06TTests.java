package lala.v06t_records.parser;

import junit.framework.TestCase;
import lala.core.parser.LanguageParser;
import lala.core.syntaxtree.Term;
import lala.v06t_records.parser.parsetree.Projection;
import lala.v06t_records.parser.parsetree.Record;

public class ParserV06TTests extends TestCase {

	public void parseString(String s) {
		LanguageParser p = new ParserV06T();
		assertTrue(p.start(s) instanceof Term);
	}

	public void testRecord() {
		ParserV06T p = new ParserV06T();
		Term t;

		p.init("{a=(1)}");
		t = p.start();
		assertTrue(t instanceof Record);

		p.init("{a=(1),b=(succ),hallo=(true)}");
		t = p.start();
		assertTrue(t instanceof Record);

		p.init("({a=(1),b=(succ),hallo=(true)}.b)");
		t = p.start();
		assertTrue(t instanceof Projection);

	}
}
