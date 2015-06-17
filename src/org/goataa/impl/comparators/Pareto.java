// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.comparators;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IIndividualComparator;

/**
 * Perform an individual comparison according to the Pareto dominance
 * relationship Section 3.3.5.
 *
 * @author Thomas Weise
 */
public class Pareto extends OptimizationModule implements
    IIndividualComparator {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the pareto comparator */
  public static final IIndividualComparator PARETO_COMPARATOR = new Pareto();

  /** instantitate a pareto comparator */
  protected Pareto() {
    super();
  }

  /**
   * Compare two individuals with each other according to the dominance
   * relationship defined in Section 3.3.5 based on
   * their objective values.
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
    int i, cr, res;

    x = a.f;
    y = b.f;
    res = 0;

    // check every objective value
    for (i = x.length; (--i) >= 0;) {

      // cr<0: x[i]<y[i]; cr>0: x[i]>y[i], cr=0: x[i]=y[i]
      cr = Double.compare(x[i], y[i]);

      if (cr > 0) {
        // x[i]>y[i]
        if (res < 0) {
          // we encountered at least one j:x[j]<y[j], hence result is
          // undecided
          return 0;
        }
        // assume that y dominates x (i.e., b dominates a)
        res = 1;
      } else {
        if (cr < 0) {
          // x[i]<y[i]
          if (res > 0) {
            // we encountered at least one j:x[j]>y[j], hence result is
            // undecided
            return 0;
          }
          // assume that x dominates y (i.e., a dominates b)
          res = -1;
        }
      }
    }

    return res;
  }
}