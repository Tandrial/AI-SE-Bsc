package lala.v05t_sumTypes.parser.parsetree;

import lala.core.parser.ParseException;
import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v05t_sumTypes.parser.ParserV05T;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class InrParser implements TermParser {

	public Term parse(Parser parser) {
		Inr inrNode = new Inr();
		parser.eatChars("(inr^");
		try {
			inrNode.type = (SumType) ((ParserV05T) parser).parseType();
		} catch (Exception e) {
			throw new ParseException("inr requires a sum type!");
		}
		inrNode.term = ((ParserV05T) parser).parseExpression();
		parser.eatChars(")");
		return inrNode;
	}

}
