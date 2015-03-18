package lala.v05t_sumTypes.reduction.callbyvalue;

import lala.v04t_if_simple.reduction.callbyvalue.IsReducibleCallByValueV04;
import lala.v05t_sumTypes.parser.parsetree.Case;
import lala.v05t_sumTypes.parser.parsetree.Inl;
import lala.v05t_sumTypes.parser.parsetree.Inr;

public class IsReducibleCallByValueV05 extends IsReducibleCallByValueV04 {

	public Boolean isReducible(Inl t) {
		return isReducible(t.term);
	}

	public Boolean isReducible(Inr t) {
		return isReducible(t.term);
	}

	public Boolean isReducible(Case c) {
		return isReducible(c.term) || (c.term instanceof Inl)
				|| (c.term instanceof Inr);
	}
}
