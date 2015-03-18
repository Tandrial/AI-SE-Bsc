package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.reduction.Applyable;
import lala.core.syntaxtree.ExpressionConstant;
import lala.core.syntaxtree.Term;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v03t_boolsAndInts.parser.PrimitiveToTermConverter;

public class Not extends ExpressionConstant implements Applyable {

	public static String ExpressionConstant = "not";

	@Override
	public String toString() {
		return ExpressionConstant;
	}

	public boolean isAbstraction() {
		return true;
	}

	public Term apply(Term right) {
		WrappedTerm wrappedTerm = new WrappedTerm(this, right, new BoolType());
		return new PrimitiveToTermConverter()
				.untypedLambdaToTypedTerm(wrappedTerm.wrappedTerm);
	}

}
