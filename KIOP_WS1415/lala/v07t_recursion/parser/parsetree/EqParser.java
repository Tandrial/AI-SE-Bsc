package lala.v07t_recursion.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v07t_recursion.parser.ParserV07T;

public class EqParser implements TermParser {

	public Term parse(Parser parser) {
		Eq eq = new Eq();

		parser.eatChars("(=");
		eq.left = ((ParserV07T) parser).parseExpression();
		eq.right = ((ParserV07T) parser).parseExpression();
		parser.eatChar(')');
		return eq;
	}
}
