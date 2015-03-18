package lala.v06t_records.parser.parsetree.types;

import lala.core.typed.syntaxtree.Type;

public class RecordTypeElement {
	public String name;
	public Type type;

	public RecordTypeElement(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	public String toString() {
		return name + ":" + type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordTypeElement other = (RecordTypeElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
