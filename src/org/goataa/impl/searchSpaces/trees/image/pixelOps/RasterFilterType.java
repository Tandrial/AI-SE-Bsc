// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.pixelOps;

import java.awt.RenderingHints;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A convolve type
 *
 * @author Thomas Weise
 */
public class RasterFilterType extends NodeType<RasterFilter, RealFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the rendering hints */
  static final RenderingHints RH = new RenderingHints(
      RenderingHints.KEY_ALPHA_INTERPOLATION,
      RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

  static {
    RH.put(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    RH.put(RenderingHints.KEY_COLOR_RENDERING,
        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    RH.put(RenderingHints.KEY_DITHERING,
        RenderingHints.VALUE_DITHER_ENABLE);
    RH.put(RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    RH.put(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RH.put(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    RH.put(RenderingHints.KEY_STROKE_CONTROL,
        RenderingHints.VALUE_STROKE_NORMALIZE);
  }

  /** the convolution operation */
  private static final RasterFilter.RasterOperation[] OPS = new RasterFilter.RasterOperation[] {//
  // //
      new RasterFilter.RasterOperation("blur_3x3", 2, new ConvolveOp(//$NON-NLS-1$
          new Kernel(3, 3, buildArrayPlain(3, 3)), ConvolveOp.EDGE_NO_OP,
          RH)),//
      //
      new RasterFilter.RasterOperation("blur_5x5", 3, new ConvolveOp(//$NON-NLS-1$
          new Kernel(5, 5, buildArrayPlain(5, 5)), ConvolveOp.EDGE_NO_OP,
          RH)),//
      //
      new RasterFilter.RasterOperation("sharpen_3x3", 2, new ConvolveOp(//$NON-NLS-1$
          new Kernel(3, 3,
              new float[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 }),
          ConvolveOp.EDGE_NO_OP, RH)),//
      //
      new RasterFilter.RasterOperation("sharpen_5x5", 3, new ConvolveOp(//$NON-NLS-1$
          new Kernel(5, 5, new float[] { -1, -1, -1, -1, -1,//
              -1, -1, -1, -1, -1,//
              -1, -1, 25, -1, -1,//
              -1, -1, -1, -1, -1,//
              -1, -1, -1, -1, -1 }), ConvolveOp.EDGE_NO_OP, RH)),//
      //
      new RasterFilter.RasterOperation("emboss_3x3", 2, new ConvolveOp(//$NON-NLS-1$
          new Kernel(3, 3, new float[] { -2, 0, 0, 0, 1, 0, 0, 0, 2 }),
          ConvolveOp.EDGE_NO_OP, RH)),//

  };

  /**
   * The internal function to build a weight array
   *
   * @param w
   *          the width
   * @param h
   *          the height
   * @return the array
   */
  private static final float[] buildArrayPlain(final int w, final int h) {
    final float[] f;
    final int s;

    s = (w * h);
    f = new float[s];
    Arrays.fill(f, (1f / s));

    return f;
  }

  /**
   * Create a new variable type record
   *
   * @param ch
   *          the types of the possible chTypes
   */
  public RasterFilterType(final NodeTypeSet<RealFunction>[] ch) {
    super(ch);
  }

  /**
   * Instantiate a node
   *
   * @param children
   *          a given set of children
   * @param r
   *          the randomizer
   * @return the new node
   */
  @Override
  public RasterFilter instantiate(final Node<?>[] children, final Random r) {
    return new RasterFilter(children, this, OPS[r.nextInt(OPS.length)]);
  }

  /**
   * Slightly modify a constant.
   *
   * @param node
   *          the input node
   * @param r
   *          the randomizer
   * @return the modified node or the original value, if no modification is
   *         possible
   */
  @Override
  public RasterFilter mutate(final RasterFilter node, final Random r) {
    RasterFilter.RasterOperation a, b;

    a = node.operation;
    do {
      b = OPS[r.nextInt(OPS.length)];
    } while (b == a);

    return new RasterFilter(new Node[] { node.get(0) }, this, b);
  }
}