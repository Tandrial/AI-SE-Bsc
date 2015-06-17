// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.spec.IUnarySearchOperation;

/**
 * A simple class that allows to randomly choose between multiple different
 * mutators, i.e., unary search operators (see
 * Definition D4.6).
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class MultiMutator<G> extends UnarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internally available operations */
  private final IUnarySearchOperation<G>[] o;

  /** the temporary array */
  private final int[] tmp;

  /** the permutation */
  private final int[] perm;

  /**
   * Create a new multi-mutator
   *
   * @param ops
   *          the search operations
   */
  public MultiMutator(final IUnarySearchOperation<G>... ops) {
    super();

    int[] x;
    int l;

    this.o = ops;
    l = ops.length;
    this.tmp = new int[l];
    this.perm = x = new int[l];
    for (; (--l) >= 0;) {
      x[l] = l;
    }
  }

  /**
   * This operation tries to mutate a genotype by randomly applying one of
   * its sub-mutators. If a sub-mutator is not able to provide a new
   * genotype, i.e., returns null or its input, then we try the next
   * sub-mutator, and so on.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final G mutate(final G g, final Random r) {
    final int[] a;
    final IUnarySearchOperation<G>[] ops;
    int i, c;
    G res;

    ops = this.o;
    a = this.tmp;
    c = a.length;
    System.arraycopy(this.perm, 0, a, 0, c);

    while (c > 0) {
      i = r.nextInt(c);
      res = ops[a[i]].mutate(g, r);
      if ((res != null) && (res != g)) {
        return res;
      }
      a[i] = a[--c];
    }

    return super.mutate(g, r);
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
    return "MM"; //$NON-NLS-1$
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
    int i;

    sb = new StringBuilder();
    for (i = 0; i < this.o.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(this.o[i].toString(longVersion));
    }

    return sb.toString();
  }
}