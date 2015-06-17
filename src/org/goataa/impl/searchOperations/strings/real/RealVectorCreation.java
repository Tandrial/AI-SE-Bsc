// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real;

import org.goataa.impl.searchOperations.strings.FixedLengthStringCreation;
import org.goataa.impl.utils.TextUtils;

/**
 * A base class for vector creation operations
 *
 * @author Thomas Weise
 */
public class RealVectorCreation extends
    FixedLengthStringCreation<double[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the minimum value an allel of a gene of the genotype can take on */
  public final double min;

  /** the maximum value an allel of a gene of the genotype can take on */
  public final double max;

  /**
   * Create a new real-vector operation
   *
   * @param dim
   *          the dimension of the search space
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  protected RealVectorCreation(final int dim, final double mi,
      final double ma) {
    super(dim);
    this.min = mi;
    this.max = ma;
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
    return getConfiguration(longVersion, this.min, this.max)
        + "^" + this.n; //$NON-NLS-1$
  }

  /**
   * Print a configuration
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @param mi
   *          the minimum
   * @param ma
   *          the maximum
   * @return the full configuration
   */
  static final String getConfiguration(final boolean longVersion,
      final double mi, final double ma) {
    return (longVersion ? "G=[" : "[")//$NON-NLS-1$//$NON-NLS-2$
        + TextUtils.formatNumber(mi) + ','
        + TextUtils.formatNumber(mi)
        + "]"; //$NON-NLS-1$
  }
}