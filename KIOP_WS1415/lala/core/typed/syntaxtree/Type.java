package lala.core.typed.syntaxtree;

public abstract class Type {
	public boolean isFunctionType() {
		return false;
	}

	public boolean isPrimitiveType() {
		return false;
	}
}
