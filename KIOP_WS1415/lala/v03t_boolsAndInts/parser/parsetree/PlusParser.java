package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstantParser;

public class PlusParser extends ExpressionConstantParser {

	@Override
	public Plus createObject() {
		return new Plus();
	}

	@Override
	public String expressionConstantTerm() {
		return Plus.ExpressionConstant;
	}

}
