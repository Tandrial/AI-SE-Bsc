package lala.core.syntaxtree;

import java.util.ArrayList;

import lala.core.parser.Parser;

public abstract class ExpressionConstantParser implements TermParser {

	public static ArrayList<String> ExpressionConstantPool = new ArrayList<String>();

	public static void addExpressionConstant(String s) {
		ExpressionConstantPool.add(s);
	}

	public static void throwExceptionIfOtherParseNode(String s) {
		if (ExpressionConstantPool.contains(s) || isInteger(s))
			throw new RuntimeException("different...");
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public ExpressionConstant parse(Parser parser) {
		ExpressionConstant o = createObject();
		parser.eatChar('(');
		parser.eatChars(expressionConstantTerm().toString());
		parser.eatChar(')');
		return o;
	}

	public abstract ExpressionConstant createObject();

	public abstract String expressionConstantTerm();

}
