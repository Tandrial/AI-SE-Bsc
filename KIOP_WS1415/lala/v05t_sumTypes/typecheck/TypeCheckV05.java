package lala.v05t_sumTypes.typecheck;

import lala.core.typed.Environment;
import lala.core.typed.TypeError;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.BoolType;
import lala.v04t_if_simple.parser.parsetree.If;
import lala.v04t_if_simple.typecheck.TypeCheckV04;
import lala.v05t_sumTypes.parser.parsetree.Case;
import lala.v05t_sumTypes.parser.parsetree.Inl;
import lala.v05t_sumTypes.parser.parsetree.Inr;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;

public class TypeCheckV05 extends TypeCheckV04 {

	public Type typeOf(Inl t, Environment e) {
		if (!typeOf(t.term, e).equals(t.type.left))
			throw new TypeError("expected type: " + t.type.left + " but got "
					+ typeOf(t.term));
		return t.type;
	}

	public Type typeOf(Inr t, Environment e) {
		if (!typeOf(t.term, e).equals(t.type.right))
			throw new TypeError("expected type: " + t.type.right + " but got "
					+ typeOf(t.term));
		return t.type;
	}

	public Type typeOf(Case c, Environment e) {
		if (!(c.inlSumType.equals(c.inrSumType)))
			throw new TypeError("expected type the same sum types in case ");

		if (!(typeOf(c.term)).equals(c.inlSumType))
			throw new TypeError("term must have the appropriate sum type");

		Environment e1 = e.clone();
		e1.add(c.inlVar, c.inlSumType.left);
		Type t1 = typeOf(c.inlTerm, e1);

		Environment e2 = e.clone();
		e2.add(c.inrVar, c.inrSumType.right);
		Type t2 = typeOf(c.inrTerm, e2);

		if (!(t1.equals(t2)))
			throw new TypeError("Terms must have the same type");

		return t1;
	}

	public Type typeOf(If t, Environment e) {

		Type typeThen = typeOf(t.thenBranch, e);
		Type typeElse = typeOf(t.elseBranch, e);

		SumType newType = new SumType();
		newType.left = typeThen;
		newType.right = typeElse;

		Type type = typeOf(t.condition, e);

		// TODO: Function Type

		if (!(type instanceof BoolType))
			throw new TypeError("If requires a BoolType in the condition"
					+ t.condition);

		Type type2 = typeOf(t.thenBranch, e);
		Type type3 = typeOf(t.elseBranch, e);

		if (!type2.equals(type3))
			return newType;

		return type2;
	}

}
