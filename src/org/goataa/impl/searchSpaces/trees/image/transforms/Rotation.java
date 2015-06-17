// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.transforms;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * Rotate an area: the first child describes the rotation angle, the second
 * one the filling.
 *
 * @author Thomas Weise
 */
public class Rotation extends GraphicsNode {
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
  public Rotation(final Node<?>[] pchildren,
      final NodeType<Rotation, RealFunction> in) {
    super(pchildren, in, false);
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
    final double rot, res;
    final AffineTransform at;

    if (!(gc.step())) {
      return 0d;
    }

    rot = this.get(0).compute(gc);
    if (Double.isNaN(rot) || Double.isInfinite(rot)) {
      return 0d;
    }

    g = gc.getGraphics();
    at = g.getTransform();
    g.rotate(Math.min(1e10d, Math.max(-1e10d, rot)));
    res = this.get(1).compute(gc);
    g.setTransform(at);

    return res;
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    sb.append("rotate(by="); //$NON-NLS-1$
    this.get(0).fillInText(sb);
    sb.append(",fill="); //$NON-NLS-1$
    this.get(1).fillInText(sb);
    sb.append(')');
  }

}