// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.unary;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.ReducibleNode;

/**
 * A mutation operation that first performs a common mutation and then
 * reduces the tree node.
 *
 * @author Thomas Weise
 */
public class TreeMutatorRed extends TreeMutator {
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
  public TreeMutatorRed(final NodeTypeSet<?> al, final int md) {
    super(md);
    this.allowed = al;
  }

  /**
   * Perform a reducing mutation
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @SuppressWarnings("unchecked")
  @Override
  public Node<?> mutate(final Node<?> g, final Random r) {
    Node<?> res;

    res = super.mutate(g, r);
    if (res instanceof ReducibleNode) {
      return ((ReducibleNode) res).reduce(this.allowed);
    }
    return res;

  }
}