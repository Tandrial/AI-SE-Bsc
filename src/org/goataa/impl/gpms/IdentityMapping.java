// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.gpms;

import java.util.Random;

import org.goataa.spec.IGPM;

/**
 * This class provides an identity genotype-phenotype mapping, the simplest
 * possible variant of genotype-phenotype mappings (see
 * Section 4.3). This mapping can only
 * be applied if the search space (see Section 4.1) and
 * the problem space (see Section 2.1) are identical.
 *
 * @param <S>
 *          the type which is both, the search and the problem space
 * @author Thomas Weise
 */
public final class IdentityMapping<S> extends GPM<S, S> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared default instance of the identity mapping */
  public static final IGPM<?, ?> IDENTITY_MAPPING = new IdentityMapping<Object>();

  /** Create a new instance of the identity mapping class */
  protected IdentityMapping() {
    super();
  }

  /**
   * Perform the identity mapping: the genotype (Definition D4.2)
   * equals the phenotype (Definition D2.2).
   *
   * @param g
   *          the genotype
   * @param r
   *          the randomizer
   * @return the phenotype which equals genotype g
   */
  @Override
  public final S gpm(final S g, final Random r) {
    return g;
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
    return "I"; //$NON-NLS-1$
  }
}