package lala.v01_untyped_simple.reduction.callbyvalue;

import lala.core.dispatcher.Dispatcher;
import lala.core.syntaxtree.AbstractionUntyped;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;

public class IsReducibleCallByValueV01 extends Dispatcher<Boolean> {

	@Override
	public String methodPrefix() {
		return "isReducible";
	}

	public Boolean isReducible(Term t) {
		return super.dispatch(t);
	}

	public Boolean isReducible(AbstractionUntyped a) {
		return false;
	}

	public Boolean isReducible(Variable v) {
		return false;
	}

	public Boolean isReducible(Application a) {
		return (a.left.isAbstraction()) || isReducible(a.left)
				|| isReducible(a.right);
	}

}
