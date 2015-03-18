package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstantParser;

public class SuccParser extends ExpressionConstantParser {

	@Override
	public Succ createObject() {
		return new Succ();
	}

	@Override
	public String expressionConstantTerm() {
		return Succ.ExpressionConstant;
	}

}