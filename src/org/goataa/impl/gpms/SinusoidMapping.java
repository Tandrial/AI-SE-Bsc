// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.gpms;

import java.util.Random;

/**
 * A sinusoid mapping translates a one dimensional genotype into a many
 * dimensional phenotype.
 *
 * @author Thomas Weise
 */
public final class SinusoidMapping extends GPM<double[], double[]> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the dimension */
  private final int dim;

  /**
   * Instantiate
   *
   * @param d
   *          the dimension
   */
  public SinusoidMapping(final int d) {
    super();
    this.dim = d;
  }

  /**
   * Translate
   *
   * @param g
   *          the genotype (Definition D4.2)
   * @param r
   *          the randomizer
   * @return the phenotype (see Definition D2.2)
   *         corresponding to the genotype g
   */
  @Override
  public final double[] gpm(final double[] g, final Random r) {
    final double[] s;
    int i;
    final double d;

    i = this.dim;
    s = new double[i];
    d = g[0];
    for (; (--i) >= 0;) {
      s[i] = Math.sin((Math.PI + i) * d);
    }

    return s;
  }

}