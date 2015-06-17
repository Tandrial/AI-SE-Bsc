// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.sa.temperatureSchedules;

import org.goataa.impl.utils.TextUtils;

/**
 * An exponential temperature schedule as defined in
 * Section 27.3.2 which implements the interface
 * ITemperatureSchedule given in
 * Listing 55.12.
 *
 * @author Thomas Weise
 */
public class Exponential extends TemperatureSchedule {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the epsilong value */
  private final double e;

  /**
   * Create a new exponential temperature schedule
   *
   * @param pTs
   *          the starting temperature
   * @param pE
   *          the epsilon value
   */
  public Exponential(final double pTs, final double pE) {
    super(pTs);
    this.e = pE;
  }

  /**
   * Get the temperature to be used in the iteration t according to a
   * exponential schedule Section 27.3.2
   * according to Equation 27.13.
   *
   * @param t
   *          the iteration
   * @return the temperature to be used for determining whether a worse
   *         solution should be accepted or not
   */
  @Override
  public final double getTemperature(final int t) {
    return (Math.pow((1 - this.e), t) * this.Ts);
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
      return this.getClass().getSimpleName();
    }
    return "exp"; //$NON-NLS-1$
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
    return super.getConfiguration(longVersion) + ','
        + (longVersion ? ("e=" + TextUtils.formatNumber(this.e)) : //$NON-NLS-1$
            TextUtils.formatNumber(this.e));
  }
}