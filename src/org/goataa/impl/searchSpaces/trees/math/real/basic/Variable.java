// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.basic;

import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A variable returns the value at a specific index of the data
 *
 * @author Thomas Weise
 */
public class Variable extends RealFunction {
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
   * @param data
   *          the context containing the data vector
   * @return the variable's value
   */
  @Override
  public double compute(final RealContext data) {
    if (data.step()) {
      return data.read(this.index);
    }
    return 0d;
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
}