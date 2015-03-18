package lala.v06t_records.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v06t_records.parser.ParserV06T;

public class ProjectionParser implements TermParser {

	public Term parse(Parser parser) {
		Projection p = new Projection();
		parser.eatChar('(');
		p.term = ((ParserV06T) parser).parseExpression();
		parser.eatChar('.');
		p.selection = ((ParserV06T) parser).parseIdentifier();
		parser.eatChar(')');
		return p;
	}
}
