// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math;

import org.goataa.impl.OptimizationModule;

/**
 * A context for mathematical computation
 *
 * @author Thomas Weise
 */
public abstract class MathContext extends OptimizationModule {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the maximum steps */
  private final int maxSteps;

  /** the remaining steps */
  private int remaining;

  /** the error flag */
  private boolean error;

  /**
   * the base class for mathematical contexts
   *
   * @param steps
   *          the maximum steps
   */
  protected MathContext(final int steps) {
    super();
    this.maxSteps = ((steps > 0) ? steps : Integer.MAX_VALUE);
  }

  /** Initialize the context for a new program execution */
  public void beginProgram() {
    this.remaining = this.maxSteps;
    this.error = false;
  }

  /** Finalize the context after a program execution */
  public void endProgram() {
    //
  }

  /**
   * Request permission to perform a single execution step
   *
   * @return true if and only if the step can be performed, false otherwise
   */
  public final boolean step() {
    if (this.error) {
      return false;
    }
    if ((this.remaining--) > 0) {
      return true;
    }
    this.error = true;
    return false;
  }

  /**
   * Get the maximum number of allowed steps
   *
   * @return the maximum number of allowed steps
   */
  public final int getMaxSteps() {
    return this.maxSteps;
  }

  /**
   * Get the number of execution steps performed by the program
   *
   * @return the number of execution steps performed by the program
   */
  public final int getPerformedSteps() {
    return (this.maxSteps - Math.max(0, this.remaining));
  }

  /** Notify the context that an error occured during the computation */
  public final void error() {
    this.error = true;
  }

  /**
   * Check whether an error occured during the computation
   *
   * @return true if an error occured, false otherwise
   */
  public final boolean hasError() {
    return this.error;
  }

  /**
   * Obtain the memory size
   *
   * @return the memory size
   */
  public abstract int getMemorySize();
}