// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.permutation.unary;

import java.util.Random;

import org.goataa.impl.searchOperations.UnarySearchOperation;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A unary search operation (see Section 4.2) for
 * permutations of n elements expressed as integer arrays of length n which
 * works according to Algorithm 29.4. This
 * operation tales an existing genotype (see Definition D4.2),
 * picks two different loci (Definition D4.5) uniformly distributed
 * in 0..n-1, and swaps the alleles (Definition D4.4) of the genes
 * (Definition D4.3) at these positions.
 *
 * @author Thomas Weise
 */
public final class IntPermutationSingleSwapMutation extends
    UnarySearchOperation<int[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared instance of the int permutation single swap
   * mutation
   */
  public static final IUnarySearchOperation<int[]> INT_PERMUTATION_SINGLE_SWAP_MUTATION = new IntPermutationSingleSwapMutation();

  /** Create a new permutation mutation operation */
  protected IntPermutationSingleSwapMutation() {
    super();
  }

  /**
   * This is an unary search operation for permutations as defined in
   * Algorithm 29.4. It takes one existing
   * genotype g (see Definition D4.2) from the search space and
   * produces one new genotype. This new element is a slightly modified
   * version of g which is obtained by swapping two elements in the
   * permutation.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final int[] mutate(final int[] g, final Random r) {
    final int[] gnew;
    int i, j, t;

    // copy g
    gnew = g.clone();

    // draw the first index i
    i = r.nextInt(g.length);

    // draw a second index j which is different from g
    do {
      j = r.nextInt(g.length);
    } while (i == j);

    t = gnew[i];
    gnew[i] = gnew[j];
    gnew[j] = t;

    return gnew;
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
      return super.getName(true);
    }
    return "IP-SS"; //$NON-NLS-1$
  }
}