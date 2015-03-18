package lala.v05t_sumTypes.reduction.callbyvalue;

import lala.core.syntaxtree.Term;
import lala.core.typed.syntaxtree.Type;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v04t_if_simple.parser.parsetree.If;
import lala.v04t_if_simple.reduction.callbyvalue.CallByValueV04;
import lala.v05t_sumTypes.parser.parsetree.Case;
import lala.v05t_sumTypes.parser.parsetree.Inl;
import lala.v05t_sumTypes.parser.parsetree.Inr;
import lala.v05t_sumTypes.parser.parsetree.types.SumType;
import lala.v05t_sumTypes.typecheck.TypeCheckV05;

public class CallByValueV05 extends CallByValueV04 {

	public Term reduce(Inl t) {
		t.term = reduce(t.term);
		return t;
	}

	public Term reduce(Inr t) {
		t.term = reduce(t.term);
		return t;
	}

	public Term reduce(Case c) {
		if (isReducible(c.term)) {
			c.term = reduce(c.term);
			return c;
		} else if (c.term instanceof Inl) {
			return c.inlTerm.replaceFreeVariable(c.inlVar, ((Inl) c.term).term);
		} else if (c.term instanceof Inr) {
			return c.inrTerm.replaceFreeVariable(c.inrVar, ((Inr) c.term).term);
		}
		return c;
	}

	public Term reduce(If t) {

		TypeCheckV05 typeChecker = new TypeCheckV05();
		Type typeThen = typeChecker.typeOf(t.thenBranch);
		Type typeElse = typeChecker.typeOf(t.elseBranch);

		if (isReducible(t.condition)) {
			t.condition = reduce(t.condition);
			return t;
		} else if (typeThen.equals(typeElse)) {
			// The condition should be now true or false
			if (t.condition.equals(Bool.TRUE)) {
				return t.thenBranch;
			} else {
				return t.elseBranch;
			}
		} else {
			SumType newType = new SumType(typeThen, typeElse);
			if (t.condition.equals(Bool.TRUE)) {
				return new Inl(newType, t.thenBranch);
			} else {
				return new Inr(newType, t.elseBranch);
			}
		}

	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV05().isReducible(t);
	}
}
