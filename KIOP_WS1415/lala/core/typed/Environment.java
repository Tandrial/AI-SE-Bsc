package lala.core.typed;

import java.util.Hashtable;

import lala.core.typed.syntaxtree.Type;

public class Environment {
	public Hashtable<String, Type> env = new Hashtable<String, Type>();

	public void add(String v, Type t) {
		env.put(v, t);
	}

	public Type typeOfVariableNamed(String v) {
		return env.get(v);
	}

	@SuppressWarnings("unchecked")
	public Environment clone() {
		Environment e = new Environment();
		e.env = (Hashtable<String, Type>) env.clone();
		return e;
	}

	public boolean containsVariable(String s) {
		return env.keySet().contains(s);
	}

}
