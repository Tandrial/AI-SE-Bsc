package lala.v07t_recursion.parser.parsetree;

import lala.core.syntaxtree.Term;

public class Eq extends Term {
	public Term left;
	public Term right;

	@Override
	public String toString() {
		return "(=" + left + right + ")";
	}

	@Override
	public Term replaceFreeVariable(String v, Term replacement) {
		left = left.replaceFreeVariable(v, replacement);
		right = right.replaceFreeVariable(v, replacement);
		return this;
	}
}