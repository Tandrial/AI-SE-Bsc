// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.es;

import org.goataa.impl.utils.Individual;

/**
 * An individual record for evolution strategies which also contains a
 * field for the strategy parameter. The search space is always the vectors
 * of real numbers.
 *
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class ESIndividual<X> extends Individual<double[], X> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the strategy parameter */
  public double[] w;

  /** The constructor creates a new Evolution Strategy individual record */
  public ESIndividual() {
    super();
  }

  /**
   * Copy the values of another individual record to this record. This
   * method saves us from excessively creating new individual records.
   *
   * @param to
   *          the individual to copy
   */
  @Override
  @SuppressWarnings("unchecked")
  public void assign(final Individual<double[], X> to) {
    super.assign(to);
    if (to instanceof ESIndividual) {
      this.w = ((ESIndividual<X>) to).w;
    }
  }
}