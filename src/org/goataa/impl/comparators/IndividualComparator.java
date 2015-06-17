// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.comparators;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IIndividualComparator;

/**
 * The individual comparator
 *
 * @author Thomas Weise
 */
public class IndividualComparator extends OptimizationModule implements
    IIndividualComparator {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantitate a pareto comparator */
  protected IndividualComparator() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public int compare(final MOIndividual<?, ?> a, final MOIndividual<?, ?> b) {
    return 0;
  }
}