// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms;

import java.util.List;
import java.util.Random;

import org.goataa.impl.searchOperations.NullarySearchOperation;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.IGPM;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ITerminationCriterion;

/**
 * A simple implementation of the Random Sampling algorithm introduced as
 * Algorithm 8.1.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public final class RandomSampling<G, X> extends
    SOOptimizationAlgorithm<G, X, Individual<G, X>> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the nullary search operation */
  private INullarySearchOperation<G> o0;

  /** instantiate the random sampling class */
  @SuppressWarnings("unchecked")
  public RandomSampling() {
    super();
    this.o0 = (INullarySearchOperation) (NullarySearchOperation.NULL_CREATION);
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
  public void setNullarySearchOperation(final INullarySearchOperation<G> op) {
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
  public final INullarySearchOperation<G> getNullarySearchOperation() {
    return this.o0;
  }

  /**
   * Invoke the random sampling process.
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

    result.add(RandomSampling.randomSampling(this.getObjectiveFunction(),//
        this.getNullarySearchOperation(), //
        this.getGPM(), term, r));

  }

  /**
   * We place the complete Random Sampling method as defined in
   * Algorithm 8.1 into this single procedure.
   *
   * @param f
   *          the objective function (Definition D2.3)
   * @param create
   *          the nullary search operator
   *          (Section 4.2) for creating the initial
   *          genotype
   * @param gpm
   *          the genotype-phenotype mapping
   *          (Section 4.3)
   * @param term
   *          the termination criterion
   *          (Section 6.3.3)
   * @param r
   *          the random number generator
   * @return the individual holding the best candidate solution
   *         (Definition D2.2) found
   * @param <G>
   *          the search space (Section 4.1)
   * @param <X>
   *          the problem space (Section 2.1)
   */
  public static final <G, X> Individual<G, X> randomSampling(
      //
      final IObjectiveFunction<X> f,
      final INullarySearchOperation<G> create,//
      final IGPM<G, X> gpm,//
      final ITerminationCriterion term, final Random r) {

    Individual<G, X> p, pbest;
    int t;

    p = new Individual<G, X>();
    pbest = new Individual<G, X>();

    // create the first genotype, map it to a phenotype, and evaluate it
    p.g = create.create(r);
    t = 1;

    // check the termination criterion
    while (!(term.terminationCriterion())) {
      p.x = gpm.gpm(p.g, r);
      p.v = f.compute(p.x, r);

      // remember the best candidate solution
      if ((t == 1) || (p.v < pbest.v)) {
        pbest.assign(p);
      }

      t++;

      // Instead of modifying existing genotypes as done in
      // Listing 56.5, we create
      // new ones in each round
      p.g = create.create(r);
    }

    return pbest;
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

    sb = new StringBuilder();

    sb.append(super.getConfiguration(longVersion));

    o = this.getNullarySearchOperation();
    if (o != null) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append("o0=");//$NON-NLS-1$
      sb.append(o.toString(longVersion));
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
    if (longVersion) {
      return this.getClass().getSimpleName();
    }
    return "RS"; //$NON-NLS-1$
  }
}