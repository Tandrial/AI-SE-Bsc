package lala.v02_typed_simple.parser.parsetree.Type;

import lala.core.parser.Parser;
import lala.core.typed.syntaxtree.TypeParser;

public class NumTypeParser implements TypeParser {

	public NumType parse(Parser parser) {
		parser.eatChars("Num");
		return new NumType();
	}

}