// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.unary;

import java.util.Random;

import org.goataa.impl.searchOperations.strings.real.RealVectorMutation;

/**
 * A unary search operation (see Section 4.2) for
 * real vectors in a bounded subspace of the real vectors of dimension n.
 * This operation tales an existing genotype (see
 * Definition D4.2) and adds a small normally distributed random
 * number to each of its genes (see Definition D4.3). There are two
 * main differences between normally distributed random numbers and
 * uniformly distributed ones: 1) The normal distribution (see
 * Section 53.4.2) is unbounded whereas the uniform
 * distribution has limits to each side (see
 * Section 53.4.1 and
 * Section 53.3.1) 2) The normal distribution gives
 * the elements around its expected value
 * Section 53.2.2 a higher probability and a lower
 * probability to elements distant from it, whereas all possible samples
 * have the same probability in a uniform distribution.
 *
 * @author Thomas Weise
 */
public final class DoubleArrayAllNormalMutation extends RealVectorMutation {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a new real-vector mutation operation
   *
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  public DoubleArrayAllNormalMutation(final double mi, final double ma) {
    super(mi, ma);
  }

  /**
   * This is an unary search operation for vectors of real numbers. It
   * takes one existing genotype g (see Definition D4.2) from the
   * search space and produces one new genotype. This new element is a
   * slightly modified version of g which is obtained by adding normally
   * distributed random numbers to its elements.
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
    final double strength;
    int i;

    i = g.length;

    // create a new real vector of dimension n
    gnew = new double[i];

    // the mutation strength: here we use a constant which is small
    // compared to the range min...max
    strength = 0.001d * (this.max - this.min);

    // set each gene Definition D4.3 of gnew to ...
    for (; (--i) >= 0;) {
      do {
        // the original allele (Definition D4.4) of the gene plus a
        // random number normally distributed
        // Section 53.4.2 with N(0, strength^2)
        gnew[i] = g[i] + (r.nextGaussian() * strength);
        // and repeat this until the new allele falls into the specified
        // boundaries
      } while ((gnew[i] < this.min) || (gnew[i] > this.max));
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
    return "DA-NAM"; //$NON-NLS-1$
  }
}