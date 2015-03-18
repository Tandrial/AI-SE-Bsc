package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.reduction.Applyable;
import lala.core.syntaxtree.ExpressionConstant;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;
import lala.v02_typed_simple.parser.parsetree.Type.NumType;

public class Plus extends ExpressionConstant implements Applyable {

	public static String ExpressionConstant = "plus";

	@Override
	public String toString() {
		return ExpressionConstant;
	}

	public boolean isAbstraction() {
		return true;
	}

	public Term apply(Term right) {
		return new WrappedTerm(this, right, new FunctionType(new NumType(),
				new NumType()));
	}
}
