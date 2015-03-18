package lala.core;

import lala.core.parser.LanguageParser;
import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.Term;
import lala.core.typed.Environment;
import lala.core.typed.TypeChecker;
import lala.core.typed.syntaxtree.Type;

public class TypedLanguage extends Language {

	public TypeChecker typeChecker;

	public TypedLanguage(LanguageParser parser,
			ReductionStrategy reductionStrategy, TypeChecker typeChecker) {
		super(parser, reductionStrategy);
		this.typeChecker = typeChecker;
	}

	public Term parseProgram(String s) {
		Term t = super.parseProgram(s);
		return t;
	}

	public Type typeOf(Term t) {
		return typeChecker.typeOf(t, new Environment());
	}

	public Type typeOf(String s) {
		Term t = parseProgram(s);
		return typeChecker.typeOf(t, new Environment());
	}

}
