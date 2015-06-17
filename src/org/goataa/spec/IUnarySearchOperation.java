// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * This interface provides the method for a unary search operations. Search
 * operations are discussed in Section 4.2.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface IUnarySearchOperation<G> extends IOptimizationModule {

  /**
   * This is the unary search operation. It takes one existing genotype g
   * (see Definition D4.2) from the genome and produces one new
   * element in the search space. This new element is usually a copy of te
   * existing element g which is slightly modified in a random manner. For
   * this purpose, we pass a random number generator in as parameter so we
   * can use the same random number generator in all parts of an
   * optimization algorithm.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  public abstract G mutate(final G g, final Random r);

}