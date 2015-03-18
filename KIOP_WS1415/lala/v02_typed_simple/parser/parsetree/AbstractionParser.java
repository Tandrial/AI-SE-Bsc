package lala.v02_typed_simple.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.TermParser;
import lala.v01_untyped_simple.parser.ParserV01U;
import lala.v02_typed_simple.parser.ParserV02T;

public class AbstractionParser implements TermParser {

	@Override
	public Abstraction parse(Parser parser) {
		Abstraction a = new Abstraction();
		parser.eatChars("(λ");
		a.varname = parser.parseIdentifier();
		parser.eatChar(':');
		a.type = ((ParserV02T) parser).parseType();
		parser.eatChar('.');
		a.body = ((ParserV01U) parser).parseExpression();
		parser.eatChar(')');
		return a;
	}

}
