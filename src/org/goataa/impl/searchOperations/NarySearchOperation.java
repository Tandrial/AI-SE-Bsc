// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.INarySearchOperation;

/**
 * A base class for n-ary search operations
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class NarySearchOperation<G> extends OptimizationModule implements
    INarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantiate the base class */
  protected NarySearchOperation() {
    super();
  }

  /**
   * This is an n-ary search operation. It takes n existing genotype gi
   * (see Definition D4.2) from the genome and produces one new
   * element in the search space.
   *
   * @param gs
   *          the existing genotypes in the search space which will be
   *          combined to a new one
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public G combine(final G[] gs, final Random r) {
    if (gs.length <= 0) {
      return null;
    }
    return gs[r.nextInt(gs.length)];
  }
}