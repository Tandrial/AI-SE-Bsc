// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.unary;

import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.searchOperations.strings.real.RealVectorMutation;

/**
 * This is a mutation operation that performes mutation using normally
 * distributed random numbers according to a given set of standard
 * deviation parameters, as suggested in
 * Section 30.4.1.2. This operator is quite similar,
 * but more sophisticated than the simple mutation method defined in
 * Listing 56.28
 *
 * @author Thomas Weise
 */
public final class DoubleArrayStdDevNormalMutation extends
    RealVectorMutation {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the standard deviation to use */
  private final double[] stddev;

  /**
   * Create a new real-vector mutation operation
   *
   * @param dim
   *          the dimension of the search space
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  public DoubleArrayStdDevNormalMutation(final int dim, final double mi,
      final double ma) {
    super(mi, ma);
    this.stddev = new double[dim];
    Arrays.fill(this.stddev, 1d);
  }

  /**
   * Perform a mutation operation that performes mutation using normally
   * distributed random numbers according to a given set of standard
   * deviation parameters, as suggested in
   * Section 30.4.1.2.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final double[] mutate(final double[] g, final Random r) {
    final double[] gnew, stddevs;
    double d;
    int i;

    i = g.length;

    // create a new real vector of dimension n
    gnew = new double[i];
    stddevs = this.stddev;

    // set each gene Definition D4.3 of gnew to ...
    for (; (--i) >= 0;) {
      do {
        // Use a normally distributed random number with a standard
        // deviation as specified in the array stddev.
        d = (g[i] + (r.nextGaussian() * stddevs[i]));
      } while ((d < this.min) || (d > this.max));
      gnew[i] = d;
    }

    return gnew;
  }

  /**
   * Set the standard deviations
   *
   * @param s
   *          the standard deviations
   */
  public final void setStdDevs(final double[] s) {
    System.arraycopy(s, 0, this.stddev, 0, this.stddev.length);
  }

  /**
   * Set the standard deviations to a single, fixed value
   *
   * @param s
   *          the standard deviation
   */
  public final void setStdDev(final double s) {
    Arrays.fill(this.stddev, s);
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
    return "DA-SDNM"; //$NON-NLS-1$
  }
}