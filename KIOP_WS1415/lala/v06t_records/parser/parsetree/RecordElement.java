package lala.v06t_records.parser.parsetree;

import lala.core.syntaxtree.Term;

public class RecordElement {
	public String name;
	public Term value;

	public RecordElement(String name, Term type) {
		this.name = name;
		this.value = type;
	}

	public String toString() {
		return name + "=" + value;
	}
}
