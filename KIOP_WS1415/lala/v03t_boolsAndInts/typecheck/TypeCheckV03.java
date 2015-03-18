package lala.v03t_boolsAndInts.typecheck;

import lala.core.typed.Environment;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;
import lala.v02_typed_simple.parser.parsetree.Type.NumType;
import lala.v02_typed_simple.typecheck.TypeCheckV02;
import lala.v03t_boolsAndInts.parser.parsetree.And;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v03t_boolsAndInts.parser.parsetree.Not;
import lala.v03t_boolsAndInts.parser.parsetree.Num;
import lala.v03t_boolsAndInts.parser.parsetree.Or;
import lala.v03t_boolsAndInts.parser.parsetree.Plus;
import lala.v03t_boolsAndInts.parser.parsetree.Pred;
import lala.v03t_boolsAndInts.parser.parsetree.Succ;

public class TypeCheckV03 extends TypeCheckV02 {

	/** Types of Expression constants **/
	public Type typeOf(Num n, Environment e) {
		return numType();
	}

	protected NumType numType() {
		return new NumType();
	}

	protected FunctionType funType(Type t1, Type t2) {
		return new FunctionType(t1, t2);
	}

	public Type typeOf(Bool n, Environment e) {
		return boolType();
	}

	protected BoolType boolType() {
		return new BoolType();
	}

	public Type typeOf(Not n, Environment e) {
		return funType(boolType(), boolType());
	}

	public Type typeOf(And n, Environment e) {
		return funType(boolType(), funType(boolType(), boolType()));
	}

	public Type typeOf(Or n, Environment e) {
		return funType(boolType(), funType(boolType(), boolType()));
	}

	public Type typeOf(Succ n, Environment e) {
		return funType(numType(), numType());
	}

	public Type typeOf(Pred n, Environment e) {
		return funType(numType(), numType());
	}

	public Type typeOf(Plus n, Environment e) {
		return funType(numType(), funType(numType(), numType()));
	}

}
