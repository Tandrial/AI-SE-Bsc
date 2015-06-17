// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.gpms;

import java.util.Arrays;
import java.util.Random;

/**
 * With this class, we implement the Random Keys encoding
 * genotype-phenotype mapping as introduced in
 * Section 29.7. This mapping which is specified in
 * Algorithm 29.8 translates an n-dimensional vector of
 * real numbers (i.e., doubles) to a permutation of the first n natural
 * numbers (i.e., an array of int containing the values 0, 1, 2, ..., n-1).
 * This mapping can only
 *
 * @author Thomas Weise
 */
public final class RandomKeys extends GPM<double[], int[]> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** a temporary double array */
  private transient double[] temp;

  /** Instantiate */
  public RandomKeys() {
    super();
  }

  /**
   * Perform Algorithm 29.8, i.e., translate a vector of n
   * real numbers into a permutation of the first n natural numbers.
   *
   * @param g
   *          the genotype (Definition D4.2)
   * @param r
   *          the randomizer
   * @return the phenotype (see Definition D2.2)
   *         corresponding to the genotype g
   */
  @Override
  public final int[] gpm(final double[] g, final Random r) {
    int[] x;
    double[] s;
    final int l;
    int i, j;

    l = g.length;
    x = new int[g.length];

    // We cannot sort g directly, since this would destroy it. Hence, we
    // use an internal temporary array.
    s = this.temp;
    if ((s == null) || (s.length < l)) {
      this.temp = s = new double[l];
    }

    // in each step, put one element from g into the right position in s
    // and update the phenotype accordingly
    for (i = 0; i < l; i++) {
      j = Arrays.binarySearch(s, 0, i, g[i]);
      if (j < 0) {
        j = ((-j) - 1);
      }
      System.arraycopy(s, j, s, j + 1, i - j);
      s[j] = g[i];
      System.arraycopy(x, j, x, j + 1, i - j);
      x[j] = i;
    }

    return x;
  }

}