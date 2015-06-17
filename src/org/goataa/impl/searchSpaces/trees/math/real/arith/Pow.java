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
 * Raise one number to the power of another one
 *
 * @author Thomas Weise
 */
public class Pow extends RealFunction {
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
  public Pow(final Node<?>[] pchildren,
      final NodeType<Pow, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Compute
   *
   * @param a
   *          the first number
   * @param b
   *          the second number
   * @return the result
   */
  private static final double compute(final double a, final double b) {
    double d;

    d = Math.pow(a, b);

    if (Double.isNaN(d)) {
      d = Math.pow(Math.abs(a), Math.abs(b));
      if (Double.isNaN(d)) {
        d = Math.abs(a);
        if (Double.isNaN(d)) {
          return 1;
        }
      }
    }

    return d;
  }

  /**
   * Raise one number to the power of another one
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  @Override
  public double compute(final RealContext data) {
    if (data.step()) {
      return compute(this.get(0).compute(data), this.get(1).compute(data));
    }
    return 0d;
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
    RealFunction oa, ob, a, b;
    NodeType<Pow, RealFunction> t;
    ConstantType q;
    double z;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);
    ob = this.get(1);
    q = null;
    a = oa.reduce(t.getChildTypes(0));
    b = ob.reduce(t.getChildTypes(1));

    if ((a.hasConstantValue() && (!(a.hasEffect())))
        && (b.hasConstantValue() && (!(b.hasEffect())))) {
      q = ConstantType.getConstantType(a, b, allowed);
      if (q != null) {
        if ((allowed == null) || (allowed.containsType(q))) {
          return q.instantiate(//
              compute(a.getConstantValue(), b.getConstantValue()));
        }
      }
    }

    if (b.hasConstantValue() && (!(b.hasEffect()))) {
      z = b.getConstantValue();
      if (z == 1d) {
        if ((allowed == null) || (allowed.containsType(a.getType()))) {
          return a;
        }
      } else {
        if (z == 0d) {
          if (q == null) {
            q = ConstantType.getConstantType(a, b, allowed);
          }
          if (q != null) {
            if ((allowed == null) || (allowed.containsType(q))) {
              return q.instantiate(1d);
            }
          }
        }
      }
    }

    if ((a != oa) || (b != ob)) {
      return new Pow(new Node[] { a, b }, t);
    }

    return this;// super.reduce(allowed);
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    this.printSubExpression(0, sb);
    sb.append('^');
    this.printSubExpression(1, sb);
  }
}