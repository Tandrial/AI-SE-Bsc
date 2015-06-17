// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.shapes;

import java.awt.Graphics2D;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.image.ImageUtils;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * Draw a rectangular shape. It has at least one child: its color. An
 * arbitrary number of additional children can be used for inner shapes.
 *
 * @author Thomas Weise
 */
public class Rectangle extends GraphicsNode {
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
  public Rectangle(final Node<?>[] pchildren,
      final NodeType<Rectangle, RealFunction> in) {
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
    double d;
    int s;

    if (gc.step()) {
      d = this.get(0).compute(gc);
      g = gc.getGraphics();
      g.setColor(ImageUtils.decodeColor(d));
      g.fillRect(0, 0, 1, 1);

      for (s = this.size(); (--s) > 0;) {
        this.get(s).compute(gc);
      }

      return d;
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
    int s;

    sb.append("drawRect(col="); //$NON-NLS-1$
    this.get(0).fillInText(sb);

    for (s = this.size(); (--s) > 0;) {
      sb.append(';');
      sb.append(' ');
      this.get(s).fillInText(sb);
    }

    sb.append(')');
  }

}