package lala.v06t_records.typecheck;

import lala.core.syntaxtree.Application;
import lala.core.typed.Environment;
import lala.core.typed.TypeError;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;
import lala.v05t_sumTypes.typecheck.TypeCheckV05;
import lala.v06t_records.parser.parsetree.Projection;
import lala.v06t_records.parser.parsetree.Record;
import lala.v06t_records.parser.parsetree.types.RecordType;
import lala.v06t_records.parser.parsetree.types.RecordTypeElement;

public class TypeCheckV06 extends TypeCheckV05 {

	public Type typeOf(Record t, Environment e) {

		RecordType typeRec = new RecordType();

		for (int i = 0; i < t.elems.size(); i++) {

			Type typeElem = typeOf(t.elems.get(i).value, e);
			typeRec.elems.add(new RecordTypeElement(t.elems.get(i).name,
					typeElem));
		}
		return typeRec;
	}

	public Type typeOf(Projection t, Environment e) {
		RecordType recType = (RecordType) typeOf(t.term, e);

		for (RecordTypeElement r : recType.elems) {
			if (r.name.equals(t.selection)) {
				return r.type;
			}
		}
		throw new TypeError("Element not found.");
	}

	public Type typeOf(Application a, Environment e) {
		Type left = typeOf(a.left, e);

		if (!left.isFunctionType())
			throw new TypeError("Function type expected at application " + a);

		Type right = typeOf(a.right, e);

		if (!(isSubtype(((FunctionType) left).left, right)))
			throw new TypeError("Expected input type "
					+ ((FunctionType) left).left.toString() + ", actual type "
					+ right.toString() + " in " + a.toString());

		return ((FunctionType) left).right;
	}

	public boolean isSubtype(Type ober, Type unter) {
		if (ober.equals(unter))
			return true;
		else if (ober instanceof FunctionType && unter instanceof FunctionType) {
			FunctionType fun_s = (FunctionType) ober;
			FunctionType fun_t = (FunctionType) unter;
			return isSubtype(fun_t.left, fun_s.left)
					&& isSubtype(fun_s.right, fun_t.right);
		} else if (ober instanceof RecordType && unter instanceof RecordType) {
			RecordType rec_ober = (RecordType) ober;
			RecordType rec_unter = (RecordType) unter;

			if (rec_unter.elems.size() >= rec_ober.elems.size()) {
				for (int i = 0; i < rec_ober.elems.size(); i++) {
					if (!isSubtype(rec_ober.elems.get(i).type,
							rec_unter.elems.get(i).type))
						return false;
				}
				return true;
			}
		}
		return false;
	}
}
