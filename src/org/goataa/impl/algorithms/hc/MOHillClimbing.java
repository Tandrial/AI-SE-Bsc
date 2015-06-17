// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.hc;

import java.util.List;
import java.util.Random;

import org.goataa.impl.algorithms.MOLocalSearchAlgorithm;
import org.goataa.impl.utils.Archive;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.impl.utils.Utils;
import org.goataa.spec.IGPM;
import org.goataa.spec.IIndividualComparator;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.IUnarySearchOperation;

/**
 * An implementation of the Multi-Objective Hill Climbing algorithm
 * introduced as Algorithm 26.2.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public final class MOHillClimbing<G, X> extends
    MOLocalSearchAlgorithm<G, X> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantiate the hill climbing class */
  public MOHillClimbing() {
    super();
  }

  /**
   * Invoke the multi-objective hill climber.
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
      final List<MOIndividual<G, X>> result) {

    MOHillClimbing.hillClimbingMO(//
        this.getObjectiveFunctions(),//
        this.getComparator(),//
        this.getNullarySearchOperation(), //
        this.getUnarySearchOperation(),//
        this.getGPM(), term,//
        this.getMaxSolutions(),//
        r, //
        result);

  }

  /**
   * We place the complete Hill Climbing method as defined in
   * Algorithm 26.1 into this single procedure.
   *
   * @param f
   *          the objective functions (Definition D2.3)
   * @param cmp
   *          the comparator
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
   * @param result
   *          the list where the best individuals should be added to
   * @param r
   *          the random number generator
   *          (Definition D2.2) found
   * @param maxSolutions
   *          the maximum number of allowed solutions
   * @param <G>
   *          the search space (Section 4.1)
   * @param <X>
   *          the problem space (Section 2.1)
   */
  public static final <G, X> void hillClimbingMO(
  //
      final IObjectiveFunction<X>[] f,//
      final IIndividualComparator cmp,//
      final INullarySearchOperation<G> create,//
      final IUnarySearchOperation<G> mutate,//
      final IGPM<G, X> gpm,//
      final ITerminationCriterion term,//
      final int maxSolutions,//
      final Random r,//
      final List<MOIndividual<G, X>> result) {

    MOIndividual<G, X> p, pnew;
    Archive<G, X> arc;
    int max;

    p = new MOIndividual<G, X>(f.length);
    pnew = new MOIndividual<G, X>(f.length);
    arc = new Archive<G, X>(maxSolutions, cmp);

    // create the first genotype, map it to a phenotype, and evaluate it
    p.g = create.create(r);
    p.x = gpm.gpm(p.g, r);
    p.evaluate(f, r);
    arc.add(p);

    // check the termination criterion
    while (!(term.terminationCriterion())) {
      pnew = new MOIndividual<G, X>(f.length);

      mutateCreate: {
        for (max = 1000; (--max) >= 0;) {
          // pick a random individual from the set of best individuals
          p = arc.get(r.nextInt(arc.size()));

          pnew.g = mutate.mutate(p.g, r);
          if (!(Utils.equals(pnew.g, p.g))) {
            break mutateCreate;
          }
        }
        pnew.g = create.create(r);
      }
      pnew.x = gpm.gpm(pnew.g, r);
      pnew.evaluate(f, r);

      // check whether the individual should enter the archive
      arc.add(pnew);
    }

    result.addAll(arc);
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
    return "MOHC"; //$NON-NLS-1$
  }
}