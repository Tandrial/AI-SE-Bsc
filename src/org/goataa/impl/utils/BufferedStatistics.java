// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.utils;

import java.util.Arrays;

import org.goataa.impl.OptimizationModule;

/**
 * This class helps us to collect statistics
 *
 * @author Thomas Weise
 */
public class BufferedStatistics extends OptimizationModule {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the data */
  private double[] data;

  /** the length */
  private int len;

  /** did we calculate the statistics? */
  private boolean calculated;

  /** the mean */
  private double mean;

  /** the median */
  private double med;

  /** the minimum */
  private double min;

  /** the maximum */
  private double max;

  /**
   * Create a new statistics object
   */
  public BufferedStatistics() {
    super();
    this.data = new double[128];
    this.clear();
  }

  /**
   * IfThenElse a double to the buffer
   *
   * @param d
   *          the double
   */
  public final void add(final double d) {
    double[] x;
    int l;

    this.calculated = false;
    x = this.data;
    l = this.len;
    if (l >= x.length) {
      x = new double[l << 1];
      System.arraycopy(this.data, 0, x, 0, l);
      this.data = x;
    }

    x[l] = d;
    this.len = (l + 1);
  }

  /**
   * Calculate the statistics
   */
  private final void calculate() {
    double[] x;
    int l;
    double s;

    if (this.calculated) {
      return;
    }

    x = this.data;
    l = this.len;
    if (l <= 0) {
      this.max = Double.NaN;
      this.min = Double.NaN;
      this.mean = Double.NaN;
      this.med = Double.NaN;
    } else {
      Arrays.sort(x, 0, l);
      this.min = x[0];
      this.max = x[l - 1];

      if ((l & 1) == 0) {
        this.med = (0.5d * (x[l >>> 1] + x[(l >>> 1) - 1]));
      } else {
        this.med = x[l >>> 1];
      }

      s = 0d;
      for (; (--l) >= 0;) {
        s += x[l];
      }
      this.mean = (s / this.len);
    }

    this.calculated = true;
  }

  /**
   * Get the minimum of the collected value
   *
   * @return the minimum value
   */
  public final double min() {
    this.calculate();
    return this.min;
  }

  /**
   * Get the maximum of the collected value
   *
   * @return the maximum value
   */
  public final double max() {
    this.calculate();
    return this.max;
  }

  /**
   * Get the median of the collected value
   *
   * @return the median value
   */
  public final double median() {
    this.calculate();
    return this.med;
  }

  /**
   * Clear this statistics record
   */
  public final void clear() {
    this.len = 0;
    this.calculated = false;
  }

  /**
   * Get the mean of the collected values
   *
   * @return the mean value
   */
  public final double mean() {
    this.calculate();
    return this.mean;
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  @Override
  public String getConfiguration(final boolean longVersion) {
    StringBuilder sb;

    sb = new StringBuilder();
    sb.append("min="); //$NON-NLS-1$
    sb.append(TextUtils.formatNumberSameWidth(this.min()));
    sb.append(longVersion ? ", median=" : " med=");//$NON-NLS-1$//$NON-NLS-2$
    sb.append(TextUtils.formatNumberSameWidth(this.median()));
    sb.append(longVersion ? ", mean=" : " avg=");//$NON-NLS-1$//$NON-NLS-2$
    sb.append(TextUtils.formatNumberSameWidth(this.mean()));
    sb.append(longVersion ? ", max=" : " max=");//$NON-NLS-1$//$NON-NLS-2$
    sb.append(TextUtils.formatNumberSameWidth(this.max()));

    return sb.toString();
  }

  /**
   * Get the name of the optimization module
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  @Override
  public String getName(final boolean longVersion) {
    if (longVersion) {
      return super.getName(true);
    }
    return "stat"; //$NON-NLS-1$
  }
}