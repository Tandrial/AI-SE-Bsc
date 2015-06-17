// Copyright (c) 2011 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.permutation.crossover;

import java.util.Arrays;
import java.util.Random;

import org.goataa.impl.searchOperations.BinarySearchOperation;

/**
 * A simple crossover for integer permutations.
 *
 * @author Thomas Weise
 */
public class IntPermutationCrossover extends BinarySearchOperation<int[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the list of used values */
  private transient boolean[] used;

  /** Instantiate */
  public IntPermutationCrossover() {
    super();
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
  public int[] recombine(final int[] p1, final int[] p2, final Random r) {
    boolean[] us;
    int[] res;
    final int l;
    int i, cp, v;

    l = p1.length;
    us = this.used;
    if ((us == null) || (us.length < l)) {
      this.used = us = new boolean[l];
    }
    Arrays.fill(us, 0, l, false);

    res = new int[l];

    cp = (r.nextInt(l - 1) + 1);
    for (i = cp; (--i) >= 0;) {
      v = p1[i];
      us[v] = true;
      res[i] = v;
    }

    v = p1[cp - 1];
    for (i = l; (--i) >= 0;) {
      if (p2[i] == v) {
        break;
      }
    }

    while (cp < l) {
      while (us[v = p2[i % l]]) {
        i++;
      }
      us[v] = true;
      res[cp++] = v;
    }

    return res;
  }

}