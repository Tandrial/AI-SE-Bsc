package lala.v01_untyped_simple.reduction.callbyvalue;

import lala.core.dispatcher.Dispatcher;
import lala.core.reduction.Applyable;
import lala.core.reduction.CannotReduceException;
import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.AbstractionUntyped;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;

public class CallByValueV01 extends Dispatcher<Term> implements
		ReductionStrategy {

	@Override
	public String methodPrefix() {
		return "reduce";
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV01().isReducible(t);
	}

	public Term reduce(Term t) {
		t = super.dispatch(t);
		return t;
	}

	public Term reduce(AbstractionUntyped a) {
		return a;
	}

	public Term reduce(Variable v) {
		return v;
	}

	public Term reduce(Application a) {
		// System.out.println("REDUCE: " + a);

		if (a.left.isAbstraction()) {
			if (isReducible(a.right)) {
				a.right = reduce(a.right);
				return a;
			} else {
				Term ret = ((Applyable) a.left).apply(a.right);
				// System.out.println("REDUCE: " + ret);
				return ret;
			}
		} else {
			if (isReducible(a.left)) {
				a.left = reduce(a.left);
				// System.out.println("REDUCE: " + a);
				return a;
			} else {
				if (isReducible(a.right)) {
					a.right = reduce(a.right);
					// System.out.println("REDUCE: " + a);
					return a;
				} else {
					throw new CannotReduceException("Cannot reduce");
				}
			}
		}
	}

}
