// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image;

import java.awt.geom.AffineTransform;

/**
 * A utility class for pixel windows
 *
 * @author Thomas Weise
 */
public class InvertibleWindow extends PixelWindow {

  /** the temporary array */
  private final double[] temp2;

  /** the original minimum x */
  private double ominX;

  /** the original maximum x */
  private double omaxX;

  /** the original minimum y */
  private double ominY;

  /** the original maximum y */
  private double omaxY;

  /** Create a new pixel window */
  public InvertibleWindow() {
    super();
    this.temp2 = new double[DEFAULT_COORDS.length];
  }

  /**
   *Compute
   *
   * @param at
   *          the affine transform
   *@param ow
   *          the original width
   *@param oh
   *          the original height
   */
  @Override
  void compute(final AffineTransform at, final int ow, final int oh) {
    AffineTransform otx;
    final double[] t1, t2;
    double minx, maxx, miny, maxy, d;
    int i;

    super.compute(at, ow, oh);

    otx = null;
    if ((this.w < ow) || (this.h < oh)) {
      if (at != null) {
        try {
          otx = at.createInverse();
        } catch (Throwable t) {
          otx = null;
        }
      }
    }

    if (otx == null) {
      this.omaxX = 1d;
      this.omaxY = 1d;
      this.ominX = 0d;
      this.ominY = 0d;
      return;
    }

    t1 = this.temp;
    t1[0] = (-1);
    t1[1] = (-1);
    t1[2] = (-1);
    t1[3] = (this.maxY + 1);
    t1[4] = (this.maxX + 1);
    t1[5] = (this.maxY + 1);
    t1[6] = (this.maxX + 1);
    t1[7] = (this.minY - 1);

    t2 = this.temp2;
    otx.transform(t1, 0, t2, 0, 4);

    minx = maxx = t2[0];
    miny = maxy = t2[1];

    for (i = t2.length; (--i) > 1;) {
      d = t2[i];
      if (d < miny) {
        miny = d;
      } else {
        if (d > maxy) {
          maxy = d;
        }
      }
      i--;
      d = t2[i];
      if (d < minx) {
        minx = d;
      } else {
        if (d > maxx) {
          maxx = d;
        }
      }
    }

    this.ominX = Math.min(1d, Math.max(0d, minx));
    this.ominY = Math.min(1d, Math.max(0d, miny));
    this.omaxX = Math.min(1d, Math.max(0d, maxx));
    this.omaxY = Math.min(1d, Math.max(0d, maxy));
  }

  /**
   * Get the (inclusive) minimum x coordinate of the current window in the
   * original coordinate space
   *
   * @return the (inclusive) minimum x coordinate of the current window in
   *         the original coordinate space
   */
  public final double origMinX() {
    return this.ominX;
  }

  /**
   * Get the (inclusive) maximum x coordinate of the current window in the
   * original coordinate space
   *
   * @return the (exclusive) maximum x coordinate of the current window in
   *         the original coordinate space
   */
  public final double origMaxX() {
    return this.omaxX;
  }

  /**
   * Get the (inclusive) minimum y coordinate of the current window in the
   * original coordinate space
   *
   * @return the (inclusive) minimum y coordinate of the current window in
   *         the original coordinate space
   */
  public final double origMinY() {
    return this.ominY;
  }

  /**
   * Get the (inclusive) maximum y coordinate of the current window in the
   * original coordinate space
   *
   * @return the (exclusive) maximum y coordinate of the current window in
   *         the original coordinate space
   */
  public final double origMaxY() {
    return this.omaxY;
  }
}