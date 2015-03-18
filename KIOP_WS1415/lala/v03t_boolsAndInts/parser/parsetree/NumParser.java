package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.ExpressionConstantParser;

public class NumParser extends ExpressionConstantParser {

	@Override
	public Num parse(Parser parser) {
		Num a = createObject();
		parser.eatChar('(');
		a.value = Integer.parseInt(parser.parseNumber());
		parser.eatChar(')');
		return a;
	}

	@Override
	public Num createObject() {
		return new Num();
	}

	@Override
	public String expressionConstantTerm() {
		return "dummy";
	}

}
