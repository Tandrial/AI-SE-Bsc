// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.bits.booleans.binary;

import java.util.Random;

import org.goataa.impl.searchOperations.BinarySearchOperation;
import org.goataa.spec.IBinarySearchOperation;

/**
 * A binary search operation which mixes the bits of two parent genotypes
 * sketched in Fig. 29.5.d and discussed
 * in Section 29.3.4.4.
 *
 * @author Thomas Weise
 */
public final class BooleanArrayUniformCrossover extends
    BinarySearchOperation<boolean[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the boolean array uniform crossover */
  public static final IBinarySearchOperation<boolean[]> BOOLEAN_ARRAY_UNIFORM_CROSSOVER = new BooleanArrayUniformCrossover();

  /** Create a new bit-string uniform crossover operation */
  protected BooleanArrayUniformCrossover() {
    super();
  }

  /**
   * The binary search operation which mixes the bits of two parent
   * genotypes sketched in Fig. 29.5.d
   * and discussed in Section 29.3.4.4.
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
  public final boolean[] recombine(final boolean[] p1, final boolean[] p2,
      final Random r) {
    final boolean[] gnew;
    int i;

    gnew = p1.clone();
    for (i = Math.min(gnew.length, p2.length); (--i) >= 0;) {
      if (r.nextBoolean()) {
        gnew[i] = p2[i];
      }
    }

    return gnew;
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
    return "BA-UX"; //$NON-NLS-1$
  }
}