// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints;

import org.goataa.impl.searchSpaces.trees.math.MathContext;

/**
 * The algorithm execution context
 *
 * @author Thomas Weise
 */
public abstract class Context extends MathContext {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * Create a new context
   *
   * @param steps
   *          the maximum number of allowed steps
   */
  protected Context(final int steps) {
    super(steps);
  }

  /**
   * Initialize the context for beginning a new execution block inside a
   * program
   */
  public void beginBlock() {
    //
  }

  /**
   * Finalized the context after finishing the execution of a block inside
   * a program
   */
  public void endBlock() {
    //
  }

  /**
   * Read the value of a specific memory cell
   *
   * @param address
   *          the memory cell's address
   * @return the value of a specific memory cell
   */
  public abstract int read(final int address);

  /**
   * Write a value to a specific memory cell
   *
   * @param address
   *          the memory cell's address
   * @param value
   *          the value to be written
   */
  public abstract void write(final int address, final int value);

  /** Commit any pending operation in the context or the memory */
  public void commit() {//
  }
}