/*
 * Copyright (c) 2010 Thomas Weise
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package org.goataa.spec;

import java.util.Random;

import org.goataa.impl.utils.Individual;

/**
 * A selection algorithm (see Section 28.4) receives a
 * population of individuals (see Definition D4.18) as input and
 * chooses some individuals amongst them to fill a mating pool. Selection
 * is an essential step in Evolutionary Algorithms (EAs) which you can find
 * introduced in Chapter 28. In each
 * generation of an EA, a set of new individuals are created and evaluated.
 * All the individuals which are currently processed form the population
 * (see Definition D4.19). Some of these individuals will
 * further be investigated, i.e., processed by the search operations. The
 * selection step chooses which of the individuals should be chosen for
 * this purpose. Usually, this decision is randomized and better
 * individuals are selected with higher probability whereas worse
 * individuals are chosen with lower probability.
 *
 * @author Thomas Weise
 */
public interface ISelectionAlgorithm extends IOptimizationModule {

  /**
   * Perform the selection step (see Section 28.4), i.e.,
   * fill the mating pool mate with individuals chosen from the population
   * pop.
   *
   * @param pop
   *          the population (see Definition D4.19)
   * @param popStart
   *          the index of the first individual in the population
   * @param popCount
   *          the number of individuals to select from
   * @param mate
   *          the mating pool (see Definition D28.10)
   * @param mateStart
   *          the first index to which the selected individuals should be
   *          copied
   * @param mateCount
   *          the number of individuals to be selected
   * @param r
   *          the random number generator
   */
  public abstract void select(final Individual<?, ?>[] pop,
      final int popStart, final int popCount,
      final Individual<?, ?>[] mate, final int mateStart,
      final int mateCount, final Random r);

}