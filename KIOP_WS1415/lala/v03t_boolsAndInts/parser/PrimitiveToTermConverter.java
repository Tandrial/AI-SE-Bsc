package lala.v03t_boolsAndInts.parser;

import lala.core.syntaxtree.Term;
import lala.lib.UntypedChurchConstants;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v03t_boolsAndInts.parser.parsetree.Num;

public class PrimitiveToTermConverter {
	public Term untypedLambdaToTypedTerm(Term t) {
		if (UntypedChurchConstants.TRUE.equals(t.toString()))
			return new Bool(true);

		if (UntypedChurchConstants.FALSE.equals(t.toString()))
			return new Bool(false);

		return new Num(UntypedChurchConstants.intFromChurchInt(t.toString()));
	}
}
