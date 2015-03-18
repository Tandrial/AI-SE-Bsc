package lala.core.util;

import lala.core.dispatcher.Dispatcher;
import lala.core.syntaxtree.AbstractionUntyped;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;

public class ToStringWriter extends Dispatcher<String> {

	@Override
	public String methodPrefix() {
		return "writeToString";
	}

	public String writeToString(Term t) {
		return super.dispatch(t);
	}

	public String writeToString(AbstractionUntyped abstraction) {
		return "(λ" + abstraction.varname.toString() + "."
				+ writeToString(abstraction.body) + ")";
	}

	public String writeToString(Application application) {
		return "(" + writeToString(application.left)
				+ writeToString(application.right) + ")";
	}

	public String writeToString(Variable variable) {
		return "(" + variable.identifier + ")";
	}

}
