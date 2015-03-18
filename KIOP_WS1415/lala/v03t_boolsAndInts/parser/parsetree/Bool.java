package lala.v03t_boolsAndInts.parser.parsetree;

import lala.core.syntaxtree.ExpressionConstant;
import lala.core.syntaxtree.Term;

public class Bool extends ExpressionConstant {

	public static Bool TRUE = new Bool(true);
	public static Bool FALSE = new Bool(false);

	public boolean value;

	public Bool() {
	}

	public Bool(boolean b) {
		value = b;
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
		Bool other = (Bool) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
