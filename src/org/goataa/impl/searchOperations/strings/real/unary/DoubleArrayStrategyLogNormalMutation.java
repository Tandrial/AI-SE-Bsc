// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.unary;

import java.util.Random;

import org.goataa.impl.searchOperations.strings.real.RealVectorMutation;

/**
 * This operator realizes the mutation strength strategy vector mutation
 * for Evolution Strategies (Chapter 30) as
 * specified in Algorithm 30.8,
 * Equation 30.12, and Equation 30.13.
 *
 * @author Thomas Weise
 */
public final class DoubleArrayStrategyLogNormalMutation extends
    RealVectorMutation {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a new real-vector mutation operation
   *
   * @param ma
   *          the maximum value of the allele of a gene
   */
  public DoubleArrayStrategyLogNormalMutation(final double ma) {
    super(Double.MIN_VALUE, ma);
  }

  /**
   * Perform the mutation strength strategy vector mutation for Evolution
   * Strategies (Chapter 30) as specified in
   * Algorithm 30.8.
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
    final double[] gnew;
    final double t0, t, c, nu;
    int i;
    double x;

    i = g.length;

    c = 1d;

    // set tau0 according to Equation 30.12
    t0 = (c / Math.sqrt(2 * i));

    // set tau according to Equation 30.13
    t = (c / Math.sqrt(2 * Math.sqrt(i)));

    nu = Math.exp(t0 * r.nextGaussian());
    gnew = g.clone();

    // set each gene Definition D4.3 of gnew to ...
    for (; (--i) >= 0;) {
      do {
        x = gnew[i] * nu * Math.exp(t * r.nextGaussian());
      } while ((x < this.min) || (x > this.max));
      gnew[i] = x;
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
    return "DA-SLN"; //$NON-NLS-1$
  }
}