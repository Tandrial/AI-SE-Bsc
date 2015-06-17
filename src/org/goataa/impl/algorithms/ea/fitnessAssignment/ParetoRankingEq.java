// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.ea.fitnessAssignment;

import java.util.Random;

import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IFitnessAssignmentProcess;
import org.goataa.spec.IIndividualComparator;

/**
 * A ranking algorithm which also punishes same objective values.
 *
 * @author Thomas Weise
 */
public class ParetoRankingEq extends FitnessAssignmentProcess {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared Pareto ranking procedure with equality punishment */
  public static final IFitnessAssignmentProcess PARETO_RANKING_EQ = new ParetoRankingEq();

  /** instantitate the pareto ranking algorithm with equality punishment */
  protected ParetoRankingEq() {
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
    int i, j, res, k, sc;
    final double pun;
    MOIndividual<?, ?> a, b;
    double[] af, bf;

    for (i = (start + count); (--i) >= start;) {
      pop[i].v = 1d;
    }

    pun = 1d;

    // compare each individual
    for (i = (start + count); (--i) > start;) {
      a = pop[i];
      af = a.f;

      // with each other individual
      for (j = i; (--j) >= start;) {
        b = pop[j];
        bf = b.f;

        res = cmp.compare(a, b);

        if (res < 0) {
          // 'a' dominates 'b' -> make fitness of 'b' bigger = worse
          b.v += pun;
        } else {
          if (res > 0) {
            // 'b' dominates 'a' -> make fitness of 'a' bigger = worse
            a.v += pun;
          }
        }

        // count the number of exactly same objective values
        sc = 0;
        for (k = bf.length; (--k) >= 0;) {
          if (Double.compare(af[k], bf[k]) == 0) {
            sc++;
          }
        }

        a.v += sc;
        b.v += sc;
      }
    }
  }

}