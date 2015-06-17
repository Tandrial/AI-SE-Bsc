// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints.ctrl;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.ints.Context;
import org.goataa.impl.searchSpaces.trees.math.ints.IntFunction;
import org.goataa.impl.searchSpaces.trees.math.ints.basic.ConstantType;

/**
 * A simple while loop
 *
 * @author Thomas Weise
 */
public class WhileLoop extends IntFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the default return value */
  private static final int RETURN_VAL = 0;

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   */
  public WhileLoop(final Node<?>[] pchildren,
      final NodeType<WhileLoop, IntFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Check a condition
   *
   * @param i
   *          the condition value
   * @return true if the loop should continue
   */
  private static final boolean check(final int i) {
    return (i > 0);
  }

  /**
   * Subtract two values
   *
   * @param context
   *          the context
   * @return the result of the computation
   */
  @Override
  public int compute(final Context context) {
    int i, s;

    s = this.size();
    if (s > 0) {
      do {
        if (!(check(this.get(0).compute(context)))) {
          break;
        }
        context.beginBlock();
        for (i = 0; i < s; i++) {
          this.get(i).compute(context);
        }
        context.endBlock();
      } while (context.step());
    }

    return RETURN_VAL;
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    int i, s;

    s = this.size();
    sb.append("while("); //$NON-NLS-1$
    this.printSubExpression(0, sb, '\0');
    sb.append(">0) {"); //$NON-NLS-1$
    for (i = 1; i < s; i++) {
      if (i > 1) {
        sb.append(',');
      }
      this.printSubExpression(1, sb, '\0');
    }
    sb.append('}');
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
    NodeType t;
    ConstantType q;

    t = this.getType();
    oa = this.get(0);
    ob = this.get(1);

    a = oa.reduce(t.getChildTypes(0));
    b = ob.reduce(t.getChildTypes(1));

    if ((!(a.hasEffect()))
        && (((a.hasConstantValue() && (!(check(ConstantType.doubleToInt(a
            .getConstantValue()))))) || ((!(b.hasEffect())))))) {
      q = ConstantType.getConstantType(a, b, allowed);

      if ((q != null) && ((allowed == null) || (allowed.containsType(q)))) {
        return q.instantiate(RETURN_VAL);
      }
    }

    if ((a != oa) || (b != ob)) {
      return new WhileLoop(new Node[] { a, b }, t);
    }

    return this;// super.reduce(allowed);
  }

  /**
   * Return whether this element has a constant value or not
   *
   * @return true if and only if this element has a constant value
   */
  @Override
  public boolean hasConstantValue() {
    return true;
  }

  /**
   * Get the constant value of this element
   *
   * @return the constant value of this element
   */
  @Override
  public double getConstantValue() {
    return 0d;
  }
}