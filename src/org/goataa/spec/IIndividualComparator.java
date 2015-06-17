// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Comparator;

import org.goataa.impl.utils.MOIndividual;

/**
 * A comparator interface for multi-objective individuals. During the
 * fitness assignment processes in multi-objective optimization, we can use
 * such a comparator to check which individuals are better and which are
 * worst. Based on the comparison results, the fitness values can be
 * computed which are then used during a subsequent selection process. This
 * interface provides the functionality specified in
 * Definition D3.18 in
 * Section 3.5.2.
 *
 * @author Thomas Weise
 */
public interface IIndividualComparator extends
    Comparator<MOIndividual<?, ?>>, IOptimizationModule {

  /**
   * Compare two individuals with each other usually by using their
   * objective values as defined in Definition D3.18. .
   * Warning: The fitness values (a.v, b.v) must not be used here since
   * they are usually computed by using the comparators during the fitness
   * assignment process.
   *
   * @param a
   *          the first individual
   * @param b
   *          the second individual
   * @return -1 if the a is better than b, 1 if b is better than a, 0 if
   *         neither of them is better
   */
  public abstract int compare(final MOIndividual<?, ?> a,
      final MOIndividual<?, ?> b);
}