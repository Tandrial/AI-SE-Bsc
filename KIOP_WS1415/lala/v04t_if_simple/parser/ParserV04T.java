package lala.v04t_if_simple.parser;

import lala.core.syntaxtree.ApplicationParser;
import lala.core.syntaxtree.ObjectParser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.VariableParser;
import lala.v02_typed_simple.parser.parsetree.AbstractionParser;
import lala.v03t_boolsAndInts.parser.ParserV03T;
import lala.v03t_boolsAndInts.parser.parsetree.AndParser;
import lala.v03t_boolsAndInts.parser.parsetree.BoolParser;
import lala.v03t_boolsAndInts.parser.parsetree.NotParser;
import lala.v03t_boolsAndInts.parser.parsetree.NumParser;
import lala.v03t_boolsAndInts.parser.parsetree.OrParser;
import lala.v03t_boolsAndInts.parser.parsetree.PlusParser;
import lala.v03t_boolsAndInts.parser.parsetree.SuccParser;
import lala.v04t_if_simple.parser.parsetree.IfParser;

public class ParserV04T extends ParserV03T {

	public String[] keywords() {
		return new String[] { "and", "or", "false", "true", "not", "or",
				"succ", "plus", "if" };
	};

	public Term parseExpression() {
		return (Term) or(new ObjectParser[] { new VariableParser(keywordSet()),
				new ApplicationParser(), new AbstractionParser(),
				new NumParser(), new BoolParser(), new NotParser(),
				new PlusParser(), new OrParser(), new SuccParser(),
				new IfParser(), new AndParser() });
	}
}
