// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints.arith;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.ints.Context;
import org.goataa.impl.searchSpaces.trees.math.ints.IntFunction;
import org.goataa.impl.searchSpaces.trees.math.ints.basic.ConstantType;

/**
 * Perform a modulo division on two numbers
 *
 * @author Thomas Weise
 */
public class Mod extends IntFunction {
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
  public Mod(final Node<?>[] pchildren, final NodeType<Mod, IntFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Divide two values
   *
   * @param context
   *          the context
   * @return the result of the computation
   */
  @Override
  public int compute(final Context context) {
    int b;
    if (context.step()) {
      b = this.get(1).compute(context);
      if (b == 0) {
        context.error();
        return 0;
      }
      return (this.get(0).compute(context) % b);
    }
    return 0;
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
    sb.append('%');
    this.printSubExpression(1, sb);
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
  public IntFunction reduce(final NodeTypeSet<IntFunction> allowed) {
    IntFunction oa, ob, a, b;
    NodeType<Mod, IntFunction> t;
    int v;
    ConstantType q;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);
    ob = this.get(1);

    a = oa.reduce(t.getChildTypes(0));
    b = ob.reduce(t.getChildTypes(1));

    if ((a.hasConstantValue() && (!(a.hasEffect())))
        && (b.hasConstantValue() && (!(b.hasEffect())))) {
      q = ConstantType.getConstantType(a, b, allowed);
      if (q != null) {
        if ((allowed == null) || (allowed.containsType(q))) {
          v = ConstantType.doubleToInt(b.getConstantValue());
          if (v != 0) {
            return q.instantiate(//
                a.getConstantValue() % v);
          }
          return q.instantiate(0);
        }
      }
    }

    if (b.hasConstantValue() && (!(b.hasEffect()))) {
      v = ConstantType.doubleToInt(b.getConstantValue());
      if ((v == 1) || (v == (-1))) {
        q = ConstantType.getConstantType(b, null, allowed);
        if (q != null) {
          if ((allowed == null) || (allowed.containsType(q))) {
            return q.instantiate(0);
          }
        }
      }
    }

    if ((a != oa) || (b != ob)) {
      return new Mod(new Node[] { a, b }, t);
    }

    return this; // super.reduce(allowed);
  }
}