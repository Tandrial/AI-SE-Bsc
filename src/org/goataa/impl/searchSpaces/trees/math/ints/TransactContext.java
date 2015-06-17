// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints;

import java.util.Arrays;

/**
 * A transactional memory context.
 *
 * @author Thomas Weise
 */
public class TransactContext extends Context {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the read array */
  private final int[][] mr;

  /** the write array */
  private final int[][] mw;

  /** the depth */
  private int depth;

  /**
   * Create a new simple memory context
   *
   * @param steps
   *          the maximum number of allowed steps
   * @param memSize
   *          the memory size
   * @param maxDepth
   *          the maximum depth
   */
  public TransactContext(final int steps, final int memSize,
      final int maxDepth) {
    super(steps);
    this.mr = new int[maxDepth][memSize];
    this.mw = new int[maxDepth][memSize];
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
    return this.mr[this.depth][address];
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
    this.mw[this.depth][address] = value;
  }

  /** Initialize the context for a new program execution */
  @Override
  public void beginProgram() {
    super.beginProgram();
    Arrays.fill(this.mr[0], 0);
    Arrays.fill(this.mw[0], 0);
    this.depth = 0;
  }

  /** Commit the memory */
  @Override
  public final void commit() {
    System.arraycopy(this.mw[this.depth], 0, this.mr[this.depth], 0,
        this.mw[this.depth].length);
    super.commit();
  }

  /**
   * Obtain the memory size
   *
   * @return the memory size
   */
  @Override
  public final int getMemorySize() {
    return this.mr[0].length;
  }

  /**
   * Initialize the context for beginning a new execution block inside a
   * program
   */
  @Override
  public final void beginBlock() {
    final int idx, oi, l;

    super.beginBlock();

    oi = this.depth;
    idx = (oi + 1);

    if (idx < this.mr.length) {
      l = this.mr[oi].length;
      System.arraycopy(this.mr[oi], 0, this.mr[idx], 0, l);
      System.arraycopy(this.mw[oi], 0, this.mw[idx], 0, l);

      this.depth = idx;
    } else {
      this.error();
    }
  }

  /** Finalize the context after a program execution */
  @Override
  public final void endProgram() {
    while (this.depth > 0) {
      this.endBlock();
    }
    System.arraycopy(this.mw[0], 0, this.mr[0], 0, this.mw[0].length);
    super.endProgram();
  }

  /**
   * Finalized the context after finishing the execution of a block inside
   * a program
   */
  @Override
  public final void endBlock() {
    final int[] cp;
    final int idx;

    if (this.depth > 0) {
      cp = this.mw[this.depth];
      idx = (--this.depth);
      System.arraycopy(cp, 0, this.mw[idx], 0, cp.length);
      System.arraycopy(cp, 0, this.mr[idx], 0, cp.length);
    }
    super.endBlock();
  }

}