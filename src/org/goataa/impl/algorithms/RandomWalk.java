// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms;

import java.util.List;
import java.util.Random;

import org.goataa.impl.utils.Individual;
import org.goataa.spec.IGPM;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A simple implementation of the Random Walk algorithm introduced as
 * Algorithm 8.2.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public final class RandomWalk<G, X> extends
    LocalSearchAlgorithm<G, X, Individual<G, X>> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantiate the random walk class */
  public RandomWalk() {
    super();
  }

  /**
   * Invoke the random walk.
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

    result.add(RandomWalk.randomWalk(this.getObjectiveFunction(),//
        this.getNullarySearchOperation(), //
        this.getUnarySearchOperation(),//
        this.getGPM(), term, r));
  }

  /**
   * We place the complete Random Walk method as defined in
   * Algorithm 8.2 into this single procedure.
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
  public static final <G, X> Individual<G, X> randomWalk(
      //
      final IObjectiveFunction<X> f,
      final INullarySearchOperation<G> create,//
      final IUnarySearchOperation<G> mutate,//
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

      // modify the last point checked, map the new point to a phenotype
      // and evaluat it - this is the main difference to
      // null is that we
      // do not use the best genotype for this
      p.g = mutate.mutate(p.g, r);
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
    if (longVersion) {
      return this.getClass().getSimpleName();
    }
    return "RW"; //$NON-NLS-1$
  }
}