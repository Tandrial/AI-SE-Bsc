// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A graphics node
 *
 * @author Thomas Weise
 */
public class GraphicsNode extends RealFunction {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a graphics node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   * @param eff
   *          does this node have an effect on the overall system state
   */
  public GraphicsNode(final Node<?>[] pchildren,
      final NodeType<? extends RealFunction, RealFunction> in,
      final boolean eff) {
    super(pchildren, in, eff);
  }

  /**
   * The compute functon of graphics contexts
   *
   * @param gc
   *          the graphics context
   * @return the result
   */
  public double compute(final GraphicsContext gc) {
    return 0d;
  }

  /**
   * This is the core method of mathematics functions: it computes the
   * result of this function and can be invoked recursively downwards for
   * the other nodes.
   *
   * @param data
   *          the context containing the data vector
   * @return the result of the computation
   */
  @Override
  public final double compute(final RealContext data) {
    return this.compute((GraphicsContext) data);
  }
}