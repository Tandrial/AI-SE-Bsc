package lala.v04t_if_simple.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v04t_if_simple.parser.ParserV04T;

public class IfParser implements TermParser {

	public Term parse(Parser parser) {
		If ifNode = new If();
		parser.eatChars("(if");
		ifNode.condition = ((ParserV04T) parser).parseExpression();
		ifNode.thenBranch = ((ParserV04T) parser).parseExpression();
		ifNode.elseBranch = ((ParserV04T) parser).parseExpression();
		parser.eatChar(')');
		return ifNode;
	}

}
