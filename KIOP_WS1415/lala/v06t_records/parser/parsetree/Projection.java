package lala.v06t_records.parser.parsetree;

import lala.core.syntaxtree.Term;

public class Projection extends Term {

	public Term term;
	public String selection;

	public Projection() {
	}

	public Projection(Term term, String selection) {
		this.term = term;
		this.selection = selection;
	}

	public String toString() {
		return "(" + term.toString() + "." + selection.toString() + ")";
	}

	public Term replaceFreeVariable(String v, Term replacement) {
		if (selection.equals(v))
			term.replaceFreeVariable(v, replacement);
		return this;
	}
}
