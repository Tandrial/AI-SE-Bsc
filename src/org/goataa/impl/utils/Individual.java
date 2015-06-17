// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.utils;

import org.goataa.impl.OptimizationModule;

/**
 * An individual record, as defined in Definition D4.18. Such a
 * record always holds one genotype from the search space and the
 * corresponding phenotype from the problem space. In this implementation,
 * we also store a fitness value v which corresponds to the objective value
 * in a single-objective problem. This way, we do not need to evaluate the
 * objective function more than once per individual. This makes sense since
 * it would (in most cases) yield the same result anyway and only waste CPU
 * time.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class Individual<G, X> extends OptimizationModule {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the genotype as defined in Definition D4.2 */
  public G g;

  /**
   * the corresponding phenotype as defined in
   * Definition D2.2
   */
  public X x;

  /**
   * the fitness value, as defined in Definition D5.1 which
   * corresponds to the objective value in single-objective optimization
   * problems
   */
  public double v;

  /** The constructor creates a new individual record */
  public Individual() {
    super();
    // initialize the record with the worst fitness
    this.v = Constants.WORST_FITNESS;
  }

  /**
   * Copy the values of another individual record to this record. This
   * method saves us from excessively creating new individual records.
   *
   * @param to
   *          the individual to copy
   */
  public void assign(final Individual<G, X> to) {
    this.g = to.g;
    this.x = to.x;
    this.v = to.v;
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this individual record, i.e., the genotype, phenotype, and
   * fitness.
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
      sb.append("fitness"); //$NON-NLS-1$
    } else {
      sb.append('v');
    }
    sb.append('=');
    sb.append(this.v);

    if (longVersion) {
      sb.append(", genotype="); //$NON-NLS-1$
    } else {
      sb.append(", g=");}//$NON-NLS-1$
    TextUtils.toStringBuilder(this.g, sb);

    if (longVersion) {
      sb.append(", phenotype="); //$NON-NLS-1$
    } else {
      sb.append(", x=");}//$NON-NLS-1$
    TextUtils.toStringBuilder(this.x, sb);

    return sb.toString();
  }

  /**
   * Get the name of the individual record
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
    return "ind"; //$NON-NLS-1$
  }
}