// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A base class for unary search operations
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class UnarySearchOperation<G> extends OptimizationModule implements
    IUnarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the useless identiy mutation */
  public static final IUnarySearchOperation<?> IDENTITY_MUTATION = new UnarySearchOperation<Object>();

  /** instantiate the base class */
  protected UnarySearchOperation() {
    super();
  }

  /**
   * This operation tries to mutate a genotype by randomly applying one of
   * its sub-mutators. If a sub-mutator is not able to provide a new
   * genotype, i.e., returns null or its input, then we try the next
   * sub-mutator, and so on.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public G mutate(final G g, final Random r) {
    return g;
  }
}