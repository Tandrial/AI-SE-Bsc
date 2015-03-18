package lala.v07t_recursion.reduction.callbyvalue;

import lala.v06t_records.reduction.callbyvalue.IsReducibleCallByValueV06;
import lala.v07t_recursion.parser.parsetree.Eq;
import lala.v07t_recursion.parser.parsetree.Fix;

public class IsReducibleCallByValueV07 extends IsReducibleCallByValueV06 {

	public Boolean isReducible(Eq t) {
		return true;
	}

	public Boolean isReducible(Fix t) {
		return true;
	}
}
