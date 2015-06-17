// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * A ternary search operation (see Definition D4.6) such
 * as the recombination operation in Differential Evolution (see
 * Section 33.2).
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface ITernarySearchOperation<G> extends IOptimizationModule {

  /**
   * This is the ternary search operation. It takes three existing
   * genotypes p1, p2, and p3 (see Definition D4.2) from the
   * genome and produces one new element in the search space.
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param p3
   *          the third parent genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public abstract G ternaryRecombine(final G p1, final G p2, final G p3,
      final Random r);

}