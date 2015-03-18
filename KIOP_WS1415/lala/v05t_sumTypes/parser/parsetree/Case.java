package lala.v05t_sumTypes.parser.parsetree;

import lala.core.syntaxtree.Term;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class Case extends Term {

	public Term term;
	public String inlVar;
	public Term inlTerm;
	public SumType inlSumType;

	public String inrVar;
	public Term inrTerm;
	public SumType inrSumType;

	public Case() {
		super();
	}

	public Case(Term t) {
		super();
		this.term = t;
	}

	public Term replaceFreeVariable(String v, Term replacement) {
		term.replaceFreeVariable(v, replacement);
		return this;
	}

}
