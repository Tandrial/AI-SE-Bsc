// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.objectiveFunctions;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IObjectiveFunction;

/**
 * A base class for objective functions
 *
 * @param <X>
 *          the problem space
 * @author Thomas Weise
 */
public class ObjectiveFunction<X> extends OptimizationModule implements
    IObjectiveFunction<X> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantiate the base class */
  public ObjectiveFunction() {
    super();
  }

  /**
   * Compute the objective value
   *
   * @param x
   *          the phenotype to be rated
   * @param r
   *          the randomizer
   * @return the objective value of x, the lower the better (see
   *         Section 6.3.4)
   */
  public double compute(final X x, final Random r) {
    return 0d;
  }
}