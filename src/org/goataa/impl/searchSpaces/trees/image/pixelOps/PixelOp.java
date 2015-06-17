// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.pixelOps;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.image.GraphicsContext;
import org.goataa.impl.searchSpaces.trees.image.GraphicsNode;
import org.goataa.impl.searchSpaces.trees.image.PixelWindow;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * RasterFilter an image.
 *
 * @author Thomas Weise
 */
public abstract class PixelOp extends GraphicsNode {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the clip polygone */
  private static final double[] CLIP = new double[] {//
  0d, 0d, 1d, 0d, 1d, 1d, 0d, 1d };

  /** the internal pixel window */
  private static final PixelWindow W = new PixelWindow();

  /** the dest image */
  private static transient WritableRaster dest;

  /** the clip buffer */
  private static final double[] CLIP_TRANSFORM = new double[CLIP.length];

  /** the x-coordinates */
  private static final int[] XP = new int[CLIP.length >>> 1];

  /** the y-coordinates */
  private static final int[] YP = new int[CLIP.length >>> 1];

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   */
  public PixelOp(final Node<?>[] pchildren,
      final NodeType<? extends PixelOp, RealFunction> in) {
    super(pchildren, in, true);
  }

  /**
   *Filter the image
   *
   * @param src
   *          the source
   * @param dst
   *          the destination
   * @return the image
   */
  protected abstract WritableRaster filter(final Raster src,
      final WritableRaster dst);

  /**
   * Get the number of additional pixels required in each direction
   *
   * @return the number of additional pixels required in each direction
   */
  protected int getAdditionalPixels() {
    return 1;
  }

  /**
   * The compute functon of graphics contexts
   *
   * @param gc
   *          the graphics context
   * @return the result
   */
  @Override
  public final double compute(final GraphicsContext gc) {
    double d;
    int s, i, ww, hh, mix, miy, dw, dh;
    final int add;
    final Raster src;
    final WritableRaster asrc;
    WritableRaster dst;
    final Graphics2D gr;
    final AffineTransform oldtransform;

    if (gc.step()) {

      // compute the involved area
      W.init(gc);

      ww = W.getWidth();
      if (ww <= 0) {
        return 0d;
      }

      hh = W.getHeight();
      if (hh <= 0) {
        return 0d;
      }

      // execute children first
      d = 0d;
      for (s = this.size(); (--s) >= 0;) {
        d = this.get(s).compute(gc);
      }

      // then apply filter
      if ((ww > 1) || (hh > 1)) {

        mix = s = W.minX();
        miy = i = W.minY();

        // increase area size for proper image pixel computation
        add = Math.max(1, this.getAdditionalPixels());
        mix = Math.max(0, mix - add);
        miy = Math.max(0, miy - add);

        ww = Math.min(gc.getWidth(), ww + (s - mix) + add);
        hh = Math.min(gc.getHeight(), hh + (i - miy) + add);

        // perform the operation on a rectanguar basis data
        try {
          asrc = gc.getImage().getRaster();

          src = asrc.createChild(mix, miy, ww, hh, 0, 0, null);
          dst = dest;

          if (dst == null) {
            dest = dst = asrc.createCompatibleWritableRaster();
          }
          dw = dst.getWidth();
          dh = dst.getHeight();
          if ((dw < ww) || (dh < hh)) {
            dest = dst = asrc.createCompatibleWritableRaster();
            dw = dst.getWidth();
            dh = dst.getHeight();
          }
          if ((dw != ww) || (dh != hh)) {
            dst = dest.createWritableChild(0, 0, ww, hh, 0, 0, null);
          }

          dst = this.filter(src, dst);
        } catch (Throwable t) {
          return 0d;
        }

        // compute the clip
        gr = gc.getGraphics();
        oldtransform = gr.getTransform();

        s = CLIP_TRANSFORM.length;
        i = (s >>> 1);
        oldtransform.transform(CLIP, 0, CLIP_TRANSFORM, 0, i);
        for (; (--i) >= 0;) {
          YP[i] = (int) (0.5d + (CLIP_TRANSFORM[--s]));
          XP[i] = (int) (0.5d + (CLIP_TRANSFORM[--s]));
        }

        // copy the data into the clipped region
        copyRaster(dst, 0, 0,//
            asrc, mix, miy, ww, hh,//
            new Polygon(XP, YP, XP.length));
      }

      return d;
    }

    return 0d;
  }

  /**
   * Copy a raster
   *
   * @param src
   *          the source
   * @param srcMinX
   *          the minimum x source coordinate
   * @param srcMinY
   *          the minimum y source coordinate
   * @param dst
   *          the dest
   * @param dstMinX
   *          the minimum x destination coordinate
   * @param dstMinY
   *          the minimum y destination coordinate
   * @param width
   *          the width
   * @param height
   *          the height
   * @param mask
   *          the mask
   */
  private static final void copyRaster(final Raster src,
      final int srcMinX, final int srcMinY, final WritableRaster dst,
      final int dstMinX, final int dstMinY, final int width,
      final int height, final Polygon mask) {
    int[] data;
    int i, j, dx, dy;

    data = null;
    for (i = height; (--i) >= 0;) {
      for (j = width; (--j) >= 0;) {
        dx = (j + dstMinX);
        dy = (i + dstMinY);
        if (mask.contains(dx, dy)) {
          data = src.getPixel(j + srcMinX, i + srcMinY, data);
          dst.setPixel(dx, dy, data);
        }
      }
    }
  }

}