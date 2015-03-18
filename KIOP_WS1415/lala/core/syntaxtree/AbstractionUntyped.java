package lala.core.syntaxtree;

import lala.core.reduction.Applyable;

public class AbstractionUntyped extends Term implements Applyable {

	public String varname = "";
	public Term body = null;

	public AbstractionUntyped() {
	}

	public AbstractionUntyped(String v, Term b) {
		varname = v;
		body = b;
	}

	public Term replaceFreeVariable(String v, Term right) {
		if (varname.equals(v)) {
			return this;
		} else {
			body = body.replaceFreeVariable(v, right);
			return this;
		}
	}

	public String toString() {
		return "(λ" + varname.toString() + "." + body.toString() + ")";
	}

	public Term apply(Term right) {
		body = body.replaceFreeVariable(varname, right);
		return body;
	}

	public boolean isAbstraction() {
		return true;
	}

}
