// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * This interface provides the method for an n-ary search operation. Search
 * operations are discussed in Section 4.2.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface INarySearchOperation<G> extends IOptimizationModule {

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
  public abstract G combine(final G[] gs, final Random r);

}