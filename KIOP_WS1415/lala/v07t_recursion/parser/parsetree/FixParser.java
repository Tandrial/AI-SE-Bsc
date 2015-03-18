package lala.v07t_recursion.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v07t_recursion.parser.ParserV07T;

public class FixParser implements TermParser {

	public Term parse(Parser parser) {
		Fix f = new Fix();
		parser.eatChars("(fix");
		f.term = ((ParserV07T) parser).parseExpression();
		parser.eatChar(')');
		return f;
	}
}
