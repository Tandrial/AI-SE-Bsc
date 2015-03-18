package lala.core.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lala.core.syntaxtree.Term;

public abstract class LanguageParserImpl extends ParserImpl implements
		LanguageParser {

	public Term start(String s) {
		return (Term) super.start(s);
	}

	public Set<String> keywordSet() {
		String[] k = keywords();
		Set<String> kSet = new HashSet<String>();
		Collections.addAll(kSet, k);
		return kSet;
	}

	/**
	 * This method returns the keywords that should be ignored when variables
	 * are parsed
	 * 
	 * @return the array of Strings that are the ignored keywords
	 */
	public String[] keywords() {
		return new String[0];
	};

}
