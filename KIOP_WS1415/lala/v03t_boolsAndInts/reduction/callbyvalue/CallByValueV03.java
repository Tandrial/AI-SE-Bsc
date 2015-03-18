package lala.v03t_boolsAndInts.reduction.callbyvalue;

import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Abstraction;
import lala.v02_typed_simple.reduction.callbyvalue.CallByValueV02;

public class CallByValueV03 extends CallByValueV02 implements ReductionStrategy {

	public Term reduce(Abstraction a) {
		return a;
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV03().isReducible(t);
	}
}
