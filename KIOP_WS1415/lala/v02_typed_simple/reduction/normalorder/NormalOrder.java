package lala.v02_typed_simple.reduction.normalorder;

import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Abstraction;

public class NormalOrder extends
		lala.v01_untyped_simple.reduction.normalorder.NormalOrder implements
		ReductionStrategy {

	public Term reduce(Abstraction a) {
		return super.reduce(a);
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleNormalOrder().isReducible(t);
	}
}
