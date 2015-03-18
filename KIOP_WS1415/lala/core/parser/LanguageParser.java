package lala.core.parser;

import lala.core.syntaxtree.Term;

public interface LanguageParser extends Parser {
	Term start();

	Term start(String s);

}
