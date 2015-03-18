package lala.core.parser;

public interface Parser {
	Object start();

	Object start(String s);

	void eatChar(char c);

	void eatChars(String c);

	String parseIdentifier();

	String parseNumber();
}
