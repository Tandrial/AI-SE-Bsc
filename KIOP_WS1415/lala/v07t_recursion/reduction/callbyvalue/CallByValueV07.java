package lala.v07t_recursion.reduction.callbyvalue;

import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Abstraction;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v06t_records.reduction.callbyvalue.CallByValueV06;
import lala.v07t_recursion.parser.parsetree.Eq;
import lala.v07t_recursion.parser.parsetree.Fix;

public class CallByValueV07 extends CallByValueV06 {

	public Term reduce(Eq t) {
		if (isReducible(t.left)) {
			t.left = reduce(t.right);
		} else if (isReducible(t.right)) {
			t.right = reduce(t.right);
		} else {
			if (t.left.equals(t.right))
				return Bool.TRUE;
			else
				return Bool.FALSE;
		}
		return t;
	}

	public Term reduce(Fix t) {
		if (isReducible(t.term)) {
			t.term = reduce(t.term);
		} else {
			if (t.term instanceof Abstraction) {
				Abstraction abs = (Abstraction) t.term;
				return abs.body.replaceFreeVariable(abs.varname, t);
			} else {
				throw new RuntimeException("fix total kaputt.");
			}
		}
		return t;
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV07().isReducible(t);
	}
}
