package lala.v07t_recursion.typcheck;

import lala.core.typed.Environment;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;
import lala.v06t_records.typecheck.TypeCheckV06;
import lala.v07t_recursion.parser.parsetree.Eq;
import lala.v07t_recursion.parser.parsetree.Fix;

public class TypeCheckV07 extends TypeCheckV06 {

	public Type typeOf(Eq t, Environment e) {

		Type type_left = typeOf(t.left, e);
		Type type_right = typeOf(t.right, e);

		if (!type_left.equals(type_right)) {
			throw new RuntimeException(
					"The arguments of eq need to have the same type, but have: "
							+ type_left + " and " + type_right);
		}

		return new BoolType();
	}

	public Type typeOf(Fix t, Environment e) {

		// fix(§sum:Num->Num.§n:Num.if((eq(n)(0))(0)(plus(n)((sum) (-n (1)))))))

		Type type = typeOf(t.term, e);

		if (!(type instanceof FunctionType)) {
			throw new RuntimeException("Rekursion geht nur mit Functionen!");
		}

		FunctionType fun_t = (FunctionType) type;

		if (!fun_t.left.equals(fun_t.right)) {
			throw new RuntimeException(
					"The function must have the same parameter/return type. But has:  "
							+ fun_t);
		}
		return fun_t.left;
	}
}
