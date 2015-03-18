package lala.core.reduction;

import lala.core.syntaxtree.Term;

/**
 * Applyable is an interface to designate terms that can accept other terms --
 * Abstractions. In a typed language, some terms such as "plus", etc. are such
 * terms that accept other terms as parameters.
 * 
 * @author Stefan Hanenberg
 *
 */
public interface Applyable {
	public Term apply(Term right);
}
