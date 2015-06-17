// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.permutation.nullary;

import java.util.Random;

import org.goataa.impl.searchOperations.strings.FixedLengthStringCreation;

/**
 * A nullary search operation (see Section 4.2) for
 * permutatitons of n elements which works according to
 * Algorithm 29.2.
 *
 * @author Thomas Weise
 */
public final class IntPermutationUniformCreation extends
    FixedLengthStringCreation<int[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** a temporary variable */
  private transient int[] temp;

  /**
   * Instantiate the permutation creation operation
   *
   * @param dim
   *          the number of objects to permutate
   */
  public IntPermutationUniformCreation(final int dim) {
    super(dim);
  }

  /**
   * This operation creates a random permutation according to
   * Algorithm 29.2.
   *
   * @param r
   *          the random number generator
   * @return a new random permutation of the numbers 0..n-1
   */
  @Override
  public final int[] create(final Random r) {
    int[] g, tmp;
    int i, j;

    i = this.n;
    g = new int[i];

    // get the temporary variable
    tmp = this.temp;
    if (tmp == null) {
      this.temp = tmp = new int[i];
    }

    // initialize the temporary variable by setting the value of the ith
    // element to i
    for (; (--i) >= 0;) {
      tmp[i] = i;
    }

    i = this.n;
    // repeat for all genes in the genotype
    while (i > 0) {
      // select the next number for the permutation from the i available
      // ones randomly by chosing its index uniformly distributed in 0..i-1
      j = r.nextInt(i);

      // in each step, there is one number less
      i--;

      // put the selected number from index j to index i in the genotype
      g[i] = tmp[j];

      // delete the jth number from the temporary variable by replacing it
      // with the last number in the arry. Since we decreased i already,
      // that last number could be accessed in the next interation anyway
      tmp[j] = tmp[i];
    }

    return g;
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
    return "IP-U"; //$NON-NLS-1$
  }
}