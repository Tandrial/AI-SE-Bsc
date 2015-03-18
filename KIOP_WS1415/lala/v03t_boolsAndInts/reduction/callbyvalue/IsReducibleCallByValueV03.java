package lala.v03t_boolsAndInts.reduction.callbyvalue;

import lala.core.syntaxtree.Application;
import lala.v02_typed_simple.parser.parsetree.Abstraction;
import lala.v02_typed_simple.reduction.callbyvalue.IsReducibleCallByValueV02;
import lala.v03t_boolsAndInts.parser.parsetree.And;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v03t_boolsAndInts.parser.parsetree.Not;
import lala.v03t_boolsAndInts.parser.parsetree.Num;
import lala.v03t_boolsAndInts.parser.parsetree.Or;
import lala.v03t_boolsAndInts.parser.parsetree.Pred;
import lala.v03t_boolsAndInts.parser.parsetree.Succ;
import lala.v03t_boolsAndInts.parser.parsetree.WrappedTerm;

public class IsReducibleCallByValueV03 extends IsReducibleCallByValueV02 {

	public Boolean isReducible(Abstraction a) {
		return false;
	}

	public Boolean isReducible(Num a) {
		return false;
	}

	public Boolean isReducible(Bool a) {
		return false;
	}

	public Boolean isReducible(Not a) {
		return false;
	}

	public Boolean isReducible(Succ a) {
		return false;
	}

	public Boolean isReducible(Pred a) {
		return false;
	}

	public Boolean isReducible(Or a) {
		return false;
	}

	public Boolean isReducible(And a) {
		return false;
	}

	public Boolean isReducible(Application a) {
		return super.isReducible(a);
	}

	public Boolean isReducible(WrappedTerm t) {
		return t.isReducible();
	}

}
