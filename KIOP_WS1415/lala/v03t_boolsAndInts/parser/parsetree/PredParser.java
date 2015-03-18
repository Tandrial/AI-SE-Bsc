package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstantParser;

public class PredParser extends ExpressionConstantParser {

	@Override
	public Pred createObject() {
		return new Pred();
	}

	@Override
	public String expressionConstantTerm() {
		return Pred.ExpressionConstant;
	}

}
