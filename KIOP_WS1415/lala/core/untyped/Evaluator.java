package lala.core.untyped;

import lala.core.syntaxtree.Term;

public interface Evaluator {
	public Term reduce(Term t);
}
