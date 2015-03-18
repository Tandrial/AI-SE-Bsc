package lala.v04t_if_simple.reduction.callbyvalue;

import lala.core.syntaxtree.Term;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v03t_boolsAndInts.reduction.callbyvalue.CallByValueV03;
import lala.v04t_if_simple.parser.parsetree.If;

public class CallByValueV04 extends CallByValueV03 {

	public Term reduce(If t) {

		if (isReducible(t.condition)) {
			t.condition = reduce(t.condition);
			return t;
		} else {
			// The condition should be now true or false
			if (t.condition.equals(Bool.TRUE)) {
				return t.thenBranch;
			} else {
				return t.elseBranch;
			}
		}

	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV04().isReducible(t);
	}
}
