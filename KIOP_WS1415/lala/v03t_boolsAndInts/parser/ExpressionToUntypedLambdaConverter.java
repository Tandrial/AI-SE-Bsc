package lala.v03t_boolsAndInts.parser;

import lala.core.dispatcher.Dispatcher;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;
import lala.lib.UntypedChurchConstants;
import lala.v02_typed_simple.parser.parsetree.Abstraction;
import lala.v03t_boolsAndInts.parser.parsetree.And;
import lala.v03t_boolsAndInts.parser.parsetree.Bool;
import lala.v03t_boolsAndInts.parser.parsetree.Not;
import lala.v03t_boolsAndInts.parser.parsetree.Num;
import lala.v03t_boolsAndInts.parser.parsetree.Or;
import lala.v03t_boolsAndInts.parser.parsetree.Plus;
import lala.v03t_boolsAndInts.parser.parsetree.Pred;
import lala.v03t_boolsAndInts.parser.parsetree.Succ;
import lala.v03t_boolsAndInts.parser.parsetree.WrappedTerm;

public class ExpressionToUntypedLambdaConverter extends Dispatcher<String> {

	public String methodPrefix() {
		return "printout";
	}

	public String printout(Term t) {
		return (String) super.dispatch(t);
	}

	public String printout(Variable v) {
		return v.toString();
	}

	public String printout(Abstraction a) {
		return "(λ" + a.varname.toString() + "." + this.printout(a.body) + ")";
	}

	public String printout(Application a) {
		return this.printout(a.left) + this.printout(a.right);
	}

	public String printout(And a) {
		return UntypedChurchConstants.AND;
	}

	public String printout(Or a) {
		return UntypedChurchConstants.OR;
	}

	public String printout(Bool a) {
		return a.value ? UntypedChurchConstants.TRUE
				: UntypedChurchConstants.FALSE;
	}

	public String printout(Not a) {
		return UntypedChurchConstants.NOT;
	}

	public String printout(Num t) {
		return UntypedChurchConstants.churchInt(t.value);
	}

	public String printout(WrappedTerm t) {
		return printout(t.wrappedTerm);
	}

	public String printout(Succ s) {
		return UntypedChurchConstants.SUCC;
	}

	public String printout(Plus p) {
		return UntypedChurchConstants.PLUS;
	}

	public String printout(Pred p) {
		return UntypedChurchConstants.PRED;
	}

}
