// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.ea.fitnessAssignment;

import java.util.Random;

import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IFitnessAssignmentProcess;
import org.goataa.spec.IIndividualComparator;

/**
 * The Pareto ranking algorithm as discussed in
 * Section 28.3.3.
 *
 * @author Thomas Weise
 */
public class ParetoRanking extends FitnessAssignmentProcess {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared Pareto ranking procedure */
  public static final IFitnessAssignmentProcess PARETO_RANKING = new ParetoRanking();

  /** instantitate the pareto ranking algorithm */
  protected ParetoRanking() {
    super();
  }

  /**
   * Rank individuals according to the Pareto dominance relationship
   * Section 28.3.3.
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
  @Override
  public void assignFitness(final MOIndividual<?, ?>[] pop,
      final int start, final int count, final IIndividualComparator cmp,
      final Random r) {
    int i, j, res;
    MOIndividual<?, ?> a, b;

    for (i = (start + count); (--i) >= start;) {
      pop[i].v = 1d;
    }

    // compare each individual
    for (i = (start + count); (--i) > start;) {
      a = pop[i];
      // with each other individual
      for (j = i; (--j) >= start;) {
        b = pop[j];
        res = cmp.compare(a, b);
        if (res < 0) {
          // 'a' dominates 'b' -> make fitness of 'b' bigger = worse
          b.v++;
        } else {
          if (res > 0) {
            // 'b' dominates 'a' -> make fitness of 'a' bigger = worse
            a.v++;
          }
        }
      }
    }
  }

}