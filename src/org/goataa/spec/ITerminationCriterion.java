// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

/**
 * The termination criterion is used to tell the optimization process when
 * to stop. It is defined in Section 6.3.3.
 *
 * @author Thomas Weise
 */
public interface ITerminationCriterion extends IOptimizationModule {

  /**
   * The function terminationCriterion, as stated in
   * Definition D6.7, returns a Boolean value which
   * is true if the optimization process should terminate and false
   * otherwise.
   *
   * @return true if the optimization process should terminate, false if it
   *         can continue
   */
  public abstract boolean terminationCriterion();

  /**
   * Reset the termination criterion to make it useable more than once
   */
  public abstract void reset();
}