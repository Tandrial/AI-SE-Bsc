package lala.v04t_if_simple.typecheck;

import lala.core.typed.Environment;
import lala.core.typed.TypeError;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v03t_boolsAndInts.typecheck.TypeCheckV03;
import lala.v04t_if_simple.parser.parsetree.If;

public class TypeCheckV04 extends TypeCheckV03 {

	public Type typeOf(If t, Environment e) {
		Type type = (BoolType) typeOf(t.condition);

		if (!(type instanceof BoolType))
			throw new TypeError("If requires a BoolType in the condition"
					+ t.condition);

		Type type2 = typeOf(t.thenBranch);
		Type type3 = typeOf(t.elseBranch);

		if (!type2.equals(type3))
			throw new TypeError("Then and else in if require the same type");

		return type2;
	}

}
