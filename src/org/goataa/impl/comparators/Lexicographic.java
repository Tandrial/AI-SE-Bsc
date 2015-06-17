// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.comparators;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IIndividualComparator;

/**
 * Perform an individual comparison according to the lexicographic
 * relationship Section 3.3.2.
 *
 * @author Thomas Weise
 */
public class Lexicographic extends OptimizationModule implements
    IIndividualComparator {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the lexicographic comparator */
  public static final IIndividualComparator LEXICOGRAPHIC_COMPARATOR = new Lexicographic();

  /** instantitate a lexicographicOptimization comparator */
  protected Lexicographic() {
    super();
  }

  /**
   * Compare two individuals with each other according to the lexicographic
   * relationship defined in Section 3.3.2
   * based on their objective values.
   *
   * @param a
   *          the first individual
   * @param b
   *          the second individual
   * @return -1 if the a dominates b, 1 if b dominates a, 0 if neither of
   *         them is better
   */
  public int compare(final MOIndividual<?, ?> a, final MOIndividual<?, ?> b) {
    double[] x, y;
    int i, cr;

    x = a.f;
    y = b.f;
    for (i = 0; i < x.length; i++) {
      cr = Double.compare(x[i], y[i]);
      if (cr != 0) {
        return cr;
      }
    }

    return 0;
  }
}