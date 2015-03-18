package lala.v05t_sumTypes.parser.parsetree;

import lala.core.parser.ParseException;
import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v05t_sumTypes.parser.ParserV05T;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class InlParser implements TermParser {

	public Term parse(Parser parser) {
		Inl inlNode = new Inl();
		parser.eatChars("(inl^");
		try {
			inlNode.type = (SumType) ((ParserV05T) parser).parseType();
		} catch (Exception e) {
			throw new ParseException("inl requires a sum type!");
		}
		inlNode.term = ((ParserV05T) parser).parseExpression();
		parser.eatChars(")");
		return inlNode;
	}

}
