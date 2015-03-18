package lala.core.syntaxtree;

public abstract class Term {

	public abstract Term replaceFreeVariable(String v, Term replacement);

	public boolean isAbstraction() {
		return false;
	}
}
