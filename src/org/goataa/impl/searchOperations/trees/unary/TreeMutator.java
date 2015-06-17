// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.unary;

import java.util.Random;

import org.goataa.impl.searchOperations.trees.TreeOperation;
import org.goataa.impl.searchOperations.trees.TreePath;
import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A simple mutation operation for a given tree which implants a randomly
 * created subtree into parent, thereby replacing a randomly picked node in
 * the parent. This operation basically proceeds according to the ideas
 * discussed in Section 31.3.4.1 with the extension
 * that it also respects the type system of the strongly-typed GP system.
 *
 * @author Thomas Weise
 */
public class TreeMutator extends TreeOperation implements
    IUnarySearchOperation<Node<?>> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internal path */
  private final TreePath<?> path;

  /**
   * Create a new tree mutationoperation
   *
   * @param md
   *          the maximum tree depth
   */
  @SuppressWarnings("unchecked")
  public TreeMutator(final int md) {
    super(md);
    this.path = new TreePath();
  }

  /**
   * This is the unary search operation. It takes one existing genotype g
   * (see Definition D4.2) from the genome and produces one new
   * element in the search space. This new element is usually a copy of te
   * existing element g which is slightly modified in a random manner. For
   * this purpose, we pass a random number generator in as parameter so we
   * can use the same random number generator in all parts of an
   * optimization algorithm.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @SuppressWarnings("unchecked")
  public Node<?> mutate(final Node<?> g, final Random r) {
    int trials, len, idx;
    Node sel, nu, par;
    TreePath p;

    p = this.path;
    for (trials = 100; trials > 0; trials--) {

      p.randomPath(g, r);
      len = p.size() - 1;

      sel = p.get(len);
      if (sel.isTerminal()) {
        nu = sel.getType().mutate(sel, r);
      } else {
        nu = sel;
      }
      if ((nu == sel) && (len > 0)) {
        len--;
        idx = p.getChildIndex(len);
        par = p.get(len);
        nu = TreeOperation.createTree(par.getType().getChildTypes(idx),
            this.maxDepth - len + 1, false, r);
        if (nu == null) {
          nu = sel;
        }
      }

      if (nu != sel) {
        return p.replaceEnd(nu);
      }
    }

    return g;
  }
}