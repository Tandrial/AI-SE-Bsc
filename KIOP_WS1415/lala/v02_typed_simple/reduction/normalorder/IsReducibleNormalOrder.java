package lala.v02_typed_simple.reduction.normalorder;

import lala.v02_typed_simple.parser.parsetree.Abstraction;

public class IsReducibleNormalOrder extends
		lala.v01_untyped_simple.reduction.normalorder.IsReducibleNormalOrder {

	public Boolean isReducible(Abstraction a) {
		return isReducible(a.body);
	}

}
