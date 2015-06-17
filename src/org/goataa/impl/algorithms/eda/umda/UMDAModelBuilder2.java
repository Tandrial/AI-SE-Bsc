// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.eda.umda;

import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.algorithms.eda.ModelBuilder;
import org.goataa.impl.utils.Individual;

/**
 * The build a model UMDA-style, as defined in
 * Algorithm 34.5, but ensure that the probabilities
 * never fully converge
 *
 * @author Thomas Weise
 */
public class UMDAModelBuilder2 extends ModelBuilder<double[], boolean[]> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the temporary counter */
  private int[] tmp;

  /** Instantiate the UMDA model building operation */
  public UMDAModelBuilder2() {
    super();
  }

  /**
   * Build a model from a set of selected points in the search space and an
   * old model as introduced in Algorithm 34.5: Count the
   * number of bits which are false at each locus and use their frequency
   * as probability value in the new model. However, here we ensure that
   * the probabilities never fully converge.
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
  public double[] buildModel(final Individual<boolean[], ?>[] mate,
      final int start, final int count, final boolean[] oldModel,
      final Random r) {
    double[] m;
    int[] cc;
    boolean[] b;
    int i, j;
    double d;

    m = new double[oldModel.length];

    cc = this.tmp;
    if ((cc == null) || (m.length > cc.length)) {
      this.tmp = cc = new int[m.length];
    } else {
      Arrays.fill(cc, 0, m.length, 0);
    }

    for (i = (start + count); (--i) >= start;) {
      b = mate[i].g;
      for (j = m.length; (--j) >= 0;) {
        if (!(b[j])) {
          cc[j]++;
        }
      }
    }

    d = Math.pow(0.9d, 1d / m.length);
    for (i = m.length; (--i) >= 0;) {
      m[i] = Math.max(d, Math
          .min(1d - d, ((((double) (cc[i])) / (count)))));
    }

    return m;
  }
}