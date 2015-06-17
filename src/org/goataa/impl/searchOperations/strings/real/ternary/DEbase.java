// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.real.ternary;

import org.goataa.impl.searchOperations.strings.real.RealVectorTernaryOperation;
import org.goataa.impl.utils.TextUtils;

/**
 * The base class for ternary crossover operators for Differential
 * Evolution as defined in
 * Section 33.2.
 *
 * @author Thomas Weise
 */
abstract class DEbase extends RealVectorTernaryOperation {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the cr value */
  private double cr;

  /** the F value */
  private double F;

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
  DEbase(final double mi, final double ma, double acr, double af) {
    super(mi, ma);
    this.setCR(acr);
    this.setF(af);
  }

  /**
   * Create a new real-vector ternary recombination operation
   *
   * @param mi
   *          the minimum value of the allele (Definition D4.4) of
   *          a gene
   * @param ma
   *          the maximum value of the allele of a gene
   */
  DEbase(final double mi, final double ma) {
    this(mi, ma, 0.3d, 0.7d);
  }

  /**
   * Get the gene crossover rate cr
   *
   * @return the gene crossover rate cr
   */
  public final double getCR() {
    return this.cr;
  }

  /**
   * Set the gene crossover rate cr
   *
   * @param acr
   *          the gene crossover rate cr
   */
  public final void setCR(final double acr) {
    this.cr = Math.max(0d, Math.min(1d, acr));
  }

  /**
   * Get the influence factor F
   *
   * @return the influence factor F
   */
  public final double getF() {
    return this.F;
  }

  /**
   * Set the influence factor F
   *
   * @param af
   *          the influence factor F
   */
  public final void setF(final double af) {
    this.F = Math.max(0d, Math.min(1d, af));
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
    StringBuilder sb;

    sb = new StringBuilder(64);

    sb.append(super.getConfiguration(longVersion));
    if (sb.length() > 0) {
      sb.append(',');
    }
    sb.append("cr=");//$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.getCR()));
    sb.append(",F="); //$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.getF()));
    return sb.toString();
  }
}