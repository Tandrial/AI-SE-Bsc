// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms;

import org.goataa.impl.searchOperations.NullarySearchOperation;
import org.goataa.impl.searchOperations.UnarySearchOperation;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A simple local search algorithm which has a nullary and a unary search
 * operation.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @param <IT>
 *          the individual type
 * @author Thomas Weise
 */
public class LocalSearchAlgorithm<G, X, IT extends Individual<G, X>>
    extends SOOptimizationAlgorithm<G, X, IT> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the nullary search operation */
  private INullarySearchOperation<G> o0;

  /** the unary search operation */
  private IUnarySearchOperation<G> o1;

  /** Create a new optimization algorithm */
  @SuppressWarnings("unchecked")
  protected LocalSearchAlgorithm() {
    super();
    this.o0 = (INullarySearchOperation) (NullarySearchOperation.NULL_CREATION);
    this.o1 = (IUnarySearchOperation) (UnarySearchOperation.IDENTITY_MUTATION);
  }

  /**
   * Set the nullary search operation to be used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.2).
   *
   * @param op
   *          the nullary search operation to use
   */
  @SuppressWarnings("unchecked")
  public void setNullarySearchOperation(final INullarySearchOperation<G> op) {
    this.o0 = ((op != null) ? op
        : ((INullarySearchOperation) (NullarySearchOperation.NULL_CREATION)));
  }

  /**
   * Get the nullary search operation which is used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.2).
   *
   * @return the nullary search operation to use
   */
  public final INullarySearchOperation<G> getNullarySearchOperation() {
    return this.o0;
  }

  /**
   * Set the unary search operation to be used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.3).
   *
   * @param op
   *          the unary search operation to use
   */
  @SuppressWarnings("unchecked")
  public void setUnarySearchOperation(final IUnarySearchOperation<G> op) {
    this.o1 = ((op != null) ? op
        : (IUnarySearchOperation) (UnarySearchOperation.IDENTITY_MUTATION));
  }

  /**
   * Get the unary search operation which is used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.3).
   *
   * @return the unary search operation to use
   */
  public final IUnarySearchOperation<G> getUnarySearchOperation() {
    return this.o1;
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
    IOptimizationModule o;

    sb = new StringBuilder();

    sb.append(super.getConfiguration(longVersion));

    o = this.getNullarySearchOperation();
    if (o != null) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append("o0=");//$NON-NLS-1$
      sb.append(o.toString(longVersion));
    }

    o = this.getUnarySearchOperation();
    if (o != null) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append("o1=");//$NON-NLS-1$
      sb.append(o.toString(longVersion));
    }

    return sb.toString();
  }
}