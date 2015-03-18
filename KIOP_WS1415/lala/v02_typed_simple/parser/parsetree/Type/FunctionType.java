package lala.v02_typed_simple.parser.parsetree.Type;

import lala.core.typed.syntaxtree.Type;

public class FunctionType extends Type {

	public boolean isFunctionType() {
		return true;
	}

	public Type left;
	public Type right;

	public FunctionType(Type type, Type t) {
		left = type;
		right = t;
	}

	public String toString() {
		return "(" + left.toString() + "->" + right.toString() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionType other = (FunctionType) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

}
