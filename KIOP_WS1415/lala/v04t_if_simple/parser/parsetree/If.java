package lala.v04t_if_simple.parser.parsetree;

import lala.core.syntaxtree.Term;

public class If extends Term {

	public Term condition;
	public Term thenBranch;
	public Term elseBranch;

	public If() {
		super();
	}

	public If(Term condition, Term thenBranch, Term elseBranch) {
		super();
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public Term replaceFreeVariable(String v, Term replacement) {
		condition.replaceFreeVariable(v, replacement);
		thenBranch.replaceFreeVariable(v, replacement);
		elseBranch.replaceFreeVariable(v, replacement);
		return this;
	}

	@Override
	public String toString() {
		return "(if" + condition + thenBranch + elseBranch + ")";
	}
}
