// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.eda;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.IModelBuilder;

/**
 * The base class for model builders
 *
 * @param <M>
 *          the model type
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @author Thomas Weise
 */
public class ModelBuilder<M, G> extends OptimizationModule implements
    IModelBuilder<M, G> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** Instantiate the UMDA model building operation */
  public ModelBuilder() {
    super();
  }

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
  public M buildModel(final Individual<G, ?>[] mate, final int start,
      final int count, final M oldModel, final Random r) {
    return null;
  }

}