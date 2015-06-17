// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.termination;

/**
 * The step limit termination criterion (see Point 2 in
 * Section 6.3.3) gets true after a maximum number
 * of steps has been exhausted. The steps are counted in terms of
 * invocations of the {@link #terminationCriterion()} method.
 *
 * @author Thomas Weise
 */
public final class StepLimit extends TerminationCriterion {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the step limit */
  private final int maxSteps;

  /** the number of remaining steps */
  private int remaining;

  /**
   * Create a new step limit termination criterion
   *
   * @param steps
   *          the number of steps
   */
  public StepLimit(final int steps) {
    this.maxSteps = steps;
  }

  /**
   * This method should be invoked after each search step, iteration, or
   * objective function evaluation. This method returns true after it has
   * been called exactly the number of times specified in the parameter
   * "steps" passed to the constructor minus 1. For example, if "1" is
   * specified in the constructor, the first call of this method will
   * return true. For "2", the second call will be true while the first
   * returns false.
   *
   * @return true after the specified steps are exhausted, false otherwise
   */
  @Override
  public final boolean terminationCriterion() {
    return (--this.remaining) <= 0;
  }

  /** Reset the termination criterion to make it useable more than once */
  @Override
  public void reset() {
    this.remaining = this.maxSteps;
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
    if (longVersion) {
      return "limit=" + String.valueOf(this.maxSteps); //$NON-NLS-1$
    }
    return String.valueOf(this.maxSteps);
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
      return super.getName(true);
    }
    return "sl"; //$NON-NLS-1$
  }
}