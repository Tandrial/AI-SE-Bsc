// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

import org.goataa.impl.utils.Individual;

/**
 * Here we give a basic interface to the model building algorithms used in
 * EDAs as introduced in Section 34.3.
 *
 * @param <M>
 *          the model type
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface IModelBuilder<M, G> extends IOptimizationModule {

  /**
   * Build a model from a set of selected points in the search space and an
   * old model as introduced in Section 34.3.
   *
   * @param mate
   *          the mating pool of selected individuals
   * @param start
   *          the index of the first individual in the mating pool
   * @param count
   *          the number of individuals in the mating pool
   * @param oldModel
   *          the old model
   * @param r
   *          a random number generator
   * @return the new model
   */
  public abstract M buildModel(final Individual<G, ?>[] mate,
      final int start, final int count, final M oldModel, final Random r);

}