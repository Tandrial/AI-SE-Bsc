// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints.basic;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.math.ints.IntFunction;

/**
 * A variable type
 *
 * @author Thomas Weise
 */
public class VariableType extends NodeType<Variable, IntFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the variables */
  public final Variable[] vars;

  /**
   * Create a new variable type record
   *
   * @param varc
   *          the variable count
   */
  public VariableType(final int varc) {
    super(null);

    int i;
    Variable[] v;

    this.vars = v = new Variable[varc];
    for (i = varc; (--i) >= 0;) {
      v[i] = new Variable(this, i);
    }
  }

  /**
   * Instantiate a node
   *
   * @param children
   *          a given set of children
   * @param r
   *          the randomizer
   * @return the new node
   */
  @Override
  public Variable instantiate(final Node<?>[] children, final Random r) {
    return this.vars[r.nextInt(this.vars.length)];
  }
}