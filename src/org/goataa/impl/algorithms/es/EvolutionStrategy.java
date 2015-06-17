// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.es;

import java.util.List;
import java.util.Random;

import org.goataa.impl.algorithms.LocalSearchAlgorithm;
import org.goataa.impl.algorithms.ea.selection.RandomSelection;
import org.goataa.impl.algorithms.ea.selection.TruncationSelection;
import org.goataa.impl.searchOperations.strings.real.nary.DoubleArrayDominantRecombination;
import org.goataa.impl.searchOperations.strings.real.nary.DoubleArrayIntermediateRecombination;
import org.goataa.impl.searchOperations.strings.real.nullary.DoubleArrayUniformCreation;
import org.goataa.impl.searchOperations.strings.real.unary.DoubleArrayStdDevNormalMutation;
import org.goataa.impl.searchOperations.strings.real.unary.DoubleArrayStrategyLogNormalMutation;
import org.goataa.impl.utils.Constants;
import org.goataa.impl.utils.Individual;
import org.goataa.impl.utils.TextUtils;
import org.goataa.spec.IGPM;
import org.goataa.spec.INarySearchOperation;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ISelectionAlgorithm;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.IUnarySearchOperation;

/**
 * An Evolution Strategy implementation which follows the definitions in
 * Chapter 30 and which comes close to
 * Algorithm 30.1. This implementation uses vectors of standard
 * deviations as endogenous strategy parameters, as discussed in
 * Section 30.4.1.2.
 *
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class EvolutionStrategy<X> extends
    LocalSearchAlgorithm<double[], X, Individual<double[], X>> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the survival selection algorithm */
  private ISelectionAlgorithm survivalSelection;

  /** the survival parental algorithm */
  private ISelectionAlgorithm parentalSelection;

  /**
   * mu - the number of parents in the mating pool, see
   * Section 28.1.4.3
   */
  private int mu;

  /**
   * lambda - the number of offsprings, see
   * Section 28.1.4.3
   */
  private int lambda;

  /**
   * rho - the number of parents per offspring, see
   * Section 28.1.4.3
   */
  private int rho;

  /** true for (lambda+mu) strategies, false for (lambda,mu) strategies */
  private boolean plus;

  /** the dimension */
  private int dimension;

  /** the minimum values per dimension */
  private double minX;

  /** the maximum values per dimension */
  private double maxX;

  /** do we need an internal setup ? */
  private boolean internalSetupNeeded;

  /** the unary reproduction operation was set by the user */
  private boolean unaryOperationSet;

  /** the nullary reproduction operation was set by the user */
  private boolean nullaryOperationSet;

  /** the internal search operation for creating the strategy parameters */
  private INullarySearchOperation<double[]> createStrategyParameters;

  /** the strategy parameter mutator */
  private IUnarySearchOperation<double[]> mutateStrategyParameters;

  /** the genotype crossover method */
  private INarySearchOperation<double[]> crossoverGenotype;

  /** the strategy crossover method */
  private INarySearchOperation<double[]> crossoverStrategy;

  /** instantiate the evolution strategy */
  public EvolutionStrategy() {
    super();

    this.survivalSelection = new TruncationSelection();
    this.parentalSelection = RandomSelection.RANDOM_SELECTION;
    this.crossoverGenotype = DoubleArrayDominantRecombination.DOUBLE_ARRAY_DOMINANT_RECOMBINATION;
    this.crossoverStrategy = DoubleArrayIntermediateRecombination.DOUBLE_ARRAY_INTERMEDIATE_RECOMBINATION;
    this.lambda = 100;
    this.mu = 10;
    this.rho = 2;
    this.plus = true;
    this.dimension = 10;
    this.minX = -1d;
    this.maxX = 1d;
    this.internalSetupNeeded = true;
  }

  /**
   * Apply an Evolution Strategy implementation which follows the
   * definitions in Chapter 30 and which comes
   * close to Algorithm 30.1. This implementation uses vectors
   * of standard deviations as endogenous strategy parameters, as discussed
   * in Section 30.4.1.2.
   *
   * @param f
   *          the objective function (Definition D2.3)
   * @param createGenotype
   *          the nullary search operator
   *          (Section 4.2) for creating the initial
   *          genotypes
   * @param createStrategy
   *          the nullary search operator
   *          (Section 4.2) for creating the initial
   *          strategy parameters
   * @param mutateGenotype
   *          the unary search operator (mutation,
   *          Section 4.2) for modifying existing
   *          genotypes
   * @param mutateStrategy
   *          the unary search operator (mutation,
   *          Section 4.2) for modifying existing
   *          strategy parameters
   * @param recombineGenotype
   *          the n-ary recombination operator for genotypes
   * @param recombineStrategy
   *          the n-ary recombination operator for strategy parameters
   * @param gpm
   *          the genotype-phenotype mapping
   *          (Section 4.3)
   * @param survivalSel
   *          the survival selection algorithm (Section 28.4
   * @param parentalSel
   *          the parental/sexual selection algorithm
   *          (Section 28.4
   * @param m
   *          the number of parents in the mating pool
   * @param la
   *          the number of offspring
   * @param rh
   *          the number of parents per offspring
   * @param pl
   *          true for a (lambda+mu), false for a (lambda,mu) strategy
   * @param term
   *          the termination criterion
   *          (Section 6.3.3)
   * @param r
   *          the random number generator
   * @return the individual containing the best candidate solution
   *         (Definition D2.2) found
   * @param <X>
   *          the problem space (Section 2.1)
   */
  @SuppressWarnings("unchecked")
  public static final <X> Individual<double[], X> evolutionaryStrategy(
      //
      final IObjectiveFunction<X> f,
      final INullarySearchOperation<double[]> createGenotype,//
      final INullarySearchOperation<double[]> createStrategy,//
      final IUnarySearchOperation<double[]> mutateGenotype,//
      final IUnarySearchOperation<double[]> mutateStrategy,//
      final INarySearchOperation<double[]> recombineGenotype,//
      final INarySearchOperation<double[]> recombineStrategy,//
      final IGPM<double[], X> gpm,//
      final ISelectionAlgorithm survivalSel,//
      final ISelectionAlgorithm parentalSel,//
      final int m,//
      final int la,//
      final int rh,//
      final boolean pl,//
      final ITerminationCriterion term,//
      final Random r) {

    ESIndividual<X>[] selected, pop, parents;
    double[][] parents2, parents3;
    int i, j;
    ESIndividual<X> p;
    Individual<double[], X> best;
    DoubleArrayStdDevNormalMutation mut;

    best = new ESIndividual<X>();
    best.v = Constants.WORST_FITNESS;
    selected = new ESIndividual[m];
    pop = selected;
    parents = new ESIndividual[rh];
    parents2 = new double[rh][];
    parents3 = new double[rh][];

    if (mutateGenotype instanceof DoubleArrayStdDevNormalMutation) {
      mut = ((DoubleArrayStdDevNormalMutation) mutateGenotype);
    } else {
      mut = null;
    }

    // build the initial population of random individuals
    for (i = selected.length; (--i) >= 0;) {
      p = new ESIndividual<X>();
      selected[i] = p;
      p.g = createGenotype.create(r);
      p.w = createStrategy.create(r);
    }

    // the basic loop of the Evolution Strategy Algorithm 30.1
    for (;;) {
      // for eachgeneration do...

      // for each individual in the population
      for (i = pop.length; (--i) >= 0;) {
        p = pop[i];

        // perform the genotype-phenotype mapping
        p.x = gpm.gpm(p.g, r);
        // compute the objective value
        p.v = f.compute(p.x, r);

        // is the current individual the best one so far?
        if (p.v < best.v) {
          best.assign(p);
        }

        // after each objective function evaluation, check if we should
        // stop
        if (term.terminationCriterion()) {
          // if we should stop, return the best individual found
          return best;
        }
      }

      // perform the selection step, usually truncation selection (see
      // Algorithm 28.8)
      if (pop != selected) {
        // for (lambda+mu) strategies use both, parents and offspring
        if (pl) {
          System.arraycopy(selected, 0, pop, la, m);
        }
        survivalSel.select(pop, 0, pop.length, selected, 0,
            selected.length, r);
      }

      if (pop == selected) {
        pop = new ESIndividual[pl ? (la + m) : la];
      }

      // fill the new population with new offspring
      for (i = pop.length; (--i) >= 0;) {
        // select the parents for the new individual, which usually is done
        // randomly with Algorithm 28.15
        parentalSel.select(selected, 0, selected.length, parents, 0,
            parents.length, r);

        for (j = parents.length; (--j) >= 0;) {
          p = parents[j];
          parents2[j] = p.g;
          parents3[j] = p.w;
        }

        p = new ESIndividual<X>();
        pop[i] = p;

        // usually done via dominate recombination, see
        // Algorithm 30.2
        p.g = recombineGenotype.combine(parents2, r);

        // We only use the strategy parameters if the mutator actually uses
        // them: mut is null if it is not the ES-typical mutator.
        if (mut != null) {
          // usually done via intermediate recombination, see
          // Algorithm 30.3
          p.w = recombineStrategy.combine(parents3, r);

          // usually done via log-normal mutation, see
          // Algorithm 30.8
          p.w = mutateStrategy.mutate(p.w, r);
          mut.setStdDevs(p.w);
        }

        // usually done via normally distributed mutation as specified in
        // Algorithm 30.5.
        p.g = mutateGenotype.mutate(p.g, r);
      }
    }
  }

  /**
   * Invoke the evolution strategy
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

    this.internalSetup();

    result.add(EvolutionStrategy.evolutionaryStrategy(//
        this.getObjectiveFunction(),//
        this.getNullarySearchOperation(),//
        this.createStrategyParameters,//
        this.getUnarySearchOperation(),//
        this.mutateStrategyParameters, //
        this.crossoverGenotype, this.crossoverStrategy,//
        this.getGPM(),//
        this.getSelectionAlgorithm(), //
        this.parentalSelection,//
        this.getMu(),//
        this.getLambda(),//
        this.getRho(), //
        this.isPlus(), //
        term,//
        r));
  }

  /**
   * Set the dimension of the search space
   *
   * @param dim
   *          the dimension of the search space
   */
  public final void setDimension(final int dim) {
    this.dimension = dim;
    this.internalSetupNeeded = true;
  }

  /**
   * Get the dimension of the search space
   *
   * @return the dimension of the search space
   */
  public final int getDimension() {
    return this.dimension;
  }

  /**
   * Set the minimum value of the search space
   *
   * @param x
   *          the minimum value of the search space
   */
  public final void setMinimum(final double x) {
    this.minX = x;
    this.internalSetupNeeded = true;
  }

  /**
   * Get the minimum value of the search space
   *
   * @return the minimum value of the search space
   */
  public final double getMaximum() {
    return this.maxX;
  }

  /**
   * Set the maximum value of the search space
   *
   * @param x
   *          the maximum value of the search space
   */
  public final void setMaximum(final double x) {
    this.maxX = x;
    this.internalSetupNeeded = true;
  }

  /**
   * Get the minimum value of the search space
   *
   * @return the minimum value of the search space
   */
  public final double getMinimum() {
    return this.minX;
  }

  /** perform some internalsetup */
  private final void internalSetup() {
    final double maxS;

    if (this.internalSetupNeeded) {
      this.internalSetupNeeded = false;

      if (!(this.nullaryOperationSet)) {
        super.setNullarySearchOperation(new DoubleArrayUniformCreation(
            this.dimension, this.minX, this.maxX));
      }

      maxS = Math.sqrt(this.maxX - this.minX);

      this.createStrategyParameters = new DoubleArrayUniformCreation(
          this.dimension, Double.MIN_VALUE, maxS);
      this.mutateStrategyParameters = new DoubleArrayStrategyLogNormalMutation(
          maxS);

      if (!(this.unaryOperationSet)) {
        super.setUnarySearchOperation(new DoubleArrayStdDevNormalMutation(
            this.dimension, this.minX, this.maxX));
      }
    }
  }

  /**
   * Set mu, the number of parents in the mating pool (see
   * Section 28.1.4.3)
   *
   * @param m
   *          the parameter mu
   */
  public final void setMu(final int m) {
    this.mu = Math.max(1, m);
  }

  /**
   * Get mu, the number of parents in the mating pool (see
   * Section 28.1.4.3)
   *
   * @return the parameter mu
   */
  public final int getMu() {
    return this.mu;
  }

  /**
   * Set lambda, the number of offspring (see
   * Section 28.1.4.3)
   *
   * @param l
   *          the parameter lambda
   */
  public final void setLambda(final int l) {
    this.lambda = Math.max(1, l);
  }

  /**
   * Get lambda, the number of offspring (see
   * Section 28.1.4.3)
   *
   * @return the parameter lambda
   */
  public final int getLambda() {
    return this.lambda;
  }

  /**
   * Set rho, the number of parents per offspring (see
   * Section 28.1.4.3)
   *
   * @param r
   *          the parameter rho
   */
  public final void setRho(final int r) {
    this.rho = Math.max(1, r);
  }

  /**
   * Get rho, the number of parents per offspring (see
   * Section 28.1.4.3)
   *
   * @return the parameter rho
   */
  public final int getRho() {
    return this.rho;
  }

  /**
   * Set whether we have a (lambda+mu) strategy or a (lambda,mu) strategy
   *
   * @param p
   *          true for (lambda+mu) strategies, false for (lambda,mu)
   *          strategies
   */
  public final void setPlus(final boolean p) {
    this.plus = p;
  }

  /**
   * Get whether we have a (lambda+mu) strategy or a (lambda,mu) strategy
   *
   * @return true for (lambda+mu) strategies, false for (lambda,mu)
   *         strategies
   */
  public final boolean isPlus() {
    return this.plus;
  }

  /**
   * Set the selection algorithm (see Section 28.4) to be
   * used by this evolutionary algorithm.
   *
   * @param sel
   *          the selection algorithm
   */
  public final void setSelectionAlgorithm(final ISelectionAlgorithm sel) {
    this.survivalSelection = sel;
  }

  /**
   * Get the selection algorithm (see Section 28.4) which is
   * used by this evolutionary algorithm.
   *
   * @return the selection algorithm
   */
  public final ISelectionAlgorithm getSelectionAlgorithm() {
    return this.survivalSelection;
  }

  /**
   * Set the unary search operation to be used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.3).
   *
   * @param op
   *          the unary search operation to use
   */
  @Override
  public void setUnarySearchOperation(
      final IUnarySearchOperation<double[]> op) {
    this.unaryOperationSet = (op != null);
    super.setUnarySearchOperation(op);
  }

  /**
   * Set the nullary search operation to be used by this optimization
   * algorithm (see Section 4.2 and
   * Listing 55.2).
   *
   * @param op
   *          the nullary search operation to use
   */
  @Override
  public void setNullarySearchOperation(
      final INullarySearchOperation<double[]> op) {
    this.nullaryOperationSet = (op != null);
    super.setNullarySearchOperation(op);
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
      return super.getName(longVersion);
    }
    return "ES"; //$NON-NLS-1$
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
    IOptimizationModule m;

    sb = new StringBuilder();

    sb.append('(');
    sb.append(this.mu);
    if (this.rho != 1) {
      sb.append('/');
      sb.append(this.rho);
    }

    sb.append(this.plus ? '+' : ',');
    sb.append(this.lambda);
    sb.append(')');

    sb.append(",G=["); //$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.minX));

    sb.append(","); //$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.maxX));

    sb.append("]^");//$NON-NLS-1$
    sb.append(this.dimension);

    sb.append(',');
    sb.append(super.getConfiguration(longVersion));

    m = this.createStrategyParameters;
    if (m != null) {
      sb.append(",o0s="); //$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    m = this.mutateStrategyParameters;
    if (m != null) {
      sb.append(",o1s="); //$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    m = this.crossoverStrategy;
    if (m != null) {
      sb.append(",oxs="); //$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    m = this.crossoverGenotype;
    if (m != null) {
      sb.append(",oxg="); //$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    if (this.survivalSelection != null) {
      sb.append(",selSur="); //$NON-NLS-1$
      sb.append(this.survivalSelection.toString(longVersion));
    }

    if (this.parentalSelection != null) {
      sb.append(",selPar="); //$NON-NLS-1$
      sb.append(this.parentalSelection.toString(longVersion));
    }

    return sb.toString();
  }
}