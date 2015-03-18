package lala.v02_typed_simple.parser.parsetree.Type;

import lala.core.parser.Parser;
import lala.core.typed.syntaxtree.Type;
import lala.core.typed.syntaxtree.TypeParser;
import lala.v02_typed_simple.parser.ParserV02T;

public class CompoundTypeParser implements TypeParser {

	public FunctionType parse(Parser parser) {
		parser.eatChar('(');
		Type left = ((ParserV02T) parser).parseType();
		parser.eatChars("->");
		Type right = ((ParserV02T) parser).parseType();
		parser.eatChar(')');
		return new FunctionType(left, right);
	}

}