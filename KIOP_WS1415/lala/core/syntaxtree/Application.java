package lala.core.syntaxtree;

public class Application extends Term {

	public Term left;
	public Term right;

	public Application() {
	}

	public Application(Term l, Term r) {
		left = l;
		right = r;
	}

	public Term replaceFreeVariable(String v, Term replacement) {
		left = left.replaceFreeVariable(v, replacement);
		right = right.replaceFreeVariable(v, replacement);
		return this;
	}

	public String toString() {
		return "(" + left.toString() + right.toString() + ")";
	}

}
