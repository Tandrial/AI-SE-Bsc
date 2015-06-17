// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.INullarySearchOperation;

/**
 * The base class for nullary search operations
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class NullarySearchOperation<G> extends OptimizationModule
    implements INullarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the null creation */
  public static final INullarySearchOperation<?> NULL_CREATION = new NullarySearchOperation<Object>();

  /** instantiate the base class */
  protected NullarySearchOperation() {
    super();
  }

  /**
   * This operation tries to create a genotype by randomly applying one of
   * its sub-creators.
   *
   * @param r
   *          the random number generator
   * @return a new genotype (see Definition D4.2)
   */
  public G create(final Random r) {
    return null;
  }
}