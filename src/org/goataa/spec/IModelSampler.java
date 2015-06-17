// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * Here we give a basic interface for sampling models which is a basic
 * functionality of EDAs Section 34.3.
 *
 * @param <M>
 *          the model type
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public interface IModelSampler<M, G> extends IOptimizationModule {

  /**
   * Sample one point from the model as required in Estimation of
   * Distribution algorithms introduced in
   * Chapter 34.
   *
   * @param model
   *          the model
   * @param r
   *          a random number generator
   * @return the new model
   */
  public abstract G sampleModel(final M model, final Random r);

}