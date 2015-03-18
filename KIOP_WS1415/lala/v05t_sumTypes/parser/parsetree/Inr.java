package lala.v05t_sumTypes.parser.parsetree;

import lala.core.syntaxtree.Term;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class Inr extends Term {

	public SumType type;
	public Term term;

	public Inr() {
		super();
	}

	public Inr(SumType type, Term term) {
		super();
		this.type = type;
		this.term = term;
	}

	public Term replaceFreeVariable(String v, Term replacement) {
		term.replaceFreeVariable(v, replacement);
		return this;
	}

	@Override
	public String toString() {
		return "inr^(" + type.left + "+" + type.right + ")" + term;
	}

}
