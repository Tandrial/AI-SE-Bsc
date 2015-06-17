// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.sa.temperatureSchedules;

/**
 * A logarithmic temperature schedule as defined in
 * Section 27.3.1 which implements the interface
 * ITemperatureSchedule given in
 * Listing 55.12.
 *
 * @author Thomas Weise
 */
public class Logarithmic extends TemperatureSchedule {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a new logarithmic temperature schedule
   *
   * @param pTs
   *          the starting temperature
   */
  public Logarithmic(final double pTs) {
    super(pTs);
  }

  /**
   * Get the temperature to be used in the iteration t according to a
   * logarithmic schedule Section 27.3.1
   * according to Equation 27.13.
   *
   * @param t
   *          the iteration
   * @return the temperature to be used for determining whether a worse
   *         solution should be accepted or not
   */
  @Override
  public final double getTemperature(final int t) {
    if (t < Math.E) {
      return this.Ts;
    }
    return (this.Ts / Math.log(t));
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
    return "log"; //$NON-NLS-1$
  }
}