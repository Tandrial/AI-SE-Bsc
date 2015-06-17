// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.eda;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IModelSampler;

/**
 * A base class for model samplers
 *
 * @param <M>
 *          the model type
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public class ModelSampler<M, G> extends OptimizationModule implements
    IModelSampler<M, G> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** Instantiate the UMDA model sampling operation */
  public ModelSampler() {
    super();
  }

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
  public G sampleModel(final M model, final Random r) {
    return null;
  }
}