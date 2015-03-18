package lala.core.typed;

import lala.core.syntaxtree.Term;
import lala.core.typed.syntaxtree.Type;

public interface TypeChecker {
	public Type typeOf(Term t);

	public Type typeOf(Term t, Environment e);

	public boolean matches(Type t1, Type t2);
}
