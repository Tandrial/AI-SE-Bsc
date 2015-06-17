// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real;

import org.goataa.impl.searchOperations.UnarySearchOperation;

/**
 * A base class for vector mutation operations
 *
 * @author Thomas Weise
 */
public class RealVectorMutation extends UnarySearchOperation<double[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the minimum value an allel of a gene of the genotype can take on */
  public final double min;

  /** the maximum value an allel of a gene of the genotype can take on */
  public final double max;

  /**
   * Create a new real-vector operation
   *
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  protected RealVectorMutation(final double mi, final double ma) {
    super();
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
    return RealVectorCreation.getConfiguration(longVersion, this.min,
        this.max);
  }
}