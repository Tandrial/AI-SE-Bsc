// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import org.goataa.impl.utils.Individual;

/**
 * A simple and general interface which enables us to provide the
 * functionality of a single-objective optimization algorithm (see
 * Section 1.3 and
 * Definition D6.3) in a unified way.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @param <IT>
 *          the individual type
 * @author Thomas Weise
 */
public interface ISOOptimizationAlgorithm<G, X, IT extends Individual<G, X>>
    extends IOptimizationModule, Callable<List<IT>>, Runnable {

  /**
   * Invoke the optimization process. This method calls the optimizer and
   * returns the list of best individuals (see Definition D4.18)
   * found. Usually, only a single individual will be returned.
   *
   * @return computed result
   */
  public abstract List<IT> call();

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
  public abstract void call(final Random r,
      final ITerminationCriterion term, final List<IT> result);

  /**
   * Set the maximum number of solutions which can be returned by the
   * algorithm
   *
   * @param max
   *          the maximum number of solutions
   */
  public void setMaxSolutions(final int max);

  /**
   * Get the maximum number of solutions which can be returned by the
   * algorithm
   *
   * @return the maximum number of solutions
   */
  public int getMaxSolutions();

  /**
   * Set the genotype-phenotype mapping (GPM, see
   * Section 4.3 and
   * Listing 55.7.
   *
   * @param gpm
   *          the GPM to use during the optimization process
   */
  public abstract void setGPM(final IGPM<G, X> gpm);

  /**
   * Get the genotype-phenotype mapping (GPM, see
   * Section 4.3 and
   * Listing 55.7.
   *
   * @return gpm the GPM which is used during the optimization process
   */
  public abstract IGPM<G, X> getGPM();

  /**
   * Set the termination criterion (see
   * Section 6.3.3 and
   * Listing 55.9).
   *
   * @param term
   *          the termination criterion
   */
  public abstract void setTerminationCriterion(
      final ITerminationCriterion term);

  /**
   * Get the termination criterion (see
   * Section 6.3.3 and
   * Listing 55.9).
   *
   * @return the termination criterion
   */
  public abstract ITerminationCriterion getTerminationCriterion();

  /**
   * Set the objective function (see Section 2.2
   * and Listing 55.8) to be used to
   * guide the optimization process
   *
   * @param f
   *          the objective function
   */
  public abstract void setObjectiveFunction(final IObjectiveFunction<X> f);

  /**
   * Get the objective function (see Section 2.2
   * and Listing 55.8) which is used
   * to guide the optimization process
   *
   * @return the objective function
   */
  public abstract IObjectiveFunction<X> getObjectiveFunction();

  /**
   * Set the seed for the internal random number generator
   *
   * @param seed
   *          the seed for the random number generator
   */
  public abstract void setRandSeed(final long seed);

  /**
   * Get the current seed for the internal random number generator
   *
   * @return the current seed for the random number generator
   */
  public abstract long getRandSeed();

  /**
   * Set that a randomly selected seed should be used for the random number
   * generator every time. If you set a random seed with setRandSeed, the
   * effect of this method will be nullified. Vice versa, this method
   * nullifies the effect of a previous random seed setting via
   * setRandSeed.
   */
  public abstract void useRandomRandSeed();

  /**
   * Are we currently using a random rand seed for each run of the
   * optimizer?
   *
   * @return true if we are currently using a random rand seed for each run
   *         of the optimizer, false otherwise
   */
  public abstract boolean usesRandomRandSeed();

}