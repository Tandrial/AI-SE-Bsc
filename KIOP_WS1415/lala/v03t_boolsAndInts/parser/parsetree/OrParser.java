package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstantParser;

public class OrParser extends ExpressionConstantParser {

	@Override
	public Or createObject() {
		return new Or();
	}

	@Override
	public String expressionConstantTerm() {
		return Or.ExpressionConstant;
	}

}
