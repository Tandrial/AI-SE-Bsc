// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

/**
 * The interface ITemperatureSchedule allows us to implement the different
 * temperature scheduling methods for Simulated Annealing (see
 * Chapter 27) as listed in
 * Section 27.3.
 *
 * @author Thomas Weise
 */
public interface ITemperatureSchedule extends IOptimizationModule {

  /**
   * Get the temperature to be used in the iteration t. This method
   * implements the getTemperature function specified in
   * Equation 27.13.
   *
   * @param t
   *          the iteration
   * @return the temperature to be used for determining whether a worse
   *         solution should be accepted or not
   */
  public abstract double getTemperature(final int t);

}