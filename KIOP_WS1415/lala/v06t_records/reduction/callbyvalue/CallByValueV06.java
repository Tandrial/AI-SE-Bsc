package lala.v06t_records.reduction.callbyvalue;

import lala.core.syntaxtree.Term;
import lala.v05t_sumTypes.reduction.callbyvalue.CallByValueV05;
import lala.v06t_records.parser.parsetree.Projection;
import lala.v06t_records.parser.parsetree.Record;
import lala.v06t_records.parser.parsetree.RecordElement;

public class CallByValueV06 extends CallByValueV05 {

	public Term reduce(Projection t) {

		if (isReducible(t.term)) {
			t.term = reduce(t.term);
		} else {
			if (t.term instanceof Record) {
				for (RecordElement r : ((Record) t.term).elems) {
					if (r.name.equals(t.selection))
						return r.value;
				}
			} else {
				throw new RuntimeException("Projection total kaputt.");
			}
		}
		return t;
	}

	public Term reduce(Record t) {
		for (RecordElement r : t.elems) {
			if (isReducible(r.value))
				r.value = reduce(r.value);
		}
		return t;
	}

	public Boolean isReducible(Term t) {
		return new IsReducibleCallByValueV06().isReducible(t);
	}
}
