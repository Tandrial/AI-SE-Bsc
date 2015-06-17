/*
 * Copyright (c) 2010 Thomas Weise
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package org.goataa.impl.algorithms.ea.selection;

import java.util.Random;

import org.goataa.impl.utils.Individual;
import org.goataa.spec.ISelectionAlgorithm;

/**
 * An selection operator which randomly selects individuals as specified in
 * Algorithm 28.15. This operator should mainly be used
 * for parental selection but not for survival selection.
 *
 * @author Thomas Weise
 */
public class RandomSelection extends SelectionAlgorithm {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of random selection */
  public static final ISelectionAlgorithm RANDOM_SELECTION = new RandomSelection();

  /** Create a new instance of the random selection algorithm */
  protected RandomSelection() {
    super();
  }

  /**
   * Perform the random selection as defined in
   * Algorithm 28.15.
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
  public final void select(final Individual<?, ?>[] pop,
      final int popStart, final int popCount,
      final Individual<?, ?>[] mate, final int mateStart,
      final int mateCount, final Random r) {
    int i;

    for (i = (mateStart + mateCount); (--i) >= mateStart;) {
      mate[i] = pop[popStart + r.nextInt(popCount)];
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
    return "RND"; //$NON-NLS-1$
  }
}