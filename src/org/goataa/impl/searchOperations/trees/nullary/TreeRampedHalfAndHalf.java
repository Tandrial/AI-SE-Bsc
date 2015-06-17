// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.nullary;

import java.util.Random;

import org.goataa.impl.searchOperations.trees.TreeOperation;
import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.spec.INullarySearchOperation;

/**
 * A tree creator using the ramped-half-and-half method as described in
 * Section 31.3.2.3.
 *
 * @param <NT>
 *          the node type
 * @author Thomas Weise
 */
public class TreeRampedHalfAndHalf<NT extends Node<NT>> extends TreeOperation
    implements INullarySearchOperation<NT> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the types to choose from */
  public final NodeTypeSet<NT> types;

  /**
   * Create a new ramped-half-and-half
   *
   * @param md
   *          the maximum tree depth
   * @param ptypes
   *          the types
   */
  public TreeRampedHalfAndHalf(final NodeTypeSet<NT> ptypes, final int md) {
    super(md);
    this.types = ptypes;
  }

  /**
   * Create a new tree according to the ramped-half-and-half method given
   * in Algorithm 31.3.
   *
   * @param r
   *          the random number generator
   * @return a new genotype (see Definition D4.2)
   */
  public NT create(final Random r) {

    return TreeOperation.createTree(this.types,//
        (2 + r.nextInt(this.maxDepth)), r.nextBoolean(), r);
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  @Override
  public String getConfiguration(final boolean longVersion) {
    return super.getConfiguration(longVersion) + ','
        + this.types.toString(longVersion);
  }
}