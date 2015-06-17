// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.recurse;

import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.image.RecursiveContext;

/**
 * Jump to a recursion target
 *
 * @author Thomas Weise
 */
public class Call extends GraphicsNode {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared function call */
  public static final Call CALL = new Call();

  /** Create a node call */
  private Call() {
    super(null, CallType.DEFAULT_CALL_TYPE, false);
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
    if (gc.step()) {
      return ((RecursiveContext) gc).call();
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
    sb.append("call"); //$NON-NLS-1$
  }

}