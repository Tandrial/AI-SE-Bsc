package lala.core.typed.syntaxtree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.ObjectParser;

public interface TypeParser extends ObjectParser {
	Type parse(Parser parser);
}
