package lala.core.syntaxtree;

import lala.core.parser.Parser;
import lala.v01_untyped_simple.parser.ParserV01U;

public class ApplicationParser implements TermParser {

	public Application parse(Parser parser) {
		Application a = new Application();
		parser.eatChar('(');
		a.left = ((ParserV01U) parser).parseExpression();
		a.right = ((ParserV01U) parser).parseExpression();
		parser.eatChar(')');
		return a;
	}

}
