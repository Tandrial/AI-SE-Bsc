// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.sa;

import java.util.List;
import java.util.Random;

import org.goataa.impl.algorithms.LocalSearchAlgorithm;
import org.goataa.impl.algorithms.sa.temperatureSchedules.Exponential;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.IGPM;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.ITemperatureSchedule;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A simple implementation of the Simulated Annealing algorithm introduced
 * as Algorithm 27.1.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public final class SimulatedAnnealing<G, X> extends
    LocalSearchAlgorithm<G, X, Individual<G, X>> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the temperature schedule */
  private ITemperatureSchedule schedule;

  /** instantiate the simulated annealing class */
  public SimulatedAnnealing() {
    super();
  }

  /**
   * Set the temperature schedule (see
   * Definition D27.2) which will be responsible for
   * setting the acceptance probability of inferior candidate solutions
   * during all optimization steps.
   *
   * @param aschedule
   *          the temperature schedule
   */
  public final void setTemperatureSchedule(
      final ITemperatureSchedule aschedule) {
    this.schedule = aschedule;
  }

  /**
   * Get the temperature schedule (see
   * Definition D27.2) which is responsible for setting
   * the acceptance probability of inferior candidate solutions during all
   * optimization steps.
   *
   * @return the temperature schedule
   */
  public final ITemperatureSchedule getTemperatureSchedule() {
    return this.schedule;
  }

  /**
   * Invoke the optimization process. This method calls the optimizer and
   * returns the list of best individuals (see Definition D4.18)
   * found. Usually, only a single individual will be returned. Different
   * from the parameterless call method, here a randomizer and a
   * termination criterion are directly passed in. Also, a list to fill in
   * the optimization results is provided. This allows recursively using
   * the optimization algorithms.
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
      final List<Individual<G, X>> result) {

    result.add(SimulatedAnnealing.simulatedAnnealing(//
        this.getObjectiveFunction(),//
        this.getNullarySearchOperation(), //
        this.getUnarySearchOperation(),//
        this.getGPM(), this.getTemperatureSchedule(),//
        term, r));
  }

  /**
   * We place the complete Simulated Annealing method as defined in
   * Algorithm 27.1 into this single procedure.
   *
   * @param f
   *          the objective function (Definition D2.3)
   * @param create
   *          the nullary search operator
   *          (Section 4.2) for creating the initial
   *          genotype
   * @param mutate
   *          the unary search operator (Section 4.2)
   *          for modifying existing genotypes
   * @param gpm
   *          the genotype-phenotype mapping
   *          (Section 4.3)
   * @param temperature
   *          the temperature schedule according to
   *          (Definition D27.2
   * @param term
   *          the termination criterion
   *          (Section 6.3.3)
   * @param r
   *          the random number generator
   * @return the best candidate solution
   *         (Definition D2.2) found
   * @param <G>
   *          the search space (Section 4.1)
   * @param <X>
   *          the problem space (Section 2.1)
   */
  public static final <G, X> Individual<G, X> simulatedAnnealing(
      //
      final IObjectiveFunction<X> f,
      final INullarySearchOperation<G> create,//
      final IUnarySearchOperation<G> mutate,//
      final IGPM<G, X> gpm,//
      final ITemperatureSchedule temperature,
      final ITerminationCriterion term, final Random r) {

    Individual<G, X> pbest, pcur, pnew;
    int t;
    double T, DE;

    pbest = new Individual<G, X>();
    pnew = new Individual<G, X>();
    pcur = new Individual<G, X>();

    // create the first genotype, map it to a phenotype, and evaluate it
    pcur.g = create.create(r);
    pcur.x = gpm.gpm(pcur.g, r);
    pcur.v = f.compute(pcur.x, r);
    pbest.assign(pcur);
    t = 1;

    // check the termination criterion
    while (!(term.terminationCriterion())) {

      // compute the current temperature
      T = temperature.getTemperature(t);

      // modify the best point known, map the new point to a phenotype and
      // evaluat it
      pnew.g = mutate.mutate(pcur.g, r);
      pnew.x = gpm.gpm(pnew.g, r);
      pnew.v = f.compute(pnew.x, r);

      // compute the energy difference according to
      // Equation 27.2
      DE = pnew.v - pcur.v;

      // implement Equation 27.3
      if (DE <= 0.0d) {
        pcur.assign(pnew);

        // remember the best candidate solution
        if (pnew.v < pbest.v) {
          pbest.assign(pnew);
        }
      } else {
        // otherwise, use
        if (r.nextDouble() < Math.exp(-DE / T)) {
          pcur.assign(pnew);
        }
      }

      t++;
    }

    return pbest;
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
    ITemperatureSchedule ts;

    ts = this.getTemperatureSchedule();

    if (ts instanceof Exponential) {
      return (longVersion ? "SimulatedQuenching" : "SQ");//$NON-NLS-1$//$NON-NLS-2$
    }

    if (longVersion) {
      return this.getClass().getSimpleName();
    }
    return "SA"; //$NON-NLS-1$
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
    ITemperatureSchedule ts;
    String s;

    s = super.getConfiguration(longVersion);
    ts = this.getTemperatureSchedule();
    if (ts != null) {
      s += ',' + ts.toString(longVersion);
    }

    return s;
  }
}