package lala.core.untyped;

import lala.core.parser.LanguageParser;

public interface UntypedLanguage<ParserType extends LanguageParser, EvaluatorType extends Evaluator> {

	ParserType parser();

	EvaluatorType evaluator();
}
