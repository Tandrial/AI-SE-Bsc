// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings;

import org.goataa.impl.searchOperations.NullarySearchOperation;

/**
 * This is a base class for fixed-length string based creation search
 * operations. It has just been developed for convenience. Actually, it
 * only holds one variable to store the dimension of search space.
 * String-based search spaces with fixed dimensions could be, for example,
 * bit strings as used in Genetic Algorithms (see
 * Section 29.3) or real
 * vectors as often optimized by Evolution Strategies (see
 * Chapter 30).
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class FixedLengthStringCreation<G> extends
    NullarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the dimension of the search space */
  public final int n;

  /**
   * Create a new fixed-length string operation
   *
   * @param dim
   *          the dimension of the search space
   */
  public FixedLengthStringCreation(final int dim) {
    super();
    this.n = dim;
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
    if (longVersion) {
      return "n=" + String.valueOf(this.n); //$NON-NLS-1$
    }
    return String.valueOf(this.n);
  }
}