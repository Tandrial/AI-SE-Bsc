// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real;

import java.util.Arrays;

import org.goataa.impl.searchSpaces.trees.math.MathContext;

/**
 * The real context
 *
 * @author Thomas Weise
 */
public class RealContext extends MathContext {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the variables */
  private final double[] variables;

  /**
   * Create a new real context
   *
   * @param maxSteps
   *          the maximum number of allowed steps
   * @param varSize
   *          the number of variables
   */
  public RealContext(final int maxSteps, final int varSize) {
    super(maxSteps);
    this.variables = new double[varSize];
  }

  /**
   * Write a given value to the specified address
   *
   * @param address
   *          the address
   * @param value
   *          the value
   */
  public final void write(final int address, final double value) {
    this.variables[address] = value;
  }

  /**
   * Read a memory cell
   *
   * @param address
   *          the address
   * @return the value read
   */
  public final double read(final int address) {
    return this.variables[address];
  }

  /** begin the program */
  @Override
  public void beginProgram() {
    super.beginProgram();
    Arrays.fill(this.variables, 0d);
  }

  /**
   * Copy the given array to the memory
   *
   * @param d
   *          the array
   */
  public void copy(final double[] d) {
    int m;

    m = Math.min(d.length, this.variables.length);
    System.arraycopy(d, 0, this.variables, 0, m);
  }

  /**
   * Obtain the memory size
   *
   * @return the memory size
   */
  @Override
  public final int getMemorySize() {
    return this.variables.length;
  }
}