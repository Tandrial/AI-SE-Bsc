// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.shapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.image.ImageUtils;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * Draw a line.
 *
 * @author Thomas Weise
 */
public class Line extends GraphicsNode {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the x-coordinates */
  private static final int[] X = new int[] { 0, 0, 1, 1 };

  /** the y-coordinates */
  private static final int[] Y = new int[] { 0, 1, 0, 1 };

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   */
  public Line(final Node<?>[] pchildren,
      final NodeType<Line, RealFunction> in) {
    super(pchildren, in, true);
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
    Graphics2D g;
    double thickness, color, direction;
    int start, end;
    final int choose;
    Stroke s;

    if (gc.step()) {

      color = this.get(0).compute(gc);

      thickness = this.get(1).compute(gc);
      if (Double.isNaN(thickness) || (thickness == 0d)) {
        return 0d;
      }

      if (thickness < 0d) {
        thickness = (-thickness);
      }
      if (thickness > 1d) {
        thickness = (1d / thickness);
      }

      direction = this.get(2).compute(gc);
      if (Double.isNaN(direction)) {
        return 0d;
      }

      if (direction < 0d) {
        direction = (-direction);
      }
      if (direction > 1d) {
        direction = (1d / direction);
      }

      choose = (int) (direction * X.length * X.length);

      end = (choose % X.length);
      start = (choose / X.length);

      g = gc.getGraphics();
      s = g.getStroke();
      g.setStroke(new BasicStroke((float) thickness));

      g.setColor(ImageUtils.decodeColor(color));
      g.drawLine(X[start], Y[start], X[end], Y[end]);

      g.setStroke(s);

      return color;
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
    sb.append("drawLine(col="); //$NON-NLS-1$
    this.get(0).fillInText(sb);
    sb.append(" thickness=");//$NON-NLS-1$
    this.get(1).fillInText(sb);
    sb.append(" dir=");//$NON-NLS-1$
    this.get(1).fillInText(sb);
    sb.append(')');
  }
}