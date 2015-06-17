// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms;

import org.goataa.impl.comparators.Pareto;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IIndividualComparator;
import org.goataa.spec.IObjectiveFunction;

/**
 * An algorithm for multi-objective optimization
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class MOOptimizationAlgorithm<G, X> extends
    SOOptimizationAlgorithm<G, X, MOIndividual<G, X>> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the individual comparator */
  private IIndividualComparator cmp;

  /** the objective functions */
  private IObjectiveFunction<X>[] ofs;

  /** Instantiate a new multi-objective optimization algorithm */
  protected MOOptimizationAlgorithm() {
    super();
    this.cmp = Pareto.PARETO_COMPARATOR;
    this.setMaxSolutions(20);
  }

  /**
   * Get the individual comparator
   *
   * @return the individual comparator
   */
  public IIndividualComparator getComparator() {
    return this.cmp;
  }

  /**
   * Set the individual comparator
   *
   * @param pcmp
   *          the individual comparator
   */
  public void setComparator(final IIndividualComparator pcmp) {
    this.cmp = pcmp;
  }

  /**
   * Get the objective functions
   *
   * @return the objective functions
   */
  public IObjectiveFunction<X>[] getObjectiveFunctions() {
    return this.ofs;
  }

  /**
   * Set the objective functions
   *
   * @param o
   *          the objective functions
   */
  public void setObjectiveFunctions(final IObjectiveFunction<X>[] o) {
    this.ofs = o;
    if (o.length > 0) {
      super.setObjectiveFunction(o[0]);
    }
  }

  /**
   * Set the objective function (see Section 2.2
   * and Listing 55.8) to be used to
   * guide the optimization process
   *
   * @param af
   *          the objective function
   */
  @Override
  @SuppressWarnings("unchecked")
  public void setObjectiveFunction(final IObjectiveFunction<X> af) {
    this.setObjectiveFunctions(new IObjectiveFunction[] { af });
  }
}