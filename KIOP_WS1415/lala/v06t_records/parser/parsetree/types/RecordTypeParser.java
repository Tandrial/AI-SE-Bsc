package lala.v06t_records.parser.parsetree.types;

import lala.core.parser.Parser;
import lala.core.typed.syntaxtree.Type;
import lala.core.typed.syntaxtree.TypeParser;
import lala.v06t_records.parser.ParserV06T;

public class RecordTypeParser implements TypeParser {

	public Type parse(Parser parser) {
		RecordType record = new RecordType();

		parser.eatChar('{');
		while (true) {
			String varname = ((ParserV06T) parser).parseIdentifier();
			parser.eatChar(':');
			Type t = ((ParserV06T) parser).parseType();
			record.elems.add(new RecordTypeElement(varname, t));
			if (((ParserV06T) parser).peekNextChar() != ',') {
				break;
			}
			parser.eatChar(',');
		}
		parser.eatChar('}');

		return record;
	}
}
