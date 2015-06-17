// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image;

import java.awt.geom.AffineTransform;

/**
 * A utility class for pixel windows
 *
 * @author Thomas Weise
 */
public class PixelWindow {

  /** default temp */
  static final double[] DEFAULT_COORDS = new double[]//
  { 0d, 0d, 0d, 1d, 1d, 0d, 1d, 1d };

  /** the temporary array */
  final double[] temp;

  /** the width */
  int w;

  /** the height */
  int h;

  /** the minimum x */
  int minX;

  /** the maximum x */
  int maxX;

  /** the minimum y */
  int minY;

  /** the maximum y */
  int maxY;

  /** Create a new pixel window */
  public PixelWindow() {
    super();
    this.temp = new double[DEFAULT_COORDS.length];
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
  void compute(final AffineTransform at, final int ow, final int oh) {
    final double[] tmp;
    double minx, miny, maxx, maxy, d;
    int i;
    final int ww, hh;

    tmp = this.temp;
    if (at == null) {
      this.maxX = this.w = ow;
      this.maxY = this.h = oh;
      this.minX = 0;
      this.minY = 0;
      return;
    }

    i = tmp.length;
    at.transform(DEFAULT_COORDS, 0, tmp, 0, i >>> 1);

    minx = maxx = tmp[0];
    miny = maxy = tmp[1];

    for (; (--i) > 1;) {
      d = tmp[i];
      if (d < miny) {
        miny = d;
      } else {
        if (d > maxy) {
          maxy = d;
        }
      }
      i--;
      d = tmp[i];
      if (d < minx) {
        minx = d;
      } else {
        if (d > maxx) {
          maxx = d;
        }
      }
    }

    ww = ow;
    hh = oh;

    this.maxX = Math.max(0, Math.min(ww, (int) (0.6d + Math.ceil(maxx))));
    this.minX = Math.max(0, Math.min(ww, (int) (0.2d + Math.floor(minx))));
    this.maxY = Math.max(0, Math.min(hh, (int) (0.6d + Math.ceil(maxy))));
    this.minY = Math.max(0, Math.min(hh, (int) (0.2d + Math.floor(miny))));

    this.w = this.maxX - this.minX;
    this.h = this.maxY - this.minY;
  }

  /**
   * Initialize
   *
   * @param gc
   *          the graphics context
   */
  public final void init(final GraphicsContext gc) {
    this.compute(gc.getGraphics().getTransform(),//
        gc.getWidth(), gc.getHeight());
  }

  /**
   * Get the width in pixels
   *
   * @return the width in pixels
   */
  public final int getWidth() {
    return this.w;
  }

  /**
   * Get the height in pixels
   *
   * @return the height in pixels
   */
  public final int getHeight() {
    return this.h;
  }

  /**
   * Get the (inclusive) minimum x coordinate of the current window
   *
   * @return the (inclusive) minimum x coordinate of the current window
   */
  public final int minX() {
    return this.minX;
  }

  /**
   * Get the (exclusive) maximum x coordinate of the current window
   *
   * @return the (exclusive) maximum x coordinate of the current window
   */
  public final int maxX() {
    return this.maxX;
  }

  /**
   * Get the (inclusive) minimum y coordinate of the current window
   *
   * @return the (inclusive) minimum y coordinate of the current window
   */
  public final int minY() {
    return this.minY;
  }

  /**
   * Get the (exclusive) maximum y coordinate of the current window
   *
   * @return the (exclusive) maximum y coordinate of the current window
   */
  public final int maxY() {
    return this.maxY;
  }
}