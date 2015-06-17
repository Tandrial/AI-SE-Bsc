// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.eda.umda;

import java.util.Random;

import org.goataa.impl.algorithms.eda.ModelSampler;

/**
 * The sample a model (a probability vector) UMDA-style accoring to
 * Algorithm 34.4
 *
 * @author Thomas Weise
 */
public class UMDAModelSampler extends ModelSampler<double[], boolean[]> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** Instantiate the UMDA model sampling operation */
  public UMDAModelSampler() {
    super();
  }

  /**
   * According to Algorithm 34.4, every value of the
   * model is between 0 and 1 and denotes the probability that the bit at
   * the same position in the genotype will be false. The model is sampled
   * using uniform distributions.
   *
   * @param model
   *          the model
   * @param r
   *          a random number generator
   * @return the new model
   */
  @Override
  public final boolean[] sampleModel(final double[] model, final Random r) {
    boolean[] b;
    int i;

    i = model.length;
    b = new boolean[i];

    for (; (--i) >= 0;) {
      b[i] = (r.nextDouble() > model[i]);
    }

    return b;
  }
}