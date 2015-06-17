// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.bits.booleans.nullary;

import java.util.Random;

import org.goataa.impl.searchOperations.strings.FixedLengthStringCreation;

/**
 * An uniform bit string creator which builds random bit strings as defined
 * in Algorithm 29.1.
 *
 * @author Thomas Weise
 */
public final class BooleanArrayUniformCreation extends
    FixedLengthStringCreation<boolean[]> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /**
   * The uniform boolean string creator
   *
   * @param dim
   *          the dimension of the search space
   */
  public BooleanArrayUniformCreation(final int dim) {
    super(dim);
  }

  /**
   * This is a nullary search operation which creates strings of boolean
   * values
   *
   * @param r
   *          the random number generator
   * @return a new genotype (see Definition D4.2)
   */
  @Override
  public boolean[] create(final Random r) {
    boolean[] bs;
    int i;

    i = this.n;
    bs = new boolean[i];

    for (; (--i) >= 0;) {
      bs[i] = r.nextBoolean();
    }

    return bs;
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
    return "BA-U"; //$NON-NLS-1$
  }
}