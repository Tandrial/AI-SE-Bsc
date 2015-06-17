// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations;

import java.util.Random;

import org.goataa.spec.IBinarySearchOperation;

/**
 * A simple class that allows to randomly choose between multiple different
 * recombination (binary) search operators (see
 * Definition D4.6) as given in
 * Definition D28.14.
 *
 * @param <G>
 *          the search space
 * @author Thomas Weise
 */
public class MultiRecombinator<G> extends BinarySearchOperation<G> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internally available operations */
  private final IBinarySearchOperation<G>[] o;

  /** the temporary array */
  private final int[] tmp;

  /** the permutation */
  private final int[] perm;

  /**
   * Create a new multi-recombinator
   *
   * @param ops
   *          the search operations
   */
  public MultiRecombinator(final IBinarySearchOperation<G>... ops) {
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
   * Choose a recombination operation randomly from the available
   * operators.
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final G recombine(final G p1, final G p2, final Random r) {
    final int[] a;
    final IBinarySearchOperation<G>[] ops;
    int i, c;
    G res;
    IBinarySearchOperation<G> ox;

    ops = this.o;
    a = this.tmp;
    c = a.length;
    System.arraycopy(this.perm, 0, a, 0, c);

    while (c > 0) {
      i = r.nextInt(c);
      ox = ops[a[i]];
      res = ox.recombine(p1, p2, r);
      if ((res == p1) && (res == p2)) {
        res = ox.recombine(p2, p1, r);
      }
      if ((res != null) && (res != p1) && (res != p2)) {
        return res;
      }
      a[i] = a[--c];
    }

    return super.recombine(p1, p2, r);
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
    return "MX"; //$NON-NLS-1$
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