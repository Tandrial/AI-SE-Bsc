package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstantParser;

public class NotParser extends ExpressionConstantParser {

	@Override
	public Not createObject() {
		return new Not();
	}

	@Override
	public String expressionConstantTerm() {
		return Not.ExpressionConstant;
	}

}
