package lala.v02_typed_simple.parser;

import lala.core.syntaxtree.ApplicationParser;
import lala.core.syntaxtree.ObjectParser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.VariableParser;
import lala.core.typed.syntaxtree.Type;
import lala.v01_untyped_simple.parser.ParserV01U;
import lala.v02_typed_simple.parser.parsetree.AbstractionParser;
import lala.v02_typed_simple.parser.parsetree.Type.BoolTypeParser;
import lala.v02_typed_simple.parser.parsetree.Type.CompoundTypeParser;
import lala.v02_typed_simple.parser.parsetree.Type.NumTypeParser;

public class ParserV02T extends ParserV01U {

	public Term start() {
		return this.parseExpression();
	}

	public Type parseType() {
		return (Type) or(new ObjectParser[] { new BoolTypeParser(),
				new NumTypeParser(), new CompoundTypeParser() });
	}

	public Term parseExpression() {
		return (Term) or(new ObjectParser[] { new VariableParser(),
				new AbstractionParser(), new ApplicationParser() });
	}
}