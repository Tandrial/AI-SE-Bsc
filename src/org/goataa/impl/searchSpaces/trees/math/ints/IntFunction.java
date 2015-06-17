// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.math.Function;

/**
 * An integer-valued function
 *
 * @author Thomas Weise
 */
public class IntFunction extends Function<IntFunction> {
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
  public IntFunction(final Node<?>[] pchildren,
      final NodeType<? extends IntFunction, IntFunction> in,
      final boolean eff) {
    super(pchildren, in, eff);
  }

  /**
   * This is the core method of mathematics functions: it computes the
   * result of this function and can be invoked recursively downwards for
   * the other nodes.
   *
   * @param context
   *          the context
   * @return the result of the computation
   */
  public int compute(final Context context) {
    return 0;
  }

}