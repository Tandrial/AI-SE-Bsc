package lala.core.util;

import junit.framework.TestCase;
import lala.core.syntaxtree.AbstractionUntyped;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;

public class ToStringWriterTests extends TestCase {

	public void testWriterVariable() {
		assertEquals("(x)", writeToString(var("x")));
		assertEquals("(y)", writeToString(var("y")));
	}

	public void testWriterApplication() {
		assertEquals("((x)(y))", writeToString(app(var("x"), var("y"))));
		assertEquals("((x)((y)(z)))",
				writeToString(app(var("x"), app(var("y"), var("z")))));
	}

	public void testWriterAbstraction() {
		assertEquals("(λx.(x))", writeToString(abs("x", var("x"))));
	}

	/*
	 * Helper functions for Tests
	 */

	public String writeToString(Term t) {
		return new ToStringWriter().writeToString(t);
	}

	public Variable var(String s) {
		return new Variable(s);
	}

	public Application app(Term t1, Term t2) {
		return new Application(t1, t2);
	}

	public AbstractionUntyped abs(String varName, Term body) {
		return new AbstractionUntyped(varName, body);
	}

}
