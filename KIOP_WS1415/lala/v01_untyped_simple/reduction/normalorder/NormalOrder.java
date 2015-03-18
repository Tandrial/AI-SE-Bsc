package lala.v01_untyped_simple.reduction.normalorder;

import lala.core.dispatcher.Dispatcher;
import lala.core.reduction.CannotReduceException;
import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.AbstractionUntyped;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;

public class NormalOrder extends Dispatcher<Term> implements ReductionStrategy {

	@Override
	public String methodPrefix() {
		return "reduce";
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleNormalOrder().isReducible(t);
	}

	public Term reduce(Term t) {
		t = super.dispatch(t);
		return t;
	}

	public Term reduce(AbstractionUntyped a) {
		a.body = reduce(a.body);
		return a;
	}

	public Term reduce(Variable v) {
		return v;
	}

	public Term reduce(Application a) {
		if (a.left.isAbstraction()) {
			return ((AbstractionUntyped) a.left).apply(a.right);
		} else {
			if (isReducible(a.left)) {
				a.left = reduce(a.left);
				return a;
			} else {
				if (isReducible(a.right)) {
					a.right = reduce(a.right);
					return a;
				} else {
					throw new CannotReduceException("Cannot reduce");
				}
			}
		}
	}

}
