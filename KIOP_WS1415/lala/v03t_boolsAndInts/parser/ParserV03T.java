package lala.v03t_boolsAndInts.parser;

import lala.core.syntaxtree.ApplicationParser;
import lala.core.syntaxtree.ObjectParser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.VariableParser;
import lala.v02_typed_simple.parser.ParserV02T;
import lala.v02_typed_simple.parser.parsetree.AbstractionParser;
import lala.v03t_boolsAndInts.parser.parsetree.AndParser;
import lala.v03t_boolsAndInts.parser.parsetree.BoolParser;
import lala.v03t_boolsAndInts.parser.parsetree.NotParser;
import lala.v03t_boolsAndInts.parser.parsetree.NumParser;
import lala.v03t_boolsAndInts.parser.parsetree.OrParser;
import lala.v03t_boolsAndInts.parser.parsetree.PlusParser;
import lala.v03t_boolsAndInts.parser.parsetree.PredParser;
import lala.v03t_boolsAndInts.parser.parsetree.SuccParser;

public class ParserV03T extends ParserV02T {

	public String[] keywords() {
		return new String[] { "and", "or", "false", "true", "not", "or",
				"succ", "plus", "pred" };
	};

	public Term parseExpression() {
		return (Term) or(new ObjectParser[] { new VariableParser(keywordSet()),
				new ApplicationParser(), new AbstractionParser(),
				new NumParser(), new BoolParser(), new NotParser(),
				new PlusParser(), new OrParser(), new SuccParser(),
				new AndParser(), new PredParser() });
	}
}
