// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.utils;

import java.util.Arrays;
import java.util.Random;

import org.goataa.spec.IObjectiveFunction;

/**
 * An individual record for multi-objective optimization as discussed in
 * Section 3.3. We just extend the basic
 * individual record with the capability to store multiple objective
 * values.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class MOIndividual<G, X> extends Individual<G, X> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the objective values */
  public final double[] f;

  /**
   * Create a multi-objective individual
   *
   * @param fc
   *          the number of objective functions
   */
  public MOIndividual(final int fc) {
    super();
    this.f = new double[fc];
    // initialize the record with the worst objective values
    Arrays.fill(this.f, Constants.WORST_OBJECTIVE);
  }

  /**
   * Copy the values of another individual record to this record. This
   * method saves us from excessively creating new individual records.
   *
   * @param to
   *          the individual to copy
   */
  @SuppressWarnings("unchecked")
  @Override
  public void assign(final Individual<G, X> to) {
    final double[] d;
    super.assign(to);
    if (to instanceof MOIndividual) {
      d = (((MOIndividual) to).f);
      System.arraycopy(d, 0, this.f, 0, Math.min(d.length, this.f.length));
    }
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this individual record, i.e., the genotype, phenotype, and
   * fitness as well as the objective values.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  @Override
  public String getConfiguration(final boolean longVersion) {
    StringBuilder sb;

    sb = new StringBuilder();
    if (longVersion) {
      sb.append("objectives"); //$NON-NLS-1$
    } else {
      sb.append('f');
    }
    sb.append('=');
    TextUtils.toStringBuilder(this.f, sb);
    sb.append(',');
    sb.append(' ');
    sb.append(super.getConfiguration(longVersion));

    return sb.toString();
  }

  /**
   * Get the name of the multi-objective individual record
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  @Override
  public String getName(final boolean longVersion) {
    if (longVersion) {
      return super.getName(true);
    }
    return "mo-ind"; //$NON-NLS-1$
  }

  /**
   * Compute the objective values by using the given objective functions
   *
   * @param fs
   *          the objective functions
   * @param r
   *          the randomizer
   */
  public final void evaluate(final IObjectiveFunction<X>[] fs,
      final Random r) {
    int i;
    for (i = fs.length; (--i) >= 0;) {
      this.f[i] = fs[i].compute(this.x, r);
    }
  }
}