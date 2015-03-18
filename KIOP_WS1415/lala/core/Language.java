package lala.core;

import lala.core.parser.LanguageParser;
import lala.core.reduction.ReductionStrategy;
import lala.core.syntaxtree.Term;

/**
 * A language consists of a Language Parser and a Reduction Strategy (such as
 * NormalOrder or CallByValue). In addition, a typed language has a type
 * checker.
 * 
 * In order to run a program, the method run needs to be invoked with a String
 * as a parameter. A program can be passed as a String (that will be parsed
 * first) or as a syntax tree (which is a Term).
 * 
 * @author Stefan Hanenberg
 *
 */
public class Language {

	public LanguageParser parser;
	public ReductionStrategy reductionStrategy;

	public Language(LanguageParser parser, ReductionStrategy reductionStrategy) {
		this.parser = parser;
		this.reductionStrategy = reductionStrategy;
	}

	/**
	 * Runs the program that is represented by a String
	 * 
	 * @param s
	 * @return
	 */
	public Term run(String s) {

		Term result = parseProgram(s);

		while (reductionStrategy.isReducible(result)) {
			result = reductionStrategy.reduce(result);
		}

		return result;
	}

	/**
	 * Runs the program that is represented by a Syntax Tree (a Term)
	 * 
	 * @param s
	 * @return
	 */
	public Term run(Term term) {

		while (reductionStrategy.isReducible(term)) {
			term = reductionStrategy.reduce(term);
		}

		return term;
	}

	/**
	 * Parses the program.
	 * 
	 * @param s
	 * @return
	 */
	public Term parseProgram(String program) {
		return parser.start(program);
	}

}
