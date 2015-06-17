// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.nullary;

import java.util.Random;

import org.goataa.impl.searchOperations.strings.real.RealVectorCreation;

/**
 * A nullary search operation (see Section 4.2) for
 * real vectors in a bounded subspace of the real vectors of dimension n.
 * This operation creates vectors which are uniformly distributed in the
 * interval [min,max]^n.
 *
 * @author Thomas Weise
 */
public class DoubleArrayUniformCreation extends RealVectorCreation {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Instantiate the real-vector creation operation
   *
   * @param dim
   *          the dimension of the search space
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  public DoubleArrayUniformCreation(final int dim, final double mi,
      final double ma) {
    super(dim, mi, ma);
  }

  /**
   * This operation just produces uniformly distributed random vectors in
   * the interval [this.min, this.max]^this.n
   *
   * @param r
   *          the random number generator
   * @return a new random real vector of dimension n
   */
  @Override
  public final double[] create(final Random r) {
    double[] g;
    int i;

    // create a new real vector (genotype, Definition D4.2) of
    // dimension n
    g = new double[this.n];

    // initialize each gene (Definition D4.3) of the vector...
    for (i = this.n; (--i) >= 0;) {
      // ...by setting it to a random number uniformly distributed in
      // [min,max] (i is the locus Definition D4.5)
      g[i] = (this.min + (r.nextDouble() * (this.max - this.min)));
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
    return "DA-U"; //$NON-NLS-1$
  }
}