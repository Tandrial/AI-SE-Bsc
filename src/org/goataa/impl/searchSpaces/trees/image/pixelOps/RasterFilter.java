// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.pixelOps;

import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;
import java.io.Serializable;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * RasterFilter an image.
 *
 * @author Thomas Weise
 */
public class RasterFilter extends PixelOp {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the operation */
  final RasterOperation operation;

  /**
   * Create a node with the given children
   *
   * @param pchildren
   *          the child nodes
   * @param in
   *          the node information record
   * @param op
   *          the operation
   */
  public RasterFilter(final Node<?>[] pchildren,
      final NodeType<RasterFilter, RealFunction> in,
      final RasterOperation op) {
    super(pchildren, in);
    this.operation = op;
  }

  /**
   * Get the number of additional pixels required in each direction
   *
   * @return the number of additional pixels required in each direction
   */
  @Override
  protected int getAdditionalPixels() {
    return this.operation.add;
  }

  /**
   * Create a compatible image
   *
   * @param src
   *          the source
   * @param dst
   *          the destination
   * @return the image
   */
  @Override
  protected WritableRaster filter(final Raster src,
      final WritableRaster dst) {
    return this.operation.cop.filter(src, dst);
  }

  /**
   * Fill in the text associated with this node
   *
   * @param sb
   *          the string builder
   */
  @Override
  public void fillInText(final StringBuilder sb) {
    sb.append(this.operation.name);
    sb.append(", fill="); //$NON-NLS-1$
    this.get(0).fillInText(sb);
    sb.append(')');
  }

  /**
   * Compare with another object
   *
   * @param o
   *          the other object
   * @return true if the objects are equal
   */
  @Override
  public boolean equals(final Object o) {
    RasterFilter w;

    if (o == this) {
      return true;
    }
    if (!(o instanceof RasterFilter)) {
      return false;
    }

    w = ((RasterFilter) o);

    return ((w.operation == this.operation) && (w.get(0).equals(this
        .get(0))));
  }

  /** the convolve operation class */
  static final class RasterOperation implements Serializable {
    /** a constant required by Java serialization */
    private static final long serialVersionUID = 1;

    /** the operation */
    final RasterOp cop;

    /** the name */
    final char[] name;

    /** the additional pixels in each direction */
    final int add;

    /**
     * Create the convolve operation
     *
     * @param xop
     *          the operation
     * @param a
     *          the additional pixels required in each direction
     * @param n
     *          the name
     */
    RasterOperation(final String n, final int a, final RasterOp xop) {
      super();
      this.cop = xop;
      this.add = Math.max(1, a);
      this.name = ("raster(" + n).toCharArray(); //$NON-NLS-1$
    }
  }
}