package lala.v02_typed_simple.typecheck;

import lala.core.dispatcher.Dispatcher;
import lala.core.syntaxtree.Application;
import lala.core.syntaxtree.Term;
import lala.core.syntaxtree.Variable;
import lala.core.typed.Environment;
import lala.core.typed.TypeChecker;
import lala.core.typed.TypeError;
import lala.core.typed.syntaxtree.Type;
import lala.v02_typed_simple.parser.parsetree.Abstraction;
import lala.v02_typed_simple.parser.parsetree.Type.FunctionType;

public class TypeCheckV02 extends Dispatcher<Type> implements TypeChecker {

	@Override
	public String methodPrefix() {
		return "typeOf";
	}

	@Override
	public Type typeOf(Term t, Environment e) {
		return super.dispatch(t, e);
	}

	public Type typeOf(Variable v, Environment e) {
		if (!e.containsVariable(v.identifier))
			throw new TypeError("Variable unknown " + v);
		return e.typeOfVariableNamed(v.identifier);
	}

	public Type typeOf(Abstraction a, Environment e) {
		Environment e2 = e.clone();
		e2.add(a.varname, a.type);
		Type t = typeOf(a.body, e2);
		return new FunctionType(a.type, t);
	}

	public Type typeOf(Application a, Environment e) {
		Type left = typeOf(a.left, e);

		if (!left.isFunctionType())
			throw new TypeError("Function type expected at application " + a);

		Type right = typeOf(a.right, e);

		if (!((FunctionType) left).left.equals(right))
			throw new TypeError("Expected input type "
					+ ((FunctionType) left).left.toString() + ", actual type "
					+ right.toString() + " in " + a.toString());

		return ((FunctionType) left).right;
	}

	@Override
	public boolean matches(Type t1, Type t2) {
		return t1.equals(t2);
	}

	@Override
	public Type typeOf(Term t) {
		return this.typeOf(t, new Environment());
	}

}
