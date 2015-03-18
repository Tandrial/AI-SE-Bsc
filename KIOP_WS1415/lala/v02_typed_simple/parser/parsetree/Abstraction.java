package lala.v02_typed_simple.parser.parsetree;

import lala.core.syntaxtree.AbstractionUntyped;
import lala.core.typed.syntaxtree.Type;

public class Abstraction extends AbstractionUntyped {

	public Type type;

	public String toString() {
		return "(λ" + varname.toString() + ":" + type.toString() + "."
				+ body.toString() + ")";
	}

}
