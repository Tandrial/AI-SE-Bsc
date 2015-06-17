// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.ea.fitnessAssignment;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.Constants;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IFitnessAssignmentProcess;
import org.goataa.spec.IIndividualComparator;

/**
 * A base class for fitness assignment processes.
 *
 * @author Thomas Weise
 */
public class FitnessAssignmentProcess extends OptimizationModule implements
    IFitnessAssignmentProcess {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantitate the fitness assignment process */
  protected FitnessAssignmentProcess() {
    super();
  }

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
  public void assignFitness(final MOIndividual<?, ?>[] pop,
      final int start, final int count, final IIndividualComparator cmp,
      final Random r) {
    int i;

    for (i = (start + count); (--i) >= start;) {
      pop[i].v = Constants.WORST_FITNESS;
    }
  }
}