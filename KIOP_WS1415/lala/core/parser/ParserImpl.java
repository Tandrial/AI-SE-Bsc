package lala.core.parser;

import lala.core.syntaxtree.ObjectParser;

/**
 * The abstract class Parser ist written with the goal of readability and
 * extensibility, NOT for performance.
 * 
 * The parser class is used not only to parse terms, it is also used to parse
 * types.
 * 
 * Nowadays, there are really no good reasons for writing parsers by hand...
 * (see AntLR!!!)
 * 
 * @author Stefan Hanenberg
 *
 * @param <PARSENODE>
 */
public abstract class ParserImpl implements Parser {

	// Der zu parsende Stream
	public ParseStream parseStream;

	public void eatChar(char c) {
		parseStream.eatChar(c);
	}

	public void eatChars(String s) {
		parseStream.eatChars(s);
	}

	// public <PNODE> PNODE or(ParseCmd<PNODE>[] commands) {
	// int setBackPoint = parseStream.position;
	// for (int i = 0; i < commands.length; i++) {
	// try {
	// return commands[i].parse();
	// } catch (Exception ex) {
	// parseStream.position = setBackPoint;
	// }
	// }
	// throw new ParseException("Parse exception");
	// }

	protected Object or(ObjectParser[] cmds) {
		int setBackPoint = parseStream.position;
		for (int i = 0; i < cmds.length; i++) {
			try {
				return (cmds[i]).parse(this);
			} catch (Exception ex) {
				parseStream.position = setBackPoint;
			}
		}
		throw new ParseException("Parse exception");
	}

	public abstract Object start();

	/**
	 * 
	 * @param s
	 */
	public void init(String s) {
		parseStream = new ParseStream(s);
	}

	public String restString() {
		return parseStream.restString();
	}

	public String parseIdentifier() {
		return parseStream.parseIdentifierUntilSomethingElse();
	}

	public String parseNumber() {
		return parseStream.parseNumberUntilSomethingElse();
	}

	public Object start(String s) {
		this.init(s);
		return this.start();
	}

}
