package lala.v02_typed_simple.parser.parsetree.Type;

import lala.core.parser.Parser;
import lala.core.typed.syntaxtree.TypeParser;

public class BoolTypeParser implements TypeParser {

	public BoolType parse(Parser parser) {
		parser.eatChars("Bool");
		return new BoolType();
	}

}