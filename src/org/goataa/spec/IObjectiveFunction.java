// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * In Section 2.2, we introduce the concept of
 * objective functions. An objective function is an optimization criterion
 * which rates one candidate solution (see
 * Definition D2.2) from the problem space according to
 * its utility. According to Section 6.3.4, smaller
 * objective values indicate better fitness, i.e., higher utility.
 *
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public interface IObjectiveFunction<X> extends IOptimizationModule {

  /**
   * Compute the objective value, i.e., determine the utility of the
   * solution candidate x as specified in
   * Definition D2.3.
   *
   * @param x
   *          the phenotype to be rated
   * @param r
   *          a randomizer
   * @return the objective value of x, the lower the better (see
   *         Section 6.3.4)
   */
  public abstract double compute(final X x, final Random r);

}