// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * A set of utilities for evolutionary art
 *
 * @author Thomas Weise
 */
public class ImageUtils {
  /** the memory address holding x coordinate */
  public static final int ADDRESS_X = (0);

  /** the memory address holding y coordinate */
  public static final int ADDRESS_Y = (ADDRESS_X + 1);

  /** the reserved memory cells */
  public static final int RESERVED_MEMORY_CELLS = (ADDRESS_Y + 1);

  /** the alpha frequency */
  private static final double A_FREQ = (2 * Math.PI);

  /** the red frequency */
  private static final double R_FREQ = A_FREQ + 1d;

  /** the green frequency */
  private static final double G_FREQ = R_FREQ + 1d;

  /** the blue frequency */
  private static final double B_FREQ = G_FREQ + 1d;

  /**
   * The internal sinus conversion function
   *
   * @param x
   *          the x coordinate
   * @param f
   *          the frequency
   * @return the sinus
   */
  private static final float isin(final double x, final double f) {
    return Math.min(1f, Math.max(0f, (float) (0.5d + Math.sin(x * f))));
  }

  /**
   * Convert a double value to a color. The value v can be more or less
   * arbitrary and is translated to a color and alpha value.
   *
   * @param v
   *          the double value completely describing the color
   * @return the color
   */
  public static final Color decodeColor(final double v) {
    if (Double.isInfinite(v) || Double.isNaN(v)) {
      return Color.BLACK;
    }
    return new Color(//
        isin(v, R_FREQ),//
        isin(v, G_FREQ),//
        isin(v, B_FREQ),//
        isin(v, A_FREQ));
  }

  /**
   * Load a buffered image from a file
   *
   * @param file
   *          the file
   * @return the image
   */
  public static final BufferedImage load(final String file) {
    try {
      return ImageIO.read(new File(file));
    } catch (Throwable t) {
      t.printStackTrace();
      return null;
    }
  }

  /**
   * Store a buffered image
   *
   * @param bi
   *          the buffered image
   * @param file
   *          the file name
   */
  public static final void store(final BufferedImage bi, final String file) {
    String ending;

    ending = file.substring(file.lastIndexOf('.') + 1).toLowerCase();
    try {
      ImageIO.write(bi, ending, new File(file));
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /**
   * Create an image of the given width and height
   *
   * @param imageWidth
   *          the image width
   * @param imageHeight
   *          the image height
   * @return the image
   */
  public static final BufferedImage createImage(final int imageWidth,
      final int imageHeight) {
    return new BufferedImage(imageWidth, imageHeight,
        BufferedImage.TYPE_INT_RGB);
  }
}