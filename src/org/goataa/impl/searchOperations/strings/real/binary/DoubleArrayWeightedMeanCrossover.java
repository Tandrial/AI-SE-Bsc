// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.binary;

import java.util.Random;

import org.goataa.impl.searchOperations.BinarySearchOperation;
import org.goataa.spec.IBinarySearchOperation;

/**
 * A weighted average crossover operator as defined in
 * Section 29.3.4.5.
 *
 * @author Thomas Weise
 */
public final class DoubleArrayWeightedMeanCrossover extends
    BinarySearchOperation<double[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared instance of the double array weighted mean
   * crossover
   */
  public static final IBinarySearchOperation<double[]> DOUBLE_ARRAY_WEIGHTED_MEAN_CROSSOVER = new DoubleArrayWeightedMeanCrossover();

  /** Create a new real-vector crossover operation */
  protected DoubleArrayWeightedMeanCrossover() {
    super();
  }

  /**
   * Perform a weighted average crossover as discussed in
   * Section 29.3.4.5.
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final double[] recombine(final double[] p1, final double[] p2,
      final Random r) {
    final double[] gnew;
    double gamma;
    int i;

    i = p1.length;
    gnew = new double[i];

    for (; (--i) >= 0;) {
      gamma = r.nextDouble();
      gamma = (gamma * gamma * gamma);
      if (r.nextBoolean()) {
        gamma = 0.5d + (0.5d * gamma);
      } else {
        gamma = 0.5d - (0.5d * gamma);
      }
      gnew[i] = ((p1[i] * gamma) + (p2[i] * (1d - gamma)));
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
    return "DA-WMC"; //$NON-NLS-1$
  }
}