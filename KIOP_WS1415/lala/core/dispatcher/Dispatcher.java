package lala.core.dispatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * The dispatcher invokes the "right" method on the syntax tree, as long as
 * "right" general method is invoked. Subclasses need to provide the method
 * "prefix" that is used to termine the target methods for dispatching.
 * 
 * An example implementation can be found in ToStringWriter.
 * 
 * @author Stefan Hanenberg
 *
 * @param <RetType>
 *            defines the return type for the dispatched methods.
 */
public abstract class Dispatcher<RetType> {

	@SuppressWarnings("unchecked")
	public RetType dispatch(Object t) {
		try {
			return (RetType) getMethodForType(t.getClass()).invoke(this, t);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public RetType dispatch(Object t, Object t2) {
		try {
			return (RetType) getMethodForType(t.getClass()).invoke(this,
					new Object[] { t, t2 });
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Gibt beste Methode, auf die Parameter passt.
	 */
	public Method getMethodForType(Class<?> t) {
		ArrayList<Method> l = getVisitMethods();
		for (Method m : l) {
			if (m.getParameterTypes()[0].equals(t)) {
				return m;
			}
		}
		System.err.println(t.getName());
		throw new RuntimeException("Not found method \"" + methodPrefix()
				+ "\"  with parameter type " + t.getName() + " in "
				+ this.getClass().getName());
	}

	public ArrayList<Method> getVisitMethods() {
		Method[] m = this.getClass().getMethods();
		ArrayList<Method> l = new ArrayList<Method>();
		for (int i = 0; i < m.length; i++) {
			if (isVisitMethod(m[i]))
				l.add(m[i]);
		}
		return l;
	}

	public boolean isVisitMethod(Method m) {
		return m.getName().startsWith(methodPrefix())
				&& m.getParameterTypes().length >= 1;
	}

	public abstract String methodPrefix();

}
