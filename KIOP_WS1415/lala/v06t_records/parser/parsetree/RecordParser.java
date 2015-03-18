package lala.v06t_records.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v06t_records.parser.ParserV06T;

public class RecordParser implements TermParser {

	public Term parse(Parser parser) {
		Record record = new Record();

		parser.eatChar('{');
		while (true) {
			String varname = ((ParserV06T) parser).parseIdentifier();
			parser.eatChar('=');
			Term t = ((ParserV06T) parser).parseExpression();
			record.elems.add(new RecordElement(varname, t));
			if (((ParserV06T) parser).peekNextChar() != ',') {
				break;
			}
			parser.eatChar(',');
		}
		parser.eatChars("}");

		return record;
	}
}
