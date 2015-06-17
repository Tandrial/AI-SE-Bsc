/*
 * Copyright (c) 2010 Thomas Weise
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package org.goataa.impl.algorithms.ea.selection;

import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.Constants;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.ISelectionAlgorithm;

/**
 * An implementation of the truncation selection algorithm which unites
 * both, Algorithm 28.7 and
 * Algorithm 28.8 from
 * Section 28.4.2 in one class.
 *
 * @author Thomas Weise
 */
public class TruncationSelection extends OptimizationModule implements
    ISelectionAlgorithm {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the number of best individuals to select */
  private final int k;

  /** Create a new instance of the truncation selection algorithm */
  public TruncationSelection() {
    this(-1);
  }

  /**
   * Create a new instance of the truncation selection algorithm
   *
   * @param ak
   *          the number of survivors
   */
  public TruncationSelection(final int ak) {
    super();
    this.k = ((ak > 0) ? ak : Integer.MAX_VALUE);
  }

  /**
   * Perform the truncation selection as defined in
   * Section 28.4.2.
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
    int i, x, e;

    Arrays.sort(pop, popStart, popStart + popCount,
        Constants.FITNESS_SORT_ASC_COMPARATOR);

    i = mateStart;
    e = (i + mateCount);
    while (i < e) {
      x = Math.min(this.k, Math.min(mateCount - i, popCount));
      System.arraycopy(pop, popStart, mate, i, x);
      i += x;
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
    return "Trunc"; //$NON-NLS-1$
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
    if ((this.k > 0) && (this.k < Integer.MAX_VALUE)) {
      return "k=" + String.valueOf(this.k); //$NON-NLS-1$
    }
    return super.getConfiguration(longVersion);
  }
}