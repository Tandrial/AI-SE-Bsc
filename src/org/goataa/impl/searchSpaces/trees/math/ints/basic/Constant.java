// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints.basic;

import org.goataa.impl.searchSpaces.trees.math.ints.Context;
import org.goataa.impl.searchSpaces.trees.math.ints.IntFunction;

/**
 * A constant always returns a specific value
 *
 * @author Thomas Weise
 */
public class Constant extends IntFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the constant value */
  public final int value;

  /**
   * Create a constant
   *
   * @param in
   *          the constant type
   * @param val
   *          the value
   */
  public Constant(final ConstantType in, final int val) {
    super(null, in, false);
    this.value = val;
  }

  /**
   * Returns the constant value
   *
   * @param context
   *          the context
   * @return the result of the computation
   */
  @Override
  public int compute(final Context context) {
    if (context.step()) {
      return this.value;
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
    sb.append(this.value);
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
    return ((o == this) || ((o instanceof Constant) && //
    (((Constant) o).value == this.value)));
  }

  /**
   * Return whether this element has a constant value or not
   *
   * @return true if and only if this element has a constant value
   */
  @Override
  public boolean hasConstantValue() {
    return true;
  }

  /**
   * Get the constant value of this element
   *
   * @return the constant value of this element
   */
  @Override
  public double getConstantValue() {
    return this.value;
  }
}