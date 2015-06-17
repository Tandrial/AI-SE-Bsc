// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.nullary;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.ReducibleNode;

/**
 * A ramped-half-and-half operator which reduces the node after creation
 *
 * @param <NT>
 *          the node type
 * @author Thomas Weise
 */
public class TreeRampedHalfAndHalfRed<NT extends Node<NT>> extends
    TreeRampedHalfAndHalf<NT> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a new ramped-half-and-half
   *
   * @param md
   *          the maximum tree depth
   * @param ptypes
   *          the types
   */
  public TreeRampedHalfAndHalfRed(final NodeTypeSet<NT> ptypes, final int md) {
    super(ptypes, md);
  }

  /**
   * Create a new tree according to the ramped-half-and-half method
   *
   * @param r
   *          the random number generator
   * @return a new genotype (see null)
   */
  @Override
  @SuppressWarnings("unchecked")
  public NT create(final Random r) {
    NT x;

    x = super.create(r);
    if (x instanceof ReducibleNode) {
      return ((NT) (((ReducibleNode) x).reduce(this.types)));
    }

    return x;
  }
}