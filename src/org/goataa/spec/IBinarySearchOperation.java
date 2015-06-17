// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * A binary search operation (see Definition D4.6) such
 * as the recombination operation in Evolutionary Algorithms (see
 * Definition D28.14) and its specific string-based version
 * used in Genetic Algorithms (see
 * Section 29.3.4).
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface IBinarySearchOperation<G> extends IOptimizationModule {

  /**
   * This is the binary search operation. It takes two existing genotypes
   * p1 and p2 (see Definition D4.2) from the genome and produces
   * one new element in the search space. There are two basic assumptions
   * about this operator: 1) Its input elements are good because they have
   * previously been selected. 2) It is somehow possible to combine these
   * good traits and hence, to obtain a single individual which unites them
   * and thus, has even better overall qualities than its parents. The
   * original underlying idea of this operation is the
   * "Building Block Hypothesis" (see
   * Section 29.5.5) for which, so far, not much
   * evidence has been found. The hypothesis
   * "Genetic Repair and Extraction" (see Section 29.5.6)
   * has been developed as an alternative to explain the positive aspects
   * of binary search operations such as recombination.
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public abstract G recombine(final G p1, final G p2, final Random r);

}