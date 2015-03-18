package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstant;
import lala.core.syntaxtree.Term;

public class Num extends ExpressionConstant {
	public int value;

	public Num() {
	}

	public Num(int i) {
		value = i;
	}

	@Override
	public Term replaceFreeVariable(String v, Term replacement) {
		return this;
	}

	@Override
	public String toString() {
		return "(" + String.valueOf(value) + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Num other = (Num) obj;
		if (value != other.value)
			return false;
		return true;
	}

}
