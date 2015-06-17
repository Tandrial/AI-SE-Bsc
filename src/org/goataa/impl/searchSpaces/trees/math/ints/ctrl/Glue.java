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
 * Glue some instructions together
 *
 * @author Thomas Weise
 */
public class Glue extends IntFunction {
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
  public Glue(final Node<?>[] pchildren,
      final NodeType<Glue, IntFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Glue some instructions together
   *
   * @param context
   *          the context
   * @return always RETURN_VAL
   */
  @Override
  public int compute(final Context context) {
    int i, s;

    if (context.step()) {
      s = this.size();
      if (s > 0) {
        context.beginBlock();
        for (i = 0; i < s; i++) {
          this.get(i).compute(context);
        }
        context.endBlock();
      }
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

    sb.append('{');
    s = this.size();
    for (i = 0; i < s; i++) {
      if (i > 0) {
        sb.append(',');
      }
      this.printSubExpression(i, sb, '\0');
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
    Node[] ch;
    IntFunction o, n;
    int s, i;
    boolean neff, eff;
    NodeType nt;
    NodeTypeSet x;
    ConstantType c;

    ch = null;
    nt = this.getType();
    c = null;
    eff = false;
    s = this.size();
    for (i = s; (--i) >= 0;) {
      o = this.get(i);

      x = nt.getChildTypes(i);
      n = o.reduce(x);
      neff = n.hasEffect();
      if (!neff) {
        if (c == null) {
          c = ConstantType.getConstantType(n, o,
              (allowed != null) ? allowed : x);
        }
        if (c != null) {
          n = c.instantiate(RETURN_VAL);
        }
      }

      if (o != n) {
        if (ch == null) {
          ch = new Node[s];
          for (; (--s) > i;) {
            ch[s] = this.get(s);
          }
        }
        ch[i] = n;
      } else {
        if (ch != null) {
          ch[i] = n;
        }
      }

      eff |= neff;
    }

    if (!eff) {
      if (c == null) {
        c = ConstantType.getConstantType(null, null, allowed);
      }

      if ((c != null) && ((allowed == null) || (allowed.containsType(c)))) {
        return c.instantiate(RETURN_VAL);
      }
    }

    if (ch != null) {
      return new Glue(ch, nt);
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