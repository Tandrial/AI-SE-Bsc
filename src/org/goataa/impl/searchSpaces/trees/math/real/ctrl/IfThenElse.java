// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.ctrl;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.utils.Utils;

/**
 * If-Then-Else
 *
 * @author Thomas Weise
 */
public class IfThenElse extends RealFunction {
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
  public IfThenElse(final Node<?>[] pchildren,
      final NodeType<IfThenElse, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * IfThenElse two values
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  @Override
  public double compute(final RealContext data) {
    if (data.step()) {
      if (this.get(0).compute(data) > 0d) {
        return this.get(1).compute(data);
      }
      return this.get(2).compute(data);
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
    sb.append("if "); //$NON-NLS-1$
    this.printSubExpression(0, sb, '\0');
    sb.append(">0 then ");//$NON-NLS-1$
    this.printSubExpression(1, sb, '\0');
    sb.append(" else ");//$NON-NLS-1$
    this.printSubExpression(2, sb, '\0');
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
    RealFunction oa, ob, oc, a, b, c, r;
    NodeType<IfThenElse, RealFunction> t;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);
    ob = this.get(1);
    oc = this.get(2);

    a = oa.reduce(t.getChildTypes(0));
    b = ob.reduce(t.getChildTypes(1));
    c = ob.reduce(t.getChildTypes(2));

    if (a.hasConstantValue() && (!(a.hasEffect()))) {
      if (a.getConstantValue() > 0d) {
        c = r = b;
      } else {
        b = r = c;
      }

      if ((allowed == null) || (allowed.containsType(r.getType()))) {
        return r;
      }
    }

    if ((!(a.hasEffect())) && (Utils.equals(b, c))) {
      r = b;
      if ((allowed == null) || (allowed.containsType(r.getType()))) {
        return r;
      }
    }

    if ((a != oa) || (b != ob) || (c != oc)) {
      return new IfThenElse(new Node[] { a, b, c }, t);
    }

    return this;// super.reduce(allowed);
  }
}