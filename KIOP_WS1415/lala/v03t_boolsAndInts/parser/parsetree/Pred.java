package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.reduction.Applyable;
import lala.core.syntaxtree.ExpressionConstant;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Type.NumType;
import lala.v03t_boolsAndInts.parser.PrimitiveToTermConverter;

public class Pred extends ExpressionConstant implements Applyable {

	public static String ExpressionConstant = "pred";

	@Override
	public String toString() {
		return ExpressionConstant;
	}

	public boolean isAbstraction() {
		return true;
	}

	public Term apply(Term right) {
		WrappedTerm wrappedTerm = new WrappedTerm(this, right, new NumType());
		return new PrimitiveToTermConverter()
				.untypedLambdaToTypedTerm(wrappedTerm.wrappedTerm);
	}
}
