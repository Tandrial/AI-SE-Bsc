/*
 * Copyright (c) 2010 Thomas Weise
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package org.goataa.impl.searchSpaces.trees;

/**
 * A node which can possibly be reduced, i.e., simplified. Such a node can
 * first check whether any of its children can be simplified and then may
 * simplified itself.
 *
 * @param <CT>
 *          the child node type
 * @author Thomas Weise
 */
public class ReducibleNode<CT extends ReducibleNode<CT>> extends Node<CT> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node type record
   */
  public ReducibleNode(final Node<?>[] pchildren,
      final NodeType<? extends CT, CT> in) {
    super(pchildren, in);
  }

  /**
   * Try to reduce this node by adhering the given allowed types
   *
   * @param allowed
   *          the allowd types
   * @return the reduced node, or this node if no reduction is possible
   */
  @SuppressWarnings("unchecked")
  public CT reduce(final NodeTypeSet<CT> allowed) {
    Node<?>[] ch;
    boolean set;
    int i;
    Node k, nk;
    NodeType<?, ?> t;

    ch = this.children;

    i = ch.length;
    if (i > 0) {
      set = true;
      t = this.getType();
      for (i = ch.length; (--i) >= 0;) {
        k = ch[i];
        if (k instanceof ReducibleNode) {
          nk = ((ReducibleNode) k).reduce(t.getChildTypes(i));
          if (nk != k) {
            if (set) {
              ch = ch.clone();
              set = false;
            }
            ch[i] = nk;
          }
        }
      }
      if (!(set)) {
        k = this.clone();
        k.children = ch;
        this.computeParams();
        return ((CT) k);
      }
    }
    return ((CT) this);
  }

}