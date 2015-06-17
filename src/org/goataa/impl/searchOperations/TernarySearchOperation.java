// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.ITernarySearchOperation;

/**
 * A base class for ternary search operations
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class TernarySearchOperation<G> extends OptimizationModule
    implements ITernarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the useless identiy ternary operation */
  public static final ITernarySearchOperation<?> IDENTITY_TERNARY_RECOMBINATION = new TernarySearchOperation<Object>();

  /** instantiate the base class */
  protected TernarySearchOperation() {
    super();
  }

  /**
   * This is the ternary search operation. It takes three existing
   * genotypes p1, p2, and p3 (see Definition D4.2) from the
   * genome and produces one new element in the search space.
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
  public G ternaryRecombine(final G p1, final G p2, final G p3,
      final Random r) {
    switch (r.nextInt(3)) {
    case 0: {
      return p1;
    }
    case 1: {
      return p2;
    }
    default: {
      return p3;
    }
    }
  }
}