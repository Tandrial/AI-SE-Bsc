// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.basic;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A memory write operation
 *
 * @author Thomas Weise
 */
public class Write extends RealFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the variable index */
  public final int index;

  /**
   * Create a write operation
   *
   * @param pchildren
   *          the child nodes *
   * @param in
   *          the node information record
   * @param idx
   *          the index
   */
  public Write(final Node<?>[] pchildren, final WriteType in, final int idx) {
    super(pchildren, in, true);
    this.index = idx;
  }

  /**
   * Set the variable's value and return the new value
   *
   * @param context
   *          the context
   * @return the result of the computation
   */
  @Override
  public double compute(final RealContext context) {
    double i;

    if (context.step()) {
      i = this.get(0).compute(context);
      context.write(this.index, i);
      return i;
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
    sb.append('x');
    sb.append('[');
    sb.append(this.index);
    sb.append(']');
    sb.append('=');
    this.printSubExpression(0, sb, '\0');
  }

  /**
   * Compare with another object
   *
   * @param o
   *          the other object
   * @return true if the objects are equal
   */
  @Override
  public boolean equals(final Object o) {
    Write w;

    if (o == this) {
      return true;
    }
    if (!(o instanceof Write)) {
      return false;
    }

    w = ((Write) o);

    return ((w.index == this.index) && (w.get(0).equals(this.get(0))));
  }

  /**
   * Try to reduce this node by adhering the given allowed types
   *
   * @param allowed
   *          the allowd types
   * @return the reduced node, or this node if no reduction is possible
   */
  @Override
  public RealFunction reduce(final NodeTypeSet<RealFunction> allowed) {
    RealFunction oa, a;
    WriteType t;

    t = ((WriteType) (this.getType()));
    oa = this.get(0);

    a = oa.reduce(t.getChildTypes(0));

    if ((a instanceof Variable) && ((((Variable) a).index) == this.index)) {
      if ((allowed == null) || (allowed.containsType(a.getType()))) {
        return a;
      }
    }

    if (a != oa) {
      return new Write(new Node[] { a }, t, this.index);
    }

    return this; // super.reduce(allowed);
  }
}