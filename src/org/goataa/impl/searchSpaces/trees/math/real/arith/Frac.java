// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.arith;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.searchSpaces.trees.math.real.basic.ConstantType;

/**
 * Frac a given number
 *
 * @author Thomas Weise
 */
public class Frac extends RealFunction {
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
  public Frac(final Node<?>[] pchildren,
      final NodeType<Frac, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Floor a number
   *
   * @param d
   *          the number
   * @return the result
   */
  private static final double compute(final double d) {
    double a;

    a = (d - Math.floor(d));
    if (Double.isNaN(a)) {
      return 0d;
    }
    return a;
  }

  /**
   * Compute the floor value of a given number
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
    return 0.5d;
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    sb.append("frac"); //$NON-NLS-1$
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
    NodeType<Frac, RealFunction> t;
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
      return new Frac(new Node[] { a }, t);
    }

    return this; // super.reduce(allowed);
  }
}