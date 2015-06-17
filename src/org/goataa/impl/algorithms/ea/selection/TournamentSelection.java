/*
 * Copyright (c) 2010 Thomas Weise
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package org.goataa.impl.algorithms.ea.selection;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.ISelectionAlgorithm;

/**
 * The tournament selection algorithm with replacement as specified in
 * Algorithm 28.11 in
 * Section 28.4.4.
 *
 * @author Thomas Weise
 */
public class TournamentSelection extends OptimizationModule implements
    ISelectionAlgorithm {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the tournament size */
  private final int k;

  /**
   * Create a new instance of the tournament selection algorithm with
   * replacement
   *
   * @param ik
   *          the tournament size
   */
  public TournamentSelection(final int ik) {
    super();
    this.k = ik;
  }

  /**
   * Perform the tournament selection as defined in
   * Algorithm 28.11.
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
  @Override
  public void select(final Individual<?, ?>[] pop, final int popStart,
      final int popCount, final Individual<?, ?>[] mate,
      final int mateStart, final int mateCount, final Random r) {
    Individual<?, ?> x, y;
    int i, j;

    for (i = (mateStart + mateCount); (--i) >= mateStart;) {
      x = pop[popStart + r.nextInt(popCount)];
      for (j = 2; j <= this.k; j++) {
        y = pop[popStart + r.nextInt(popCount)];
        if (y.v < x.v) {
          x = y;
        }
      }
      mate[i] = x;
    }

  }

  /**
   * Get the name of the optimization module
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  @Override
  public String getName(final boolean longVersion) {
    if (longVersion) {
      return this.getClass().getSimpleName();
    }
    return "Tour"; //$NON-NLS-1$
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  @Override
  public String getConfiguration(final boolean longVersion) {
    return "k=" + String.valueOf(this.k); //$NON-NLS-1$
  }
}