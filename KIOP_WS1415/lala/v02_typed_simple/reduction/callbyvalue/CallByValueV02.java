package lala.v02_typed_simple.reduction.callbyvalue;

import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.Term;
import lala.v01_untyped_simple.reduction.callbyvalue.CallByValueV01;
import lala.v02_typed_simple.parser.parsetree.Abstraction;

public class CallByValueV02 extends CallByValueV01 implements ReductionStrategy {

	public Term reduce(Abstraction a) {
		return a;
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV02().isReducible(t);
	}
}
