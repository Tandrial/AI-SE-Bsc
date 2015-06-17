// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints.basic;

import org.goataa.impl.searchSpaces.trees.math.ints.Context;
import org.goataa.impl.searchSpaces.trees.math.ints.IntFunction;

/**
 * A variable returns the value at a specific index of the data
 *
 * @author Thomas Weise
 */
public class Variable extends IntFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the variable index */
  public final int index;

  /**
   * Create a variable
   *
   * @param in
   *          the node information record
   * @param idx
   *          the index
   */
  public Variable(final VariableType in, final int idx) {
    super(null, in, false);
    this.index = idx;
  }

  /**
   * Returns the variable's value
   *
   * @param context
   *          the context
   * @return the result of the computation
   */
  @Override
  public int compute(final Context context) {
    if (context.step()) {
      return context.read(this.index);
    }
    return 0;
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    sb.append('x');
    sb.append('[');
    sb.append(this.index);
    sb.append(']');
  }

  /**
   * Compare with another object
   *
   * @param o
   *          the other object
   * @return true if the objects are equal
   */
  @Override
  public boolean equals(final Object o) {
    return ((o == this) || ((o instanceof Variable) && //
    (((Variable) o).index == this.index)));
  }
}