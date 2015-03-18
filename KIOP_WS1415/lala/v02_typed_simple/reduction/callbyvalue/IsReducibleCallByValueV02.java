package lala.v02_typed_simple.reduction.callbyvalue;

import lala.v01_untyped_simple.reduction.callbyvalue.IsReducibleCallByValueV01;
import lala.v02_typed_simple.parser.parsetree.Abstraction;

public class IsReducibleCallByValueV02 extends IsReducibleCallByValueV01 {

	public Boolean isReducible(Abstraction a) {
		return false;
	}

}
