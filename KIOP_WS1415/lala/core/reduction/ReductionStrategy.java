package lala.core.reduction;

import lala.core.syntaxtree.Term;

/**
 * A reduction strategy is for example CallByValue or NormalOrder, etc.
 * 
 * @author Stefan Hanenberg
 */
public interface ReductionStrategy {
	Term reduce(Term t);

	Boolean isReducible(Term t);
}
