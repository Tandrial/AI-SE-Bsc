package lala.core.syntaxtree;

public class Variable extends Term {

	public String identifier;

	public Variable() {
	}

	public Variable(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public Term replaceFreeVariable(String v, Term replacement) {
		if (identifier.equals(v))
			return replacement;
		return this;
	}

	public String toString() {
		return "(" + identifier + ")";
	}

}
