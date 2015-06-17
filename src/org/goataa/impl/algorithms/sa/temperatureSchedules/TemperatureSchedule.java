// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.sa.temperatureSchedules;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.TextUtils;
import org.goataa.spec.ITemperatureSchedule;

/**
 * The base class for temperature schedules.
 *
 * @author Thomas Weise
 */
public class TemperatureSchedule extends OptimizationModule implements
    ITemperatureSchedule {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the start temperature */
  final double Ts;

  /**
   * Create a new logarithmic temperature schedule
   *
   * @param pTs
   *          the starting temperature
   */
  protected TemperatureSchedule(final double pTs) {
    super();
    this.Ts = pTs;
  }

  /**
   * Get the temperature.
   *
   * @param t
   *          the iteration
   * @return the temperature
   */
  public double getTemperature(final int t) {
    return this.Ts;
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
    return (longVersion ? ("Ts=" + TextUtils.formatNumber(this.Ts)) : //$NON-NLS-1$
        TextUtils.formatNumber(this.Ts));
  }

}