package lala.core.syntaxtree;

import lala.core.parser.Parser;

public interface TermParser extends ObjectParser {
	Term parse(Parser parser);
}
