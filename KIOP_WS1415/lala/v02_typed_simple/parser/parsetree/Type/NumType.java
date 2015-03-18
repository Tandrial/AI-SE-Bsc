package lala.v02_typed_simple.parser.parsetree.Type;

import lala.core.typed.syntaxtree.Type;

public class NumType extends Type {

	public String toString() {
		return "Num";
	}

	public boolean equals(Object t) {
		return t.getClass().equals(this.getClass());
	}

	public boolean isPrimitiveType() {
		return true;
	}

}
