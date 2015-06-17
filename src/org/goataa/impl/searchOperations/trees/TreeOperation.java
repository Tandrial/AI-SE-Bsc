// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;

/**
 * A base class for tree-based search operations
 *
 * @author Thomas Weise
 */
public class TreeOperation extends OptimizationModule {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the maximum depth */
  public final int maxDepth;

  /**
   * Create a new tree operation
   *
   * @param md
   *          the maximum tree depth
   */
  protected TreeOperation(final int md) {
    super();
    this.maxDepth = Math.max(2, md);
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
    return (((longVersion) ? "maxDepth" : //$NON-NLS-1$
        "md") + this.maxDepth); //$NON-NLS-1$
  }

  /**
   * Create a sub-tree of the specified size maximum depth. This function
   * facilitates both, an implementation of the full method
   * Algorithm 31.1 and one of the grow method
   * Algorithm 31.2, which can be chosen by setting the
   * parameter 'full' apropriately. It can be used to create trees during
   * the random population initialization phase or during mutation steps.
   *
   * @param types
   *          the node types available for creating the tree
   * @param maxDepth
   *          the maximum depth of the tree
   * @param full
   *          Should we construct a sub-tree according to the full method?
   *          If full is false, grow is used.
   * @param r
   *          the random number generator
   * @return the new tree
   * @param <NT>
   *          the node type
   */
  @SuppressWarnings("unchecked")
  public static final <NT extends Node<NT>> NT createTree(
      final NodeTypeSet<NT> types, final int maxDepth, final boolean full,
      final Random r) {
    NodeType<NT, NT> t;
    NT[] x;
    int i;

    t = null;
    if (maxDepth <= 1) {
      t = types.randomTerminalType(r);
    } else {
      if (full) {
        t = types.randomNonTerminalType(r);
      }
      if (t == null) {
        t = types.randomType(r);
      }
    }
    if (t.isTerminal()) {
      return types.randomTerminalType(r).instantiate(null, r);
    }

    i = t.getChildCount();
    x = ((NT[]) (new Node[i]));
    for (; (--i) >= 0;) {
      x[i] = createTree(t.getChildTypes(i), maxDepth - 1, full, r);
    }
    return t.instantiate(x, r);
  }

}