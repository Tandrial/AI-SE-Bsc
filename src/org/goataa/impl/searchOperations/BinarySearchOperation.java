// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IBinarySearchOperation;

/**
 *A base class for binary search operations
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class BinarySearchOperation<G> extends OptimizationModule implements
    IBinarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the useless identiy crossover */
  public static final IBinarySearchOperation<?> IDENTITY_CROSSOVER = new BinarySearchOperation<Object>();

  /** instantiate the base class */
  protected BinarySearchOperation() {
    super();
  }

  /**
   * Choose a recombination operation randomly from the available
   * operators.
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public G recombine(final G p1, final G p2, final Random r) {
    return (r.nextBoolean() ? p1 : p2);
  }
}