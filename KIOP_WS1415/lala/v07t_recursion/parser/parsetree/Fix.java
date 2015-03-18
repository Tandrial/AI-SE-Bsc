package lala.v07t_recursion.parser.parsetree;

import lala.core.syntaxtree.Term;

public class Fix extends Term {

	public Term term;

	@Override
	public String toString() {
		return "(fix" + term + ")";
	}

	@Override
	public Term replaceFreeVariable(String v, Term replacement) {
		replaceFreeVariable(v, replacement);
		return this;
	}
}
