// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.nary;

import java.util.Random;

import org.goataa.impl.searchOperations.NarySearchOperation;
import org.goataa.spec.INarySearchOperation;

/**
 * The dominant recombination operator as defined in
 * Algorithm 30.2.
 *
 * @author Thomas Weise
 */
public final class DoubleArrayDominantRecombination extends
    NarySearchOperation<double[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared instance of the double array dominant
   * recombination operation
   */
  public static final INarySearchOperation<double[]> DOUBLE_ARRAY_DOMINANT_RECOMBINATION = new DoubleArrayDominantRecombination();

  /** Create a new real-vector n-ary search operation */
  protected DoubleArrayDominantRecombination() {
    super();
  }

  /**
   * Perform the dominant recombination operator as defined in
   * Algorithm 30.2.
   *
   * @param gs
   *          the existing genotypes in the search space which will be
   *          combined to a new one
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final double[] combine(final double[][] gs, final Random r) {
    final double[] gnew;
    final int dim;
    int i;

    if ((gs == null) || (gs.length <= 0)) {
      return null;
    }

    dim = gs[0].length;
    gnew = new double[dim];

    for (i = dim; (--i) >= 0;) {
      gnew[i] = gs[r.nextInt(gs.length)][i];
    }

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
    return "DA-domX"; //$NON-NLS-1$
  }
}