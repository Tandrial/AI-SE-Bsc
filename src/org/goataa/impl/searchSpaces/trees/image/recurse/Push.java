// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.recurse;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.image.RecursiveContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * Push a recursion target
 *
 * @author Thomas Weise
 */
public class Push extends GraphicsNode {
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
  public Push(final Node<?>[] pchildren,
      final NodeType<Push, RealFunction> in) {
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
    double times;
    RealFunction f;
    int s;

    if (gc.step()) {
      times = Math.abs(this.get(0).compute(gc));
      if (times < 1d) {
        times = (1d / times);
      }
      if (Double.isInfinite(times) || (Double.isNaN(times))
          || (times >= Integer.MAX_VALUE)) {
        s = 1;
      } else {
        s = Math.max(1, ((int) (0.5d + times)));
      }

      f = this.get(1);
      if (!((RecursiveContext) gc).push(f, s)) {
        return 0d;
      }

      return f.compute(gc);
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

    sb.append("push("); //$NON-NLS-1$
    this.get(0).fillInText(sb);
    sb.append(" times "); //$NON-NLS-1$
    this.get(1).fillInText(sb);
    sb.append(')');
  }

}