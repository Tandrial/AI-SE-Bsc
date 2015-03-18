package lala.v06t_records.reduction.callbyvalue;

import lala.v05t_sumTypes.reduction.callbyvalue.IsReducibleCallByValueV05;
import lala.v06t_records.parser.parsetree.Projection;
import lala.v06t_records.parser.parsetree.Record;
import lala.v06t_records.parser.parsetree.RecordElement;

public class IsReducibleCallByValueV06 extends IsReducibleCallByValueV05 {

	public Boolean isReducible(Projection t) {

		if (t.term instanceof Record) {
			for (RecordElement r : ((Record) t.term).elems) {
				if (r.name.equals(t.selection))
					return true;
			}
		}
		return isReducible(t.term);
	}

	public Boolean isReducible(Record t) {
		for (RecordElement r : t.elems) {
			if (isReducible(r.value))
				return true;
		}
		return false;
	}
}
