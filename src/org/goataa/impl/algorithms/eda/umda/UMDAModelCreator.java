// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.eda.umda;

import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.INullarySearchOperation;

/**
 * The UMDA-style creation operation for models, as defined in
 * Algorithm 34.3, fills the initial probability vector with 0.5.
 *
 * @author Thomas Weise
 */
public class UMDAModelCreator extends OptimizationModule implements
    INullarySearchOperation<double[]> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the dimension */
  private final int dimension;

  /**
   * Instantiate the UMDA model creation operation
   *
   * @param dim
   *          the dimension of the search space
   */
  public UMDAModelCreator(final int dim) {
    super();
    this.dimension = dim;
  }

  /**
   * Create a vector of all 0.5 values, as stated in Algorithm 34.3
   *
   * @param r
   *          the random number generator
   * @return the vector of 0.5 values
   */
  public final double[] create(final Random r) {
    double[] d;

    d = new double[this.dimension];
    Arrays.fill(d, 0.5d);

    return d;
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  @Override
  public String getConfiguration(final boolean longVersion) {
    return ("dim=" + this.dimension); //$NON-NLS-1$
  }
}