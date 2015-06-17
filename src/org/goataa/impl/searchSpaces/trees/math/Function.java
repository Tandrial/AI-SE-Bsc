// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.ReducibleNode;

/**
 * An base class for mathematical functions
 *
 * @param <FT>
 *          the function type
 * @author Thomas Weise
 */
public class Function<FT extends Function<FT>> extends ReducibleNode<FT> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** does this function has a state-changing effect? */
  int effect;

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
  public Function(final Node<?>[] pchildren,
      final NodeType<? extends FT, FT> in, final boolean eff) {
    super(pchildren, in);
    if (eff && (this.effect <= 1)) {
      this.effect = 2;
    }
  }

  /**
   * Print a sub-expression to the given string builder
   *
   * @param idx
   *          the sub-expression index
   * @param sb
   *          the string builder
   * @param prefix
   *          the alternative prefix
   */
  protected final void printSubExpression(final int idx,
      final StringBuilder sb, final char prefix) {
    Function<?> a;
    boolean b;

    a = this.get(idx);
    b = ((a.size() > 0) || a.hasEffect() || //
    (a.hasConstantValue() && (a.getConstantValue() < 0d)));
    if (b) {
      sb.append('(');
    } else {
      if (prefix != '\0') {
        sb.append(prefix);
      }
    }
    a.fillInText(sb);
    if (b) {
      sb.append(')');
    }
  }

  /**
   * Print a sub-expression to the given string builder
   *
   * @param idx
   *          the sub-expression index
   * @param sb
   *          the string builder
   */
  protected final void printSubExpression(final int idx,
      final StringBuilder sb) {
    this.printSubExpression(idx, sb, '\0');
  }

  /**
   * Compute the fixed parameters of this node. This method is called
   * whenever a reproduced copy of the node is created or when a new node
   * is created with the constructor.
   */
  @Override
  protected void computeParams() {
    int i;

    super.computeParams();

    if (this.effect < 2) {
      for (i = this.size(); (--i) >= 0;) {
        if (this.get(i).effect > 0) {
          this.effect = 1;
          return;
        }
      }
    }
  }

  /**
   * Does this function has any state-changing effect?
   *
   * @return true if this function may change the global state, false
   *         otherwise
   */
  public final boolean hasEffect() {
    return (this.effect > 0);
  }

  /**
   * Return whether this element has a constant value or not
   *
   * @return true if and only if this element has a constant value
   */
  public boolean hasConstantValue() {
    return false;
  }

  /**
   * Get the constant value of this function
   *
   * @return the constant value of this function
   */
  public double getConstantValue() {
    return 0d;
  }
}