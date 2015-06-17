// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image;

import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A recursive context
 *
 * @author Thomas Weise
 */
public class RecursiveContext extends GraphicsContext {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the recursers */
  private final Recurser[] recurse;

  /** the depth */
  private int depth;

  /**
   * Create a new graphics context
   *
   * @param maxSteps
   *          the maximum number of allowed steps
   * @param maxDepth
   *          the maximum recursion depth
   * @param addMemoryCells
   *          the additionally provided memory cells
   * @param imageWidth
   *          the image width
   * @param imageHeight
   *          the image height
   */
  public RecursiveContext(final int maxSteps, final int maxDepth,
      final int addMemoryCells, final int imageWidth, final int imageHeight) {
    super(maxSteps, addMemoryCells, imageWidth, imageHeight);

    final Recurser[] r;
    int i;

    this.recurse = r = new Recurser[maxDepth];
    for (i = maxDepth; (--i) >= 0;) {
      r[i] = new Recurser();
    }
  }

  /**
   * Push a recursion target!
   *
   * @param f
   *          the function
   * @param maxCall
   *          the maximum time the function can be called before it expires
   * @return true if everything is ok, false on error
   */
  public final boolean push(final RealFunction f, final int maxCall) {
    int d;
    final Recurser[] rr;
    Recurser x;

    d = this.depth;
    rr = this.recurse;
    if (d >= rr.length) {
      this.error();
      return false;
    }

    x = rr[d++];
    this.depth = d;
    x.call = f;
    x.count = maxCall;

    return true;
  }

  /**
   * perform the recursion
   *
   * @return the recursion result
   */
  public final double call() {
    int d;
    final Recurser[] rr;
    Recurser x;
    RealFunction f;

    d = this.depth;

    if (d <= 0) {
      // this.error();
      return 0d;
    }

    rr = this.recurse;
    x = rr[--d];
    f = x.call;
    if ((--x.count) <= 0) {
      x.call = null;
      this.depth = d;
    }

    return f.compute(this);
  }

  /** end the program */
  @Override
  public void endProgram() {
    final Recurser[] r;
    int i;

    super.endProgram();

    r = this.recurse;
    for (i = this.depth; (--i) >= 0;) {
      r[i].call = null;
    }
    this.depth = 0;
  }

  /**
   * The recurser information
   *
   * @author Thomas Weise
   */
  private static final class Recurser {
    /** the allowed number of steps to visit the recurser */
    int count;

    /** the function to call */
    RealFunction call;

    /** Create a new recurser */
    Recurser() {
      super();
    }
  }
}