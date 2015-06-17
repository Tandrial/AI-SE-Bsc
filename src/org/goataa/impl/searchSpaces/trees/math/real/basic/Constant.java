// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.basic;

import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A constant always returns a specific value
 *
 * @author Thomas Weise
 */
public class Constant extends RealFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the constant value */
  public final double value;

  /**
   * Create a constant
   *
   * @param in
   *          the constant type
   * @param val
   *          the value
   */
  public Constant(final ConstantType in, final double val) {
    super(null, in, false);
    this.value = val;
  }

  /**
   * Returns the constant value
   *
   * @param data
   *          the context containing the data vector
   * @return the constant value
   */
  @Override
  public double compute(final RealContext data) {
    if (data.step()) {
      return this.value;
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
    sb.append(this.value);
  }
}