package lala.v06t_records.parser.parsetree;

import java.util.ArrayList;

import lala.core.syntaxtree.Term;

public class Record extends Term {

	public ArrayList<RecordElement> elems = new ArrayList<RecordElement>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (RecordElement r : elems) {
			sb.append(r);
			sb.append(',');
		}
		if (elems.size() > 0)
			sb.deleteCharAt(sb.length() - 1);

		sb.append('}');
		return sb.toString();
	}

	public Term getElem(String name) {
		for (RecordElement r : elems) {
			if (r.name.equals(name))
				return r.value;
		}
		return null;
	}

	@Override
	public Term replaceFreeVariable(String v, Term replacement) {
		for (RecordElement r : elems) {
			if (r.name.equals(v))
				r.value.replaceFreeVariable(v, replacement);
		}
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (elems == null) {
			if (other.elems != null)
				return false;
		} else if (!elems.equals(other.elems))
			return false;
		return true;
	}

}
