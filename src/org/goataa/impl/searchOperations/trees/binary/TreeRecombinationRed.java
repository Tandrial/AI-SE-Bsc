// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.binary;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.ReducibleNode;

/**
 * A tree recombination operator which reduces the nodes after
 * recombination
 *
 * @author Thomas Weise
 */
public class TreeRecombinationRed extends TreeRecombination {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the allowed node types */
  private final NodeTypeSet<?> allowed;

  /**
   * Create a new tree mutationoperation
   *
   * @param md
   *          the maximum tree depth
   * @param al
   *          the allowed node types
   */
  public TreeRecombinationRed(final NodeTypeSet<?> al, final int md) {
    super(md);
    this.allowed = al;
  }

  /**
   * Perform a reducing binary recombination
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  @SuppressWarnings("unchecked")
  public Node<?> recombine(final Node<?> p1, final Node<?> p2,
      final Random r) {
    Node<?> res;

    res = super.recombine(p1, p2, r);
    if (res instanceof ReducibleNode) {
      return ((ReducibleNode) res).reduce(this.allowed);
    }
    return res;
  }
}