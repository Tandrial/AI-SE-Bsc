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
 * Ceil a given number
 *
 * @author Thomas Weise
 */
public class Ceil extends RealFunction {
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
  public Ceil(final Node<?>[] pchildren,
      final NodeType<Ceil, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Compute the ceil value of a given number
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  @Override
  public double compute(final RealContext data) {
    if (data.step()) {
      return Math.ceil(this.get(0).compute(data));
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
    sb.append("ceil"); //$NON-NLS-1$
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
    NodeType<Ceil, RealFunction> t;
    ConstantType q;
    double d;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);

    a = oa.reduce(t.getChildTypes(0));
    if ((a.hasConstantValue()) && (!(a.hasEffect()))) {
      q = ConstantType.getConstantType(a, null, allowed);
      if (q != null) {
        if ((allowed == null) || (allowed.containsType(q))) {
          d = Math.ceil(a.getConstantValue());
          if (Double.isNaN(d)) {
            d = 0d;
          }
          return q.instantiate(d);
        }
      }
    }

    if (a != oa) {
      return new Ceil(new Node[] { a }, t);
    }

    return this; // super.reduce(allowed);
  }
}