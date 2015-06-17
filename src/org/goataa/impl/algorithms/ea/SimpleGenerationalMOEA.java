// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.ea;

import java.util.List;
import java.util.Random;

import org.goataa.impl.utils.Archive;
import org.goataa.impl.utils.MOIndividual;
import org.goataa.spec.IBinarySearchOperation;
import org.goataa.spec.IFitnessAssignmentProcess;
import org.goataa.spec.IGPM;
import org.goataa.spec.IIndividualComparator;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ISelectionAlgorithm;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A straightforward implementation of the multi-objective Evolutionary
 * Algorithm given in Section 28.1.1 and specified in
 * Algorithm 28.2 with generational population treatment
 * (see Section 28.1.4.1).
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class SimpleGenerationalMOEA<G, X> extends MOEABase<G, X> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** instantiate the simple generational EA */
  public SimpleGenerationalMOEA() {
    super();
  }

  /**
   * Perform a simple EA as defined in Algorithm 28.1 while
   * using a generational population handling (see
   * Section 28.1.4.1).
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
   *          the unary search operator (mutation,
   *          Section 4.2) for modifying existing
   *          genotypes
   * @param recombine
   *          the recombination operator (Definition D28.14)
   * @param gpm
   *          the genotype-phenotype mapping
   *          (Section 4.3)
   * @param sel
   *          the selection algorithm (Section 28.4
   * @param fa
   *          the fitness assignment process
   * @param term
   *          the termination criterion
   *          (Section 6.3.3)
   * @param ps
   *          the population size
   * @param mps
   *          the mating pool size
   * @param cr
   *          the crossover rate
   * @param mr
   *          the mutation rate
   * @param maxSolutions
   *          the maximum number of solutions
   * @param r
   *          the random number generator
   * @param result
   *          the list to add the best candidate solutions
   *          (Definition D2.2) to
   * @param <G>
   *          the search space (Section 4.1)
   * @param <X>
   *          the problem space (Section 2.1)
   */
  @SuppressWarnings("unchecked")
  public static final <G, X> void evolutionaryAlgorithmMO(
  //
      final IObjectiveFunction<X>[] f,//
      final IIndividualComparator cmp,//
      final INullarySearchOperation<G> create,//
      final IUnarySearchOperation<G> mutate,//
      final IBinarySearchOperation<G> recombine, final IGPM<G, X> gpm,//
      final ISelectionAlgorithm sel,//
      final IFitnessAssignmentProcess fa,//
      final int ps, final int mps,//
      final double mr,//
      final double cr,//
      final ITerminationCriterion term, final int maxSolutions,//
      final Random r,//
      final List<MOIndividual<G, X>> result) {

    MOIndividual<G, X>[] pop, mate;
    int mateIndex, popIndex, i, j, k;
    MOIndividual<G, X> p;
    Archive<G, X> best;

    best = new Archive<G, X>(maxSolutions, cmp);
    pop = new MOIndividual[ps];
    mate = new MOIndividual[mps];

    // build the initial population of random individuals
    for (i = pop.length; (--i) >= 0;) {
      p = new MOIndividual<G, X>(f.length);
      pop[i] = p;
      p.g = create.create(r);
    }

    // the basic loop of the Evolutionary Algorithm 28.1
    for (;;) {
      // for each generation do...

      // for each individual in the population
      for (i = pop.length; (--i) >= 0;) {
        p = pop[i];

        // perform the genotype-phenotype mapping
        p.x = gpm.gpm(p.g, r);

        // compute the objective values
        p.evaluate(f, r);

        // update the archive
        best.add(p);

        // after each objective function evaluation, check if we should
        // stop
        if (term.terminationCriterion()) {
          // if we should stop, return the best individuals found
          result.addAll(best);
          return;
        }
      }

      // assign the fitness
      fa.assignFitness(pop, 0, pop.length, cmp, r);

      // perform the selection step
      sel.select(pop, 0, pop.length, mate, 0, mate.length, r);

      mateIndex = 0;
      popIndex = ps;

      // create cr*ps individuals with crossover
      k = (int) (0.5 + (cr * ps));
      for (j = 0; (j < k) && (popIndex > 0); j++) {
        p = new MOIndividual<G, X>(f.length);

        // recombine an individual with another
        p.g = recombine.recombine(mate[mateIndex].g,
            mate[r.nextInt(mps)].g, r);
        pop[--popIndex] = p;
        mateIndex = ((mateIndex + 1) % mps);
      }

      // create mr*ps individuals with mutation
      k = (int) (0.5 + (mr * ps));
      for (j = 0; (j < k) && (popIndex > 0); j++) {
        p = new MOIndividual<G, X>(f.length);

        // recombine an individual with another
        p.g = mutate.mutate(mate[mateIndex].g, r);
        pop[--popIndex] = p;
        mateIndex = ((mateIndex + 1) % mps);
      }

      // copy the remaining individuals
      for (; popIndex > 0;) {
        pop[--popIndex] = mate[mateIndex];
        mateIndex = ((mateIndex + 1) % mps);
      }
    }
  }

  /**
   * Invoke the multi-objective EA.
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

    evolutionaryAlgorithmMO(//
        this.getObjectiveFunctions(),//
        this.getComparator(),//
        this.getNullarySearchOperation(), //
        this.getUnarySearchOperation(),//
        this.getBinarySearchOperation(),//
        this.getGPM(), //
        this.getSelectionAlgorithm(),//
        this.getFitnesAssignmentProcess(),//
        this.getPopulationSize(),//
        this.getMatingPoolSize(),//
        this.getMutationRate(),//
        this.getCrossoverRate(),//
        term, //
        this.getMaxSolutions(),//
        r,//
        result);
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
    IOptimizationModule om;
    boolean ga;

    ga = false;

    om = this.getNullarySearchOperation();
    if (om != null) {
      if (om.getClass().getCanonicalName().contains("strings.bits.")) { //$NON-NLS-1$
        ga = true;
      }
    } else {
      om = this.getUnarySearchOperation();
      if (om != null) {
        if (om.getClass().getCanonicalName().contains("strings.bits.")) { //$NON-NLS-1$
          ga = true;
        }
      } else {
        om = this.getBinarySearchOperation();
        if (om != null) {
          if (om.getClass().getCanonicalName().contains("strings.bits.")) { //$NON-NLS-1$
            ga = true;
          }
        }
      }
    }

    if (ga) {
      return (longVersion ? "sMOGeneticAlgorithm" : "sMOGA");//$NON-NLS-1$//$NON-NLS-2$
    }

    if (longVersion) {
      return this.getClass().getSimpleName();
    }
    return "sMOEA"; //$NON-NLS-1$
  }

}