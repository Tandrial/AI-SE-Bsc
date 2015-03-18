package lala.v03t_boolsAndInts.parser;

import lala.core.syntaxtree.Term;
import lala.lib.UntypedChurchConstants;

public class LambdaToPrimitiveConverter {

	public String untypedLambdaToTypedTerm(Term t) {
		if (UntypedChurchConstants.TRUE.equals(t.toString()))
			return "(true)";

		if (UntypedChurchConstants.FALSE.equals(t.toString()))
			return "(false)";

		return "("
				+ String.valueOf(UntypedChurchConstants.intFromChurchInt(t
						.toString())) + ")";
	}

}
