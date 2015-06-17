// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.ea.selection;

import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.ISelectionAlgorithm;

/**
 * A base class for selection algorithms
 *
 * @author Thomas Weise
 */
public class SelectionAlgorithm extends OptimizationModule implements
    ISelectionAlgorithm {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** create the base class */
  protected SelectionAlgorithm() {
    super();
  }

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
  public void select(final Individual<?, ?>[] pop, final int popStart,
      final int popCount, final Individual<?, ?>[] mate,
      final int mateStart, final int mateCount, final Random r) {
    Arrays.fill(mate, mateStart, mateStart + mateCount, null);
  }
}