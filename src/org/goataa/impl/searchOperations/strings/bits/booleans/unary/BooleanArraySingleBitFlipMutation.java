// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.bits.booleans.unary;

import java.util.Random;

import org.goataa.impl.searchOperations.UnarySearchOperation;
import org.goataa.spec.IUnarySearchOperation;

/**
 * A unary search operation which flips exactly one bit of a bit string, as
 * sketched in Fig. 29.2.a and discussed
 * in Section 29.3.2.1.
 *
 * @author Thomas Weise
 */
public final class BooleanArraySingleBitFlipMutation extends
    UnarySearchOperation<boolean[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the single bit flip mutation */
  public static final IUnarySearchOperation<boolean[]> BOOLEAN_ARRAY_SINGLE_BIT_FLIP_MUTATION = new BooleanArraySingleBitFlipMutation();

  /** Create a new bit-string single bit mutation operation */
  protected BooleanArraySingleBitFlipMutation() {
    super();
  }

  /**
   * The single-bit flip unary search operation (mutator) sketched in
   * Fig. 29.2.a and discussed in
   * Section 29.3.2.1.
   *
   * @param g
   *          the existing genotype in the search space from which a
   *          slightly modified copy should be created
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @Override
  public final boolean[] mutate(final boolean[] g, final Random r) {
    final boolean[] gnew;
    int i;

    gnew = g.clone();
    i = r.nextInt(gnew.length);
    gnew[i] = (!(gnew[i]));

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
    return "BA-SBF"; //$NON-NLS-1$
  }
}