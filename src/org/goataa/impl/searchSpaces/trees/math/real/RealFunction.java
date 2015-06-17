// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.math.Function;

/**
 * A mathematics function which works one a vector of parameters and
 * returns a single value, as used, for instance, in Symbolic Regression
 * Section 49.1.
 *
 * @author Thomas Weise
 */
public class RealFunction extends Function<RealFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   * @param eff
   *          has this function a state changing effect?
   */
  public RealFunction(final Node<?>[] pchildren,
      final NodeType<? extends RealFunction, RealFunction> in,
      final boolean eff) {
    super(pchildren, in, eff);
  }

  /**
   * This is the core method of mathematics functions: it computes the
   * result of this function and can be invoked recursively downwards for
   * the other nodes.
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  public double compute(final RealContext data) {
    return 0d;
  }
}