package lala.v05t_sumTypes.parser;

import lala.core.syntaxtree.ApplicationParser;
import lala.core.syntaxtree.ObjectParser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.VariableParser;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.AbstractionParser;
import lala.v02_typed_simple.parser.parsetree.Type.BoolTypeParser;
import lala.v02_typed_simple.parser.parsetree.Type.CompoundTypeParser;
import lala.v02_typed_simple.parser.parsetree.Type.NumTypeParser;
import lala.v03t_boolsAndInts.parser.parsetree.AndParser;
import lala.v03t_boolsAndInts.parser.parsetree.BoolParser;
import lala.v03t_boolsAndInts.parser.parsetree.NotParser;
import lala.v03t_boolsAndInts.parser.parsetree.NumParser;
import lala.v03t_boolsAndInts.parser.parsetree.OrParser;
import lala.v03t_boolsAndInts.parser.parsetree.PlusParser;
import lala.v03t_boolsAndInts.parser.parsetree.SuccParser;
import lala.v04t_if_simple.parser.ParserV04T;
import lala.v04t_if_simple.parser.parsetree.IfParser;
import lala.v05t_sumTypes.parser.parsetree.CaseParser;
import lala.v05t_sumTypes.parser.parsetree.InlParser;
import lala.v05t_sumTypes.parser.parsetree.InrParser;
import lala.v05t_sumTypes.parser.parsetree.types.SumTypeParser;

public class ParserV05T extends ParserV04T {

	public String[] keywords() {
		return new String[] { "and", "or", "false", "true", "not", "or",
				"succ", "plus", "if", "inl", "inr", "case" };
	};

	public Type parseType() {
		return (Type) or(new ObjectParser[] { new SumTypeParser(),
				new BoolTypeParser(), new NumTypeParser(),
				new CompoundTypeParser() });
	}

	public Term parseExpression() {

		return (Term) or(new ObjectParser[] { new VariableParser(keywordSet()),
				new ApplicationParser(), new AbstractionParser(),
				new NumParser(), new BoolParser(), new NotParser(),
				new PlusParser(), new OrParser(), new SuccParser(),
				new IfParser(), new AndParser(), new InlParser(),
				new InrParser(), new CaseParser()

		});
	}
}
