// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.de;

import java.util.List;
import java.util.Random;

import org.goataa.impl.algorithms.SOOptimizationAlgorithm;
import org.goataa.impl.searchOperations.NullarySearchOperation;
import org.goataa.impl.utils.Constants;
import org.goataa.impl.utils.Individual;
import org.goataa.impl.utils.TextUtils;
import org.goataa.spec.IGPM;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.ITernarySearchOperation;

/**
 * The base class for Differential Evolution-based algorithms as discussed
 * in Chapter 33.
 *
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class DifferentialEvolution1<X> extends
    SOOptimizationAlgorithm<double[], X, Individual<double[], X>> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the nullary search operation */
  private INullarySearchOperation<double[]> o0;

  /** the population size */
  private int ps;

  /** the ternary operator for crossover */
  private ITernarySearchOperation<double[]> tc;

  /** instantiate the Differential Evolution algorithm */
  @SuppressWarnings("unchecked")
  public DifferentialEvolution1() {
    super();
    this.o0 = (INullarySearchOperation) (NullarySearchOperation.NULL_CREATION);
    this.ps = 100;
  }

  /**
   * Set the nullary search operation to be used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.2).
   *
   * @param op
   *          the nullary search operation to use
   */
  @SuppressWarnings("unchecked")
  public void setNullarySearchOperation(
      final INullarySearchOperation<double[]> op) {
    this.o0 = ((op != null) ? op
        : ((INullarySearchOperation) (NullarySearchOperation.NULL_CREATION)));
  }

  /**
   * Get the nullary search operation which is used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.2).
   *
   * @return the nullary search operation to use
   */
  public final INullarySearchOperation<double[]> getNullarySearchOperation() {
    return this.o0;
  }

  /**
   * Perform a simple differential evolution as defined in
   * Chapter 33
   *
   * @param f
   *          the objective function (Definition D2.3)
   * @param create
   *          the nullary search operator
   *          (Section 4.2) for creating the initial
   *          genotype
   * @param crossover
   *          the ternary crossover operator
   * @param gpm
   *          the genotype-phenotype mapping
   *          (Section 4.3)
   * @param term
   *          the termination criterion
   *          (Section 6.3.3)
   * @param ps
   *          the population size
   * @param r
   *          the random number generator
   * @return the individual containing the best candidate solution
   *         (Definition D2.2) found
   * @param <X>
   *          the problem space (Section 2.1)
   */
  @SuppressWarnings("unchecked")
  public static final <X> Individual<double[], X> differentialEvolution(
      //
      final IObjectiveFunction<X> f,
      final INullarySearchOperation<double[]> create,//
      final ITernarySearchOperation<double[]> crossover,//
      final IGPM<double[], X> gpm,//
      final int ps, final ITerminationCriterion term, final Random r) {

    Individual<double[], X>[] pop, npop;
    Individual<double[], X> p, best;
    int i, a, b, c;

    best = new Individual<double[], X>();
    best.v = Constants.WORST_FITNESS;
    pop = new Individual[ps];
    npop = new Individual[ps];

    // build the initial population of random individuals
    for (i = npop.length; (--i) >= 0;) {
      p = new Individual<double[], X>();
      npop[i] = p;
      p.g = create.create(r);
    }

    // the basic loop of the Differential Evolution
    for (;;) {
      // for eachgeneration do...

      // for each individual in the population
      for (i = npop.length; (--i) >= 0;) {
        p = npop[i];

        // perform the genotype-phenotype mapping
        p.x = gpm.gpm(p.g, r);
        // compute the objective value
        p.v = f.compute(p.x, r);

        // fight against direct parent
        if ((pop[i] == null) || (p.v < pop[i].v)) {
          pop[i] = p;

          // is the current individual the best one so far?
          if (p.v < best.v) {
            best.assign(p);
          }
        }

        // after each objective function evaluation, check if we should
        // stop
        if (term.terminationCriterion()) {
          // if we should stop, return the best individual found
          return best;
        }
      }

      for (i = npop.length; (--i) >= 0;) {
        a = r.nextInt(pop.length);
        do {
          b = r.nextInt(pop.length);
        } while (a == b);

        do {
          c = r.nextInt(pop.length);
        } while ((a == c) || (b == c));

        npop[i] = p = new Individual<double[], X>();
        p.g = crossover.ternaryRecombine(pop[a].g, pop[b].g, pop[c].g, r);

      }
    }
  }

  /**
   * Invoke the differential evolution
   *
   * @param r
   *          the randomizer (will be used directly without setting the
   *          seed)
   * @param term
   *          the termination criterion (will be used directly without
   *          resetting)
   * @param result
   *          a list to which the results are to be appended
   */
  @Override
  public void call(final Random r, final ITerminationCriterion term,
      final List<Individual<double[], X>> result) {

    result.add(DifferentialEvolution1.differentialEvolution(//
        this.getObjectiveFunction(),//
        this.getNullarySearchOperation(),//
        this.getTernarySearchOperation(),//
        this.getGPM(), //
        this.getPopulationSize(), //
        term, //
        r));
  }

  /**
   * Obtain the ternary search operation
   *
   * @return the ternary search operation
   */
  public final ITernarySearchOperation<double[]> getTernarySearchOperation() {
    return this.tc;
  }

  /**
   * Set the ternary search operation
   *
   * @param atc
   *          the ternary search operation
   */
  public final void setTernarySearchOperation(
      final ITernarySearchOperation<double[]> atc) {
    this.tc = atc;
  }

  /**
   * Set the population size, i.e., the number of individuals which are
   * kept in the population.
   *
   * @param aps
   *          the population size
   */
  public final void setPopulationSize(final int aps) {
    this.ps = Math.max(1, aps);
  }

  /**
   * Set the population size, i.e., the number of individuals which are
   * kept in the population.
   *
   * @return the population size
   */
  public final int getPopulationSize() {
    return this.ps;
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  @Override
  public String getConfiguration(final boolean longVersion) {
    StringBuilder sb;
    IOptimizationModule o;

    sb = new StringBuilder(super.getConfiguration(longVersion));

    o = this.getNullarySearchOperation();
    if (o != null) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append("o0=");//$NON-NLS-1$
      sb.append(o.toString(longVersion));
    }

    sb.append(",ps=");//$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.getPopulationSize()));

    if (this.tc != null) {
      sb.append(",op3=");//$NON-NLS-1$
      sb.append(this.tc.toString(longVersion));
    }

    return sb.toString();
  }

  /**
   * Get the name of the optimization module
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  @Override
  public String getName(final boolean longVersion) {
    return (longVersion ? super.getName(true) : "DE-1"); //$NON-NLS-1$
  }
}