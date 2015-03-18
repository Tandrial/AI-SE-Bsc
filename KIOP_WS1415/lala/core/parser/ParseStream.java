package lala.core.parser;

public class ParseStream {

	String parseString = "";
	public int position = 0;

	public ParseStream(String s) {
		parseString = s;
	}

	public void eatChar(char c) {
		if (eatChar() != c) {
			throw new RuntimeException("kaputttt");
		}
	}

	public char currentChar() {
		return parseString.charAt(position);
	}

	public void eatChars(String s) {
		for (int i = 0; i < s.length(); i++) {
			this.eatChar(s.charAt(i));
		}
	}

	public char eatChar() {
		char ret = parseString.charAt(position);
		position = position + 1;
		return ret;
	}

	protected String parseIdentifierUntil(char ending) {
		String identifier = this.parseIdentifierUntilSomethingElse();

		eatChar(ending);

		return identifier;
	}

	/**
	 * Identifier sind alle kleinbuchstaben a-z
	 */
	private boolean isPartOfIdentifier(char charAt) {
		return charAt >= 97 && charAt <= 122;
	}

	public String restString() {
		return parseString.substring(position);
	}

	public void failIfNotFinished() {
		if (parseString.length() != position) {
			throw new ParseException("invalid String");
		}
	}

	public String parseIdentifierUntilSomethingElse() {
		String identifier = "";

		while (isPartOfIdentifier(parseString.charAt(position))) {
			identifier = identifier + parseString.charAt(position);
			position = position + 1;
			if (position > parseString.length())
				return identifier;
		}

		return identifier;
	}

	public String parseNumberUntilSomethingElse() {
		String identifier = "";

		while (isDigit(parseString.charAt(position))) {
			identifier = identifier + parseString.charAt(position);
			position = position + 1;
			if (position >= parseString.length())
				return identifier;
		}

		return identifier;
	}

	private boolean isDigit(char charAt) {
		return charAt >= '0' && charAt <= '9';
	}

}
