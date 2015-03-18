package lala.core.syntaxtree;

import java.util.HashSet;
import java.util.Set;

import lala.core.parser.ParseException;
import lala.core.parser.Parser;

public class VariableParser implements TermParser {

	public Set<String> ignore;

	public VariableParser() {
		ignore = new HashSet<String>();
	}

	public VariableParser(Set<String> ignore) {
		this.ignore = ignore;
	}

	@Override
	public Variable parse(Parser parser) {
		Variable v = new Variable();
		parser.eatChar('(');
		v.identifier = parser.parseIdentifier();
		throwExceptionIfIgnored(v.identifier);
		parser.eatChar(')');
		return v;
	}

	// Terrible: runtime log(n)!!!!
	public void throwExceptionIfIgnored(String s) {
		if (ignore.contains(s))
			throw new ParseException("ups...");
	}
}
