package networkGenerator.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class DeterministicHashSet<T> extends LinkedHashSet<T> {

	private static final long serialVersionUID = 1L;

	public DeterministicHashSet() {
		super();
	}

	public DeterministicHashSet(Collection<? extends T> c) {
		super(c);
	}

	public DeterministicHashSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public DeterministicHashSet(int initialCapacity) {
		super(initialCapacity);
	}

	// JDK Bug? LinkedHashSet.contains(o) returns always false
	@Override
	public boolean contains(Object o) {
		for (T e : this) {
			if (e.equals(o))
				return true;
		}
		return false;
	}

	// JDK Bug? LinkedHashSet.remove(o) does not remove, returns always false
	@Override
	public boolean remove(Object o) {
		List<T> l = new LinkedList<T>();
		boolean modified = false;
		for (T e : this) {
			if (!e.equals(o))
				l.add(e);
			else
				modified = true;
		}
		this.clear();
		this.addAll(l);
		return modified;
	}
}