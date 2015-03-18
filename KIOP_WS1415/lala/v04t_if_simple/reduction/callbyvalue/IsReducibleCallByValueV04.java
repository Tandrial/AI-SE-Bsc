package lala.v04t_if_simple.reduction.callbyvalue;

import lala.core.syntaxtree.Application;
import lala.v03t_boolsAndInts.reduction.callbyvalue.IsReducibleCallByValueV03;
import lala.v04t_if_simple.parser.parsetree.If;

public class IsReducibleCallByValueV04 extends IsReducibleCallByValueV03 {

	public Boolean isReducible(If t) {
		return true;
	}

	public Boolean isReducible(Application a) {
		return super.isReducible(a);
	}

}
