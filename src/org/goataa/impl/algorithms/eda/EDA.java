// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms.eda;

import java.util.List;
import java.util.Random;

import org.goataa.impl.algorithms.SOOptimizationAlgorithm;
import org.goataa.impl.utils.Constants;
import org.goataa.impl.utils.Individual;
import org.goataa.impl.utils.TextUtils;
import org.goataa.spec.IGPM;
import org.goataa.spec.IModelBuilder;
import org.goataa.spec.IModelSampler;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ISelectionAlgorithm;
import org.goataa.spec.ITerminationCriterion;

/**
 * The basic Estimation of Distribution Algorithm (EDA) scheme according to
 * Algorithm 34.1.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @param <M>
 *          the model type
 * @author Thomas Weise
 */
public class EDA<G, X, M> extends
    SOOptimizationAlgorithm<G, X, Individual<G, X>> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the model creation operation */
  private INullarySearchOperation<M> modelCreate;

  /** the model builder */
  private IModelBuilder<M, G> modelBuilder;

  /** the model sampler */
  private IModelSampler<M, G> modelSampler;

  /** the selection algorithm */
  private ISelectionAlgorithm selectionAlgorithm;

  /** the population size */
  private int ps;

  /** the mating pool size */
  private int mps;

  /** instantiate an EDA */
  public EDA() {
    super();
    this.ps = 100;
    this.mps = 100;
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
   * Set the mating pool size, i.e., the number of individuals which will
   * be selected into the mating pool.
   *
   * @param amps
   *          the mating pool size
   */
  public final void setMatingPoolSize(final int amps) {
    this.mps = Math.max(1, amps);
  }

  /**
   * Set the mating pool size, i.e., the number of individuals which will
   * be selected into the mating pool.
   *
   * @return the mating pool size
   */
  public final int getMatingPoolSize() {
    return this.mps;
  }

  /**
   * Set the algorithm used to create a model
   *
   * @param create
   *          the model creation algorithm
   */
  public final void setModelCreator(final INullarySearchOperation<M> create) {
    this.modelCreate = create;
  }

  /**
   * Get the algorithm used to create a model
   *
   * @return the model creation algorithm
   */
  public final INullarySearchOperation<M> getModelCreator() {
    return this.modelCreate;
  }

  /**
   * Set the algorithm used to build/update a model
   *
   * @param builder
   *          the model building/updating algorithm
   */
  public final void setModelBuilder(final IModelBuilder<M, G> builder) {
    this.modelBuilder = builder;
  }

  /**
   * Get the algorithm used to build/update a model
   *
   * @return the model building/updating algorithm
   */
  public final IModelBuilder<M, G> getModelBuilder() {
    return this.modelBuilder;
  }

  /**
   * Set the algorithm used to sample a model
   *
   * @param sampler
   *          the model sampling algorithm
   */
  public final void setModelSampler(final IModelSampler<M, G> sampler) {
    this.modelSampler = sampler;
  }

  /**
   * Get the algorithm used to sample a model
   *
   * @return the model sampling algorithm
   */
  public final IModelSampler<M, G> getModelSampler() {
    return this.modelSampler;
  }

  /**
   * Set the selection algorithm (see Section 28.4) to be
   * used by this evolutionary algorithm.
   *
   * @param sel
   *          the selection algorithm
   */
  public final void setSelectionAlgorithm(final ISelectionAlgorithm sel) {
    this.selectionAlgorithm = sel;
  }

  /**
   * Get the selection algorithm (see Section 28.4) which is
   * used by this evolutionary algorithm.
   *
   * @return the selection algorithm
   */
  public final ISelectionAlgorithm getSelectionAlgorithm() {
    return this.selectionAlgorithm;
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

    sb = new StringBuilder(super.getConfiguration(longVersion));

    sb.append(",ps=");//$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.getPopulationSize()));

    sb.append(",mps=");//$NON-NLS-1$
    sb.append(TextUtils.formatNumber(this.getMatingPoolSize()));

    m = this.getModelCreator();
    if (m != null) {
      sb.append(",new=");//$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    m = this.getModelBuilder();
    if (m != null) {
      sb.append(",build=");//$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    m = this.getModelSampler();
    if (m != null) {
      sb.append(",sample=");//$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    m = this.getSelectionAlgorithm();
    if (m != null) {
      sb.append(",sel=");//$NON-NLS-1$
      sb.append(m.toString(longVersion));
    }

    return sb.toString();
  }

  /**
   * Perform a basic Estimation of Distribution algorithm as defined in
   * Algorithm 34.1.
   *
   * @param f
   *          the objective function (Definition D2.3)
   * @param newModel
   *          the model creation operation
   * @param buildModel
   *          the model building/updating operator
   * @param sampleModel
   *          the model sampling algorithm
   * @param gpm
   *          the genotype-phenotype mapping
   *          (Section 4.3)
   * @param sel
   *          the selection algorithm (Section 28.4
   * @param term
   *          the termination criterion
   *          (Section 6.3.3)
   * @param ps
   *          the population size
   * @param mps
   *          the mating pool size
   * @param r
   *          the random number generator
   * @return the individual containing the best candidate solution
   *         (Definition D2.2) found
   * @param <G>
   *          the search space (Section 4.1)
   * @param <X>
   *          the problem space (Section 2.1)
   * @param <M>
   *          the model type
   */
  @SuppressWarnings("unchecked")
  public static final <G, X, M> Individual<G, X> eda(
      //
      final IObjectiveFunction<X> f,
      final INullarySearchOperation<M> newModel,//
      final IModelBuilder<M, G> buildModel,//
      final IModelSampler<M, G> sampleModel,
      final IGPM<G, X> gpm,//
      final ISelectionAlgorithm sel, final int ps, final int mps,
      final ITerminationCriterion term, final Random r) {

    Individual<G, X>[] pop, mate;
    Individual<G, X> p, best;
    M model;
    int i;

    best = new Individual<G, X>();
    best.v = Constants.WORST_FITNESS;
    pop = new Individual[ps];
    mate = new Individual[mps];

    model = newModel.create(r);

    // the basic loop of the Evolutionary Algorithm 28.1
    for (;;) {
      // for eachgeneration do...

      // for each individual in the population
      for (i = pop.length; (--i) >= 0;) {
        pop[i] = p = new Individual<G, X>();

        // sample the model
        p.g = sampleModel.sampleModel(model, r);

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

      // perform the selection step
      sel.select(pop, 0, pop.length, mate, 0, mate.length, r);

      // build the model for the next generation
      model = buildModel.buildModel(mate, 0, mate.length, model, r);
    }
  }

  /**
   * Invoke the estimation of distribution algorithm
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

    result.add(EDA.eda(this.getObjectiveFunction(),//
        this.getModelCreator(),//
        this.getModelBuilder(),//
        this.getModelSampler(),//
        this.getGPM(),//
        this.getSelectionAlgorithm(),//
        this.getPopulationSize(),//
        this.getMatingPoolSize(),//
        term,//
        r));
  }
}