// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.trig;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.searchSpaces.trees.math.real.basic.ConstantType;

/**
 * Compute the arcus tangent of a number
 *
 * @author Thomas Weise
 */
public class ATan extends RealFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   */
  public ATan(final Node<?>[] pchildren,
      final NodeType<ATan, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Compute
   *
   * @param a
   *          the value
   * @return the result
   */
  private static final double compute(final double a) {
    double d;
    d = Math.atan(a);
    if (Double.isNaN(d)) {
      return 1d;
    }
    return d;
  }

  /**
   * Compute the tangent
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  @Override
  public double compute(final RealContext data) {
    if (data.step()) {
      return compute(this.get(0).compute(data));
    }
    return 0d;
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    sb.append("atan"); //$NON-NLS-1$
    this.printSubExpression(0, sb, ' ');
  }

  /**
   * Try to reduce this node by adhering the given allowed types
   *
   * @param allowed
   *          the allowd types
   * @return the reduced node, or this node if no reduction is possible
   */
  @Override
  @SuppressWarnings("unchecked")
  public RealFunction reduce(final NodeTypeSet<RealFunction> allowed) {
    RealFunction oa, a;
    NodeType<ATan, RealFunction> t;
    ConstantType q;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);

    a = oa.reduce(t.getChildTypes(0));
    if ((a.hasConstantValue()) && (!(a.hasEffect()))) {
      q = ConstantType.getConstantType(a, null, allowed);
      if (q != null) {
        if ((allowed == null) || (allowed.containsType(q))) {
          return q.instantiate(compute(a.getConstantValue()));
        }
      }
    }

    if (a != oa) {
      return new ATan(new Node[] { a }, t);
    }

    return this; // super.reduce(allowed);
  }
}