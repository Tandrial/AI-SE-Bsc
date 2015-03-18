package lala.v05t_sumTypes.parser.parsetree;

import lala.core.parser.Parser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.TermParser;
import lala.v05t_sumTypes.parser.ParserV05T;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class CaseParser implements TermParser {

	public Term parse(Parser parser) {
		Case $case = new Case();
		parser.eatChars("(case");
		$case.term = parseMyExpression(parser);
		parser.eatChars("inl^");
		$case.inlSumType = (SumType) ((ParserV05T) parser).parseType();
		parser.eatChars(" ");
		$case.inlVar = parser.parseIdentifier();
		parser.eatChars("=>");
		$case.inlTerm = parseMyExpression(parser);
		parser.eatChars("||");
		parser.eatChars("inr^");
		$case.inrSumType = (SumType) ((ParserV05T) parser).parseType();
		parser.eatChars(" ");
		$case.inrVar = parser.parseIdentifier();
		parser.eatChars("=>");
		$case.inrTerm = parseMyExpression(parser);
		parser.eatChars(")");
		return $case;
	}

	protected Term parseMyExpression(Parser parser) {
		return ((ParserV05T) parser).parseExpression();
	}

}
