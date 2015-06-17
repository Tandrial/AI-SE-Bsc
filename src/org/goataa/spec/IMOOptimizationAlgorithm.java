// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import org.goataa.impl.utils.MOIndividual;

/**
 * A simple and general interface which enables us to provide the
 * functionality of a multi-objective optimization algorithm (see
 * Section 1.3 and
 * Definition D6.3) in a unified way.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public interface IMOOptimizationAlgorithm<G, X> extends
    ISOOptimizationAlgorithm<G, X, MOIndividual<G, X>> {

  /**
   * Get the individual comparator
   *
   * @return the individual comparator
   */
  public abstract IIndividualComparator getComparator();

  /**
   * Set the individual comparator
   *
   * @param pcmp
   *          the individual comparator
   */
  public abstract void setComparator(final IIndividualComparator pcmp);

  /**
   * Get the objective functions
   *
   * @return the objective functions
   */
  public abstract IObjectiveFunction<X>[] getObjectiveFunctions();

  /**
   * Set the objective functions
   *
   * @param o
   *          the objective functions
   */
  public abstract void setObjectiveFunctions(
      final IObjectiveFunction<X>[] o);

}