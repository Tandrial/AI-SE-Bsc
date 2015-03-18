package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.Language;
import lala.core.reduction.Applyable;
import lala.core.syntaxtree.Term;
import lala.core.typed.syntaxtree.Type;
import lala.v01_untyped_simple.parser.ParserV01U;
import lala.v01_untyped_simple.reduction.normalorder.NormalOrder;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;
import lala.v03t_boolsAndInts.parser.ExpressionToUntypedLambdaConverter;
import lala.v03t_boolsAndInts.parser.LambdaToPrimitiveConverter;
import lala.v03t_boolsAndInts.parser.PrimitiveToTermConverter;

/**
 * WrappedTerms repräsentieren Terme, die als Zwischenergebnisse von Primitiven
 * Termen existieren. Wenn zB eine Function add mit nur einem Parameter
 * aufgerufen wird, ist die Zwischenlösung eine Function, die Intern
 * 
 */
public class WrappedTerm extends Term implements Applyable {
	public Term wrappedTerm;
	public Type wrappedType;

	public WrappedTerm(Term function, Term param, Type t) {
		wrappedType = t;
		String untypedExpression = "("
				+ new ExpressionToUntypedLambdaConverter().printout(function)
				+ new ExpressionToUntypedLambdaConverter().printout(param)
				+ ")";

		Language l = new Language(new ParserV01U(), new NormalOrder());
		wrappedTerm = l.run(untypedExpression);
	}

	@Override
	public Term replaceFreeVariable(String v, Term replacement) {
		wrappedTerm.replaceFreeVariable(v, replacement);
		return this;
	}

	public boolean isAbstraction() {
		return wrappedTerm.isAbstraction();
	}

	public Term apply(Term right) {

		// Konvertiere rechten Term in einen UNGETYPTEN term
		Language l = new Language(new ParserV01U(), new NormalOrder());
		right = l.run(new ExpressionToUntypedLambdaConverter().printout(right));

		wrappedTerm = ((Applyable) wrappedTerm).apply(right);
		wrappedType = ((FunctionType) wrappedType).right;

		while (new NormalOrder().isReducible(wrappedTerm))
			wrappedTerm = new NormalOrder().reduce(wrappedTerm);

		if (wrappedType.isPrimitiveType()) {
			return new PrimitiveToTermConverter()
					.untypedLambdaToTypedTerm(wrappedTerm);
		}

		return this;
	}

	public String toString() {
		if (!wrappedType.isPrimitiveType()) {
			System.out.println(this.wrappedTerm);
			return "#WrappedUntypedTerm#";
		} else {
			return new LambdaToPrimitiveConverter()
					.untypedLambdaToTypedTerm(wrappedTerm);
		}
	}

	public boolean isReducible() {
		return new NormalOrder().isReducible(wrappedTerm);
	}

}
