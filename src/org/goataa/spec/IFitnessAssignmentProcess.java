// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

import org.goataa.impl.utils.MOIndividual;

/**
 * A fitness assignment process translates the (vectors of) objective
 * values used in multi-objective optimization
 * (Section 3.3) of a set of individuals to
 * single fitness values as specified in
 * Definition D28.6.
 *
 * @author Thomas Weise
 */
public interface IFitnessAssignmentProcess extends IOptimizationModule {

  /**
   * Assign fitness to the individuals of a population based on their
   * objective values as specified in
   * Definition D28.6.
   *
   * @param pop
   *          the population of individuals
   * @param start
   *          the index of the first individual in the population
   * @param count
   *          the number of individuals in the population
   * @param cmp
   *          the individual comparator which tells which individual is
   *          better than another one
   * @param r
   *          than randomizer
   */
  public abstract void assignFitness(final MOIndividual<?, ?>[] pop,
      final int start, final int count, final IIndividualComparator cmp,
      final Random r);

}