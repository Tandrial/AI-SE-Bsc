package lala.v01_untyped_simple.parser;

import lala.core.parser.LanguageParserImpl;
import lala.core.syntaxtree.AbstractionUntypedParser;
import lala.core.syntaxtree.ApplicationParser;
import lala.core.syntaxtree.ObjectParser;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.VariableParser;

public class ParserV01U extends LanguageParserImpl {

	public Term start() {
		return this.parseExpression();
	}

	public Term parseExpression() {
		return (Term) or(new ObjectParser[] { new VariableParser(),
				new AbstractionUntypedParser(), new ApplicationParser() });
	}

}
