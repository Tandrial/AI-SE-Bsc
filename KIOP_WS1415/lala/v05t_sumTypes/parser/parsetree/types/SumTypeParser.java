package lala.v05t_sumTypes.parser.parsetree.types;

import lala.core.parser.Parser;
import lala.core.typed.syntaxtree.TypeParser;
import lala.v05t_sumTypes.parser.ParserV05T;

public class SumTypeParser implements TypeParser {

	public SumType parse(Parser parser) {
		SumType t = new SumType();
		parser.eatChars("(");
		t.left = ((ParserV05T) parser).parseType();
		parser.eatChars("+");
		t.right = ((ParserV05T) parser).parseType();
		parser.eatChars(")");
		return t;
	}

}