package lala.core.syntaxtree;

/**
 * ExpressionConstant is the superclass for expression constants such as plus,
 * succ, or, etc.
 * 
 * The implementation assumes that all terms (including expression constants)
 * are closed terms -- hence the default behavior is that the replacement of the
 * free variables does not do anything.
 * 
 * @author stefan
 *
 */
public abstract class ExpressionConstant extends Term {

	public Term replaceFreeVariable(String v, Term replacement) {
		return this;
	}

	public abstract String toString();

}
