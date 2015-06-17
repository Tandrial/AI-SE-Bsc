// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.transforms;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.image.ImageUtils;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.searchSpaces.trees.math.real.basic.ConstantType;

/**
 * Divide an area into two pieces. This node needs three children: The
 * first one is a fraction, the second one is painted into the upper part
 * and the third one is painted into the lower part.
 *
 * @author Thomas Weise
 */
public class FractionDiv extends GraphicsNode {
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
  public FractionDiv(final Node<?>[] pchildren,
      final NodeType<FractionDiv, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Compute the function value from a constant
   *
   * @param c
   *          the constant
   * @return the function value
   */
  private static final double compute(final double c) {
    if (Double.isNaN(c)) {
      return 0.5d;
    }

    return Math.max(-1d, Math.min(1d, c));
  }

  /**
   * The compute functon of graphics contexts
   *
   * @param gc
   *          the graphics context
   * @return the result
   */
  @Override
  public double compute(final GraphicsContext gc) {
    final Graphics2D g;
    final double oldY;
    double div, res;
    final AffineTransform at;
    RealFunction c1, c2, c3;

    if (!(gc.step())) {
      return 0d;
    }

    res = div = compute(this.get(0).compute(gc));

    c1 = this.get(1);
    c2 = this.get(2);
    if (div < 0d) {
      c3 = c1;
      c1 = c2;
      c2 = c3;
      div = (-div);
    }

    g = gc.getGraphics();
    at = g.getTransform();

    if (div > 0d) {
      g.scale(1d, div);
      c1.compute(gc);
    }

    if (div < 1d) {
      g.setTransform(at);
      g.translate(0d, div);
      g.scale(1d, 1d - div);
      oldY = gc.read(ImageUtils.ADDRESS_Y);
      gc.write(ImageUtils.ADDRESS_Y, div);
      res = c2.compute(gc);
      gc.write(ImageUtils.ADDRESS_Y, oldY);
    }

    g.setTransform(at);

    return res;
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
    RealFunction oa, a, b, ob, c, oc;
    NodeType<FractionDiv, RealFunction> t;
    ConstantType q;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);
    a = oa.reduce(t.getChildTypes(0));

    ob = this.get(1);
    b = ob.reduce(t.getChildTypes(1));

    oc = this.get(2);
    c = oc.reduce(t.getChildTypes(2));

    if (!(b.hasEffect() || c.hasEffect())) {
      if (a.hasConstantValue() && (!(a.hasEffect()))) {
        q = ConstantType.getConstantType(a, b, allowed);
        if ((allowed == null) || (allowed.containsType(q))) {
          return q.instantiate(compute(a.getConstantValue()));
        }
      }

      if ((allowed == null) || (allowed.containsType(a.getType()))) {
        return a;
      }
    }

    if ((a != oa) || (b != ob) || (c != oc)) {
      return new FractionDiv(new Node[] { a, b, c }, t);
    }

    return this; // super.reduce(allowed);
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    sb.append("divideV("); //$NON-NLS-1$
    this.get(0).fillInText(sb);
    sb.append(':');
    this.get(1).fillInText(sb);
    sb.append('|');
    this.get(2).fillInText(sb);
    sb.append(')');
  }
}