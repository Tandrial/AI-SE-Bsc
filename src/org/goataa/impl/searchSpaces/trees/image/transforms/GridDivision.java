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
import org.goataa.impl.searchSpaces.trees.image.InvertibleWindow;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.searchSpaces.trees.math.real.basic.ConstantType;

/**
 * Divide an area into a grid and fill all of its elements accordingly.
 * This node has two children: The first one provides the fraction of width
 * and height that each grid cell gets assigned. The second child performs
 * the filling.
 *
 * @author Thomas Weise
 */
public class GridDivision extends GraphicsNode {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internal pixel window */
  private static final InvertibleWindow W = new InvertibleWindow();

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   */
  public GridDivision(final Node<?>[] pchildren,
      final NodeType<GridDivision, RealFunction> in) {
    super(pchildren, in, false);
  }

  /**
   * Compute
   *
   * @param d
   *          the value
   * @return the result
   */
  private static final double compute(final double d) {

    if (Double.isNaN(d) || Double.isInfinite(d)) {
      return 0d;
    }

    return Math.abs(d);
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
    final RealFunction fillChild;
    final double oldX, oldY, minX, minY, maxX, maxY;
    int steps;
    int x, y;
    double div, xpos, nxpos, nypos, ypos;
    final AffineTransform at;

    W.init(gc);
    x = W.getWidth();
    if (x <= 0) {
      return 0d;
    }
    y = W.getHeight();
    if (y <= 0) {
      return 0d;
    }

    steps = Math.max(x, y);

    div = compute(this.get(0).compute(gc));
    if (Double.isNaN(div)) {
      return 0d;
    }

    if ((div != 0d) && (div != 1d) && (!(Double.isInfinite(div)))) {
      if (div < 1d) {
        div = (1d / div);
      }
      steps = Math.min((int) (0.5d + div), steps);
    }

    if (steps <= 0) {
      return 0d;
    }

    div = (1d / steps);

    fillChild = this.get(1);

    oldX = gc.read(ImageUtils.ADDRESS_X);
    oldY = gc.read(ImageUtils.ADDRESS_Y);

    g = gc.getGraphics();
    at = g.getTransform();

    minX = W.origMinX();
    maxX = W.origMaxX();
    minY = W.origMinY();
    maxY = W.origMaxY();

    inner: {
      xpos = 1d;
      for (x = steps; (--x) >= 0;) {
        nxpos = xpos;
        xpos = (x * div);

        if ((nxpos >= minX) && (xpos <= maxX)) {
          ypos = 1d;

          for (y = steps; (--y) >= 0;) {
            nypos = ypos;
            ypos = (y * div);

            if ((nypos >= minY) && (ypos <= maxY)) {

              if (!(gc.step())) {
                break inner;
              }

              g.setTransform(at);
              g.translate(xpos, ypos);
              g.scale(div, div);
              gc.write(ImageUtils.ADDRESS_X, xpos);
              gc.write(ImageUtils.ADDRESS_Y, ypos);

              fillChild.compute(gc);
            }
          }
        }
      }
    }

    gc.write(ImageUtils.ADDRESS_Y, oldY);
    gc.write(ImageUtils.ADDRESS_X, oldX);
    g.setTransform(at);

    return div;
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
    RealFunction oa, a, b, ob;
    NodeType<GridDivision, RealFunction> t;
    ConstantType q;

    t = ((NodeType) (this.getType()));
    oa = this.get(0);
    a = oa.reduce(t.getChildTypes(0));

    ob = this.get(1);
    b = ob.reduce(t.getChildTypes(1));

    if (!(b.hasEffect())) {
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

    if ((a != oa) || (b != ob)) {
      return new GridDivision(new Node[] { a, b }, t);
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
    sb.append("grid(fract="); //$NON-NLS-1$
    this.get(0).fillInText(sb);
    sb.append(",fill="); //$NON-NLS-1$
    this.get(1).fillInText(sb);
    sb.append(')');
  }
}