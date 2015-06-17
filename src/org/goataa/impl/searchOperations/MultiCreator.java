// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.spec.INullarySearchOperation;

/**
 * A simple class that allows to randomly choose between multiple different
 * creators (i.e., nullary search operations as defined in
 * Definition D4.6).
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class MultiCreator<G> extends NullarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internally available operations */
  private final INullarySearchOperation<G>[] o;

  /**
   * Create a new multimutator
   *
   * @param ops
   *          the search operations
   */
  public MultiCreator(final INullarySearchOperation<G>... ops) {
    super();

    this.o = ops;
  }

  /**
   * This operation tries to create a genotype by randomly applying one of
   * its sub-creators.
   *
   * @param r
   *          the random number generator
   * @return a new genotype (see Definition D4.2)
   */
  @Override
  public final G create(final Random r) {
    return this.o[r.nextInt(this.o.length)].create(r);
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
    return "MC"; //$NON-NLS-1$
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
    int i;

    sb = new StringBuilder();
    for (i = 0; i < this.o.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(this.o[i].toString(longVersion));
    }

    return sb.toString();
  }
}