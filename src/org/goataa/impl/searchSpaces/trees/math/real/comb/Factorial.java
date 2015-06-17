// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.comb;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.searchSpaces.trees.math.real.basic.ConstantType;

/**
 * Compute the factorial value of a given number
 *
 * @author Thomas Weise
 */
public class Factorial extends RealFunction {
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
  public Factorial(final Node<?>[] pchildren,
      final NodeType<Factorial, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Compute the factorial value of a given number
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  @Override
  public double compute(final RealContext data) {

    if (data.step()) {
      return factorial(this.get(0).compute(data));
    }
    return 0d;
  }

  /**
   * Compute the factorial of a number
   *
   * @param v
   *          the input
   * @return the output
   */
  private static final double factorial(final double v) {
    double d;
    int i;

    if ((v < 1d) || Double.isNaN(v)) {
      return 0d;
    }
    if (v > 100d) {
      return Double.POSITIVE_INFINITY;
    }

    i = (int) (0.5d + Math.floor(v));
    d = 1d;
    for (; i > 1; i--) {
      d *= i;
    }

    return d;
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
    sb.append('!');
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
    NodeType<Factorial, RealFunction> t;
    ConstantType q;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);

    a = oa.reduce(t.getChildTypes(0));
    if ((a.hasConstantValue()) && (!(a.hasEffect()))) {
      q = ConstantType.getConstantType(a, null, allowed);
      if (q != null) {
        if ((allowed == null) || (allowed.containsType(q))) {
          return q.instantiate(factorial(a.getConstantValue()));
        }
      }
    }

    if (a != oa) {
      return new Factorial(new Node[] { a }, t);
    }

    return this; // super.reduce(allowed);
  }
}