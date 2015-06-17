// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * This interface provides the method for a nullary search operations.
 * Search operations are discussed in Section 4.2.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface INullarySearchOperation<G> extends IOptimizationModule {

  /**
   * This is the nullary search operation. It takes no argument from the
   * search space and produces one new element in the search. This new
   * element is usually created randomly. For this reason, we pass a random
   * number generator in as parameter so we can use the same random number
   * generator in all parts of an optimization algorithm.
   *
   * @param r
   *          the random number generator
   * @return a new genotype (see Definition D4.2)
   */
  public abstract G create(final Random r);

}