package lala.core.syntaxtree;

import lala.core.parser.Parser;
import lala.v01_untyped_simple.parser.ParserV01U;

public class AbstractionUntypedParser implements TermParser {

	@Override
	public AbstractionUntyped parse(Parser parser) {
		AbstractionUntyped a = new AbstractionUntyped();
		parser.eatChars("(λ");
		a.varname = parser.parseIdentifier();
		parser.eatChar('.');
		a.body = ((ParserV01U) parser).parseExpression();
		parser.eatChar(')');
		return a;
	}

}