package lala.v06t_records.parser.parsetree.types;

import java.util.ArrayList;

import lala.core.typed.syntaxtree.Type;

public class RecordType extends Type {

	public ArrayList<RecordTypeElement> elems = new ArrayList<RecordTypeElement>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (RecordTypeElement r : elems) {
			sb.append(r);
			sb.append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordType other = (RecordType) obj;
		if (elems == null) {
			if (other.elems != null)
				return false;
		} else if (!elems.equals(other.elems))
			return false;
		return true;
	}
}
