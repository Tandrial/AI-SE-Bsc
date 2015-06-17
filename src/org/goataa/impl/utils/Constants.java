// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.utils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class holds some common constants
 *
 * @author Thomas Weise
 */
public final class Constants {

  /*
   * based on Section 6.3.4, we define the following
   * constants
   */

  /** the worst possible objective value */
  public static final double WORST_OBJECTIVE = Double.POSITIVE_INFINITY;

  /** the best possible objective value */
  public static final double BEST_OBJECTIVE = Double.NEGATIVE_INFINITY;

  /** the worst possible fitness value */
  public static final double WORST_FITNESS = Double.POSITIVE_INFINITY;

  /** the best possible fitness value */
  public static final double BEST_FITNESS = 0d;

  /**
   * a comparator that can be used for sorting individuals according to
   * their fitness in ascending order, from the best to the worst.
   */
  public static final Comparator<Individual<?, ?>> FITNESS_SORT_ASC_COMPARATOR = new FitnessSortASCComparator();

  /**
   * An internal utility class for sorting individuals according to their
   * fitness in ascending order.
   *
   * @author Thomas Weise
   */
  private static final class FitnessSortASCComparator implements
      Serializable, Comparator<Individual<?, ?>> {
    /** a constant required by Java serialization */
    private static final long serialVersionUID = 1;

    /** instantiate */
    FitnessSortASCComparator() {
      super();
    }

    /**
     * Compare two individual records
     *
     * @param a
     *          the first individual record
     * @param b
     *          the second individual record
     * @return -1 if a.v<b.v, 0 if a.v=b.v, 1 if a.v>b.v
     */
    public final int compare(final Individual<?, ?> a,
        final Individual<?, ?> b) {
      return Double.compare(a.v, b.v);
    }
  }
}