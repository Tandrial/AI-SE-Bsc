// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.nary;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.INarySearchOperation;

/**
 * A average n-ary search operator which computes the average of n
 * elements, i.e., performs intermediate recombination as defined in
 * Algorithm 30.3.
 *
 * @author Thomas Weise
 */
public final class IntArrayIntermediateRecombination extends
    OptimizationModule implements INarySearchOperation<int[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared instance of the int array intermediate
   * recombination operation
   */
  public static final INarySearchOperation<int[]> INTEGER_ARRAY_INTERMEDIATE_RECOMBINATION = new IntArrayDominantRecombination();

  /** Create a new real-vector n-ary search operation */
  protected IntArrayIntermediateRecombination() {
    super();
  }

  /**
   * Compute theaverage the average of n genotypes, i.e., perform
   * intermediate recombination as defined in
   * Algorithm 30.3.
   *
   * @param gs
   *          the existing genotypes in the search space which will be
   *          combined to a new one
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public final int[] combine(final int[][] gs, final Random r) {
    final int[] gnew;
    int[] g;
    final int dim;
    final double gamma;
    int i, j;

    if ((gs == null) || (gs.length <= 0)) {
      return null;
    }

    dim = gs[0].length;
    gnew = new int[dim];
    gamma = (1d / gs.length);

    for (j = gs.length; (--j) >= 0;) {
      g = gs[j];
      for (i = dim; (--i) >= 0;) {
        gnew[i] += (g[i] * gamma);
      }
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
    return "DA-IX"; //$NON-NLS-1$
  }
}