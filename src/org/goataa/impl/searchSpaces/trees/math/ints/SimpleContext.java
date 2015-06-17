// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints;

import java.util.Arrays;

/**
 * A simple memory context
 *
 * @author Thomas Weise
 */
public class SimpleContext extends Context {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the memory array */
  private final int[] mem;

  /**
   * Create a new simple memory context
   *
   * @param steps
   *          the maximum number of allowed steps
   * @param memSize
   *          the memory size
   */
  public SimpleContext(final int steps, final int memSize) {
    super(steps);
    this.mem = new int[memSize];
  }

  /**
   * Read the value of a specific memory cell
   *
   * @param address
   *          the memory cell's address
   * @return the value of a specific memory cell
   */
  @Override
  public final int read(final int address) {
    return this.mem[address];
  }

  /**
   * Write a value to a specific memory cell
   *
   * @param address
   *          the memory cell's address
   * @param value
   *          the value to be written
   */
  @Override
  public final void write(final int address, final int value) {
    this.mem[address] = value;
  }

  /** Initialize the context for a new program execution */
  @Override
  public void beginProgram() {
    super.beginProgram();
    Arrays.fill(this.mem, 0);
  }

  /**
   * Obtain the memory size
   *
   * @return the memory size
   */
  @Override
  public final int getMemorySize() {
    return this.mem.length;
  }

}