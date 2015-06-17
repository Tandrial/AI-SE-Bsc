// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.ternary;

import java.util.Random;

/**
 * The ternary binominal crossover operator for Differential Evolution as
 * defined in Section 33.2.
 *
 * @author Thomas Weise
 */
public final class DoubleArrayDEbin extends DEbase {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a new real-vector ternary recombination operation
   *
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   * @param acr
   *          the gene crossover rate cr
   * @param af
   *          the influence factor F
   */
  public DoubleArrayDEbin(final double mi, final double ma, double acr, double af) {
    super(mi, ma, acr, af);
  }

  /**
   * Create a new real-vector ternary recombination operation
   *
   * @param dim
   *          the dimension of the search space
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  public DoubleArrayDEbin(final int dim, final double mi, final double ma) {
    super(mi, ma);
  }

  /**
   * Perform the ternary Differential Evolution recombination method as
   * discussed in
   * Section 33.2 using a
   * normally distributed weight.
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
  @Override
  public double[] ternaryRecombine(final double[] p1, final double[] p2,
      final double[] p3, final Random r) {
    final double[] gnew;
    double weight, xn;
    boolean change;
    int i, j, maxTrials;
    final double f, cr;

    i = p1.length;
    gnew = new double[i];

    f = this.getF();
    cr = this.getCR();

    for (maxTrials = 10; maxTrials > 0; maxTrials--) {
      // choose a weight very close to F with a tiny variation
      weight = f * (1d + (0.01d * r.nextGaussian()));
      change = false;
      j = r.nextInt(gnew.length);

      // compute the new vector
      for (; (--i) >= 0;) {

        // if we should perform the crossover in this dimension
        if ((i == j) || (r.nextDouble() < cr)) {
          // compute the new allele and bound it to the search space
          xn = Math.min(this.max, Math.max(this.min, p3[i]
              + (weight * (p1[i] - p2[i]))));

          if (xn != p3[i]) {
            change = true;
          }
        } else {
          // if not, just copy the old value
          xn = p3[i];
        }

        gnew[i] = xn;
      }

      if (change) {
        return gnew;
      }
    }

    return p3;
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
    return "DA-1/rand/bin"; //$NON-NLS-1$
  }
}