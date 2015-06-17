// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.impl.gpms.IdentityMapping;
import org.goataa.impl.termination.TerminationCriterion;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.IGPM;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ISOOptimizationAlgorithm;
import org.goataa.spec.ITerminationCriterion;

/**
 * The default base class for implementations of the optimization algorithm
 * interface given in
 * Listing 55.10
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @param <IT>
 *          the individual type
 * @author Thomas Weise
 */
public class SOOptimizationAlgorithm<G, X, IT extends Individual<G, X>>
    extends OptimizationModule implements
    ISOOptimizationAlgorithm<G, X, IT> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the seed generator */
  private static final Random SEED = new Random(0l);

  /** the genotype-phenotype mapping */
  private IGPM<G, X> gpm;

  /** the termination criterion */
  private ITerminationCriterion tc;

  /** the objective function */
  private IObjectiveFunction<X> f;

  /** the internal randomizer */
  private final Random random;

  /** the seed */
  private long seed;

  /** should we use a random seed ? */
  private boolean randomSeed;

  /** the maximum number of solutions */
  private int maxs;

  /** Create a new optimization algorithm */
  @SuppressWarnings("unchecked")
  protected SOOptimizationAlgorithm() {
    super();
    this.random = new Random();
    this.gpm = ((IGPM) (IdentityMapping.IDENTITY_MAPPING));
    this.tc = TerminationCriterion.NEVER_TERMINATE;
    this.randomSeed = true;
    this.seed = SEED.nextLong();
    this.maxs = 1;
  }

  /** {@inheritDoc} */
  @Override
  public List<IT> call() {
    final List<IT> l;
    final ITerminationCriterion c;
    final Random r;

    l = new ArrayList<IT>();

    c = this.getTerminationCriterion();
    c.reset();

    r = this.random;
    if (this.randomSeed) {
      this.seed = SEED.nextLong();
    }
    r.setSeed(this.seed);

    this.call(r, c, l);

    return l;
  }

  /** {@inheritDoc} */
  @Override
  public void call(final Random r, final ITerminationCriterion term,
      final List<IT> result) {
    result.clear();
  }

  /** {@inheritDoc} */
  @Override
  public final void setGPM(final IGPM<G, X> agpm) {
    this.gpm = agpm;
  }

  /** {@inheritDoc} */
  @Override
  public final IGPM<G, X> getGPM() {
    return this.gpm;
  }

  /** {@inheritDoc} */
  @Override
  public void setTerminationCriterion(final ITerminationCriterion aterm) {
    this.tc = ((aterm != null) ? aterm
        : TerminationCriterion.NEVER_TERMINATE);
  }

  /** {@inheritDoc} */
  @Override
  public ITerminationCriterion getTerminationCriterion() {
    return this.tc;
  }

  /** {@inheritDoc} */
  @Override
  public void setObjectiveFunction(final IObjectiveFunction<X> af) {
    this.f = af;
  }

  /** {@inheritDoc} */
  @Override
  public final IObjectiveFunction<X> getObjectiveFunction() {
    return this.f;
  }

  /** {@inheritDoc} */
  @Override
  public final void setRandSeed(final long aseed) {
    this.randomSeed = false;
    this.seed = aseed;
  }

  /** {@inheritDoc} */
  @Override
  public final long getRandSeed() {
    return this.seed;
  }

  /** {@inheritDoc} */
  @Override
  public final void useRandomRandSeed() {
    this.randomSeed = true;
  }

  /** {@inheritDoc} */
  @Override
  public final boolean usesRandomRandSeed() {
    return this.randomSeed;
  }

  /** {@inheritDoc} */
  @Override
  public void setMaxSolutions(final int max) {
    this.maxs = ((max > 1) ? max : 1);
  }

  /** {@inheritDoc} */
  @Override
  public int getMaxSolutions() {
    return this.maxs;
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public String getConfiguration(final boolean longVersion) {
    StringBuilder sb;
    IOptimizationModule o;

    sb = new StringBuilder();

    o = this.getGPM();
    if (o != null) {
      if (longVersion || (!(o instanceof IdentityMapping))) {
        sb.append("gpm="); //$NON-NLS-1$
        sb.append(o.toString(longVersion));
      }
    }

    o = this.getObjectiveFunction();
    if (o != null) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append("f=");//$NON-NLS-1$
      sb.append(o.toString(longVersion));
    }

    o = this.getTerminationCriterion();
    if (o != null) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append(longVersion ? "terminationCriterion=" : //$NON-NLS-1$
          "tc=");//$NON-NLS-1$
      sb.append(o.toString(longVersion));
    }

    if (sb.length() > 0) {
      sb.append(',');
    }
    if (this.usesRandomRandSeed()) {
      sb.append(longVersion ? "fixedRandomSeed=" : //$NON-NLS-1$
          "frs=");//$NON-NLS-1$
      sb.append(this.getRandSeed());
    } else {
      sb.append(longVersion ? "randomSeed" : "r");//$NON-NLS-1$//$NON-NLS-2$
    }

    return sb.toString();
  }

  /** A simple wrapper to make this algorithm Runnable */
  public void run() {
    this.call();
  }
}