package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstantParser;

public class AndParser extends ExpressionConstantParser {

	@Override
	public And createObject() {
		return new And();
	}

	@Override
	public String expressionConstantTerm() {
		return And.ExpressionConstant;
	}

}
