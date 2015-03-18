package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.ExpressionConstantParser;

public class BoolParser extends ExpressionConstantParser {

	@Override
	public Bool parse(Parser parser) {
		Bool a = createObject();
		parser.eatChar('(');
		a.value = parseBool(parser);
		parser.eatChar(')');
		return a;
	}

	private boolean parseBool(Parser parser) {
		String identifier = parser.parseIdentifier();
		if (identifier.equals("true"))
			return true;
		if (identifier.equals("false"))
			return false;
		throw new RuntimeException("Unknown");
	}

	@Override
	public Bool createObject() {
		return new Bool();
	}

	@Override
	public String expressionConstantTerm() {
		return "dummy";
	}
}
