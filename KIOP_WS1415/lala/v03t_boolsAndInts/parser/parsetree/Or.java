package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstant;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;

public class Or extends ExpressionConstant {

	public static String ExpressionConstant = "or";

	@Override
	public String toString() {
		return ExpressionConstant;
	}

	public boolean isAbstraction() {
		return true;
	}

	public Term apply(Term right) {
		return new WrappedTerm(this, right, new FunctionType(new BoolType(),
				new BoolType()));
	}

}
