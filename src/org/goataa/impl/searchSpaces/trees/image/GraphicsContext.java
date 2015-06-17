// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.goataa.impl.searchSpaces.trees.math.real.RealContext;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;
import org.goataa.impl.utils.Utils;

/**
 * A graphics context
 *
 * @author Thomas Weise
 */
public class GraphicsContext extends RealContext {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internal image */
  private final BufferedImage bi;

  /** the graphics */
  private final Graphics2D gr;

  /** the image width */
  private final int w;

  /** the image height */
  private final int h;

  /** the original affine transform */
  private final AffineTransform oat;

  /** the original stroke */
  private final Stroke ost;

  /** the original clip */
  private final Shape oc;

  /** the original composite */
  private final Composite oco;

  /** the original font */
  private final Font of;

  /** the paint */
  private final Paint paint;

  /**
   * Create a new graphics context
   *
   * @param maxSteps
   *          the maximum number of allowed steps
   * @param addMemoryCells
   *          the additionally provided memory cells
   * @param imageWidth
   *          the image width
   * @param imageHeight
   *          the image height
   */
  public GraphicsContext(final int maxSteps, final int addMemoryCells,
      final int imageWidth, final int imageHeight) {
    super(maxSteps, ImageUtils.RESERVED_MEMORY_CELLS + addMemoryCells);

    this.bi = ImageUtils.createImage(imageWidth, imageHeight);
    this.gr = ((Graphics2D) (this.bi.getGraphics()));
    this.w = imageWidth;
    this.h = imageHeight;
    this.oat = this.gr.getTransform();
    this.ost = this.gr.getStroke();
    this.oc = this.gr.getClip();
    this.oco = this.gr.getComposite();
    this.of = this.gr.getFont();
    this.paint = this.gr.getPaint();
  }

  /** Clear the image */
  public void clearImage() {
    this.gr.setTransform(this.oat);
    this.gr.setStroke(this.ost);
    this.gr.setClip(this.oc);
    this.gr.setComposite(this.oco);
    this.gr.setFont(this.of);
    this.gr.setPaint(this.paint);

    this.gr.setColor(Color.white);
    this.gr.setBackground(Color.white);
    this.gr.clearRect(0, 0, this.w, this.h);
    this.gr.fillRect(0, 0, this.w, this.h);

    this.gr.scale(this.w, this.h);
  }

  /** begin the program */
  @Override
  public void beginProgram() {
    super.beginProgram();
    this.clearImage();
  }

  /**
   * Get the width in pixels
   *
   * @return the width
   */
  public final int getWidth() {
    return this.w;
  }

  /**
   * Get the height in pixels
   *
   * @return the height
   */
  public final int getHeight() {
    return this.h;
  }

  /** end the program */
  @Override
  public void endProgram() {
    this.bi.flush();
    super.endProgram();
  }

  /**
   * Get the graphics object
   *
   * @return the graphics object
   */
  public final Graphics2D getGraphics() {
    return this.gr;
  }

  /**
   * Get the image in this context
   *
   * @return the image in this context
   */
  public final BufferedImage getImage() {
    return this.bi;
  }

  /**
   * Draw a function on this context
   *
   * @param f
   *          the function
   */
  public final void draw(final RealFunction f) {

    try {
      this.beginProgram();
      try {
        f.compute(this);
      } finally {
        this.endProgram();
      }
    } catch (Throwable t) {
      Utils.gc();
    }

  }

}