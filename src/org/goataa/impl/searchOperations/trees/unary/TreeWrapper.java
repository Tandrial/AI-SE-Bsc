// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.unary;

import java.util.Random;

import org.goataa.impl.searchOperations.trees.TreeOperation;
import org.goataa.impl.searchOperations.trees.TreePath;
import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.spec.IUnarySearchOperation;

/**
 * An operator which wraps a tree node
 *
 * @author Thomas Weise
 */
public class TreeWrapper extends TreeOperation implements
    IUnarySearchOperation<Node<?>> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internal path */
  private final TreePath<?> path;

  /**
   * Create a new tree mutation operation
   *
   * @param md
   *          the maximum tree depth
   */
  @SuppressWarnings("unchecked")
  public TreeWrapper(final int md) {
    super(md);
    this.path = new TreePath();
  }

  /**
   * This is an unary operation which tries to wrap a tree node
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
    int trials, len, idx, i;
    Node sel, nu, par;
    TreePath p;
    NodeType x, y;

    p = this.path;
    for (trials = 100; trials > 0; trials--) {

      p.randomPath(g, r);
      len = p.size() - 1;

      nu = sel = p.get(len);

      if (len > 0) {
        len--;
        idx = p.getChildIndex(len);
        par = p.get(len);
        nu = TreeOperation.createTree(par.getType().getChildTypes(idx),
            this.maxDepth - len + 1, false, r);
        if (nu == null) {
          nu = sel;
        }
        if (nu != sel) {

          len = nu.size();
          if (len > 0) {
            idx = r.nextInt(len);
            x = nu.getType();
            y = sel.getType();
            for (i = len; (--i) >= 0;) {
              if (x.getChildTypes(idx).containsType(y)) {
                nu = nu.setChild(sel, idx);
                return p.replaceEnd(nu);
              }
              idx = ((idx + 1) % len);
            }
          }
        }
      }

    }

    return g;
  }
}