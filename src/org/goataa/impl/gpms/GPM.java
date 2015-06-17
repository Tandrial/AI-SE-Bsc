// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.gpms;

import java.util.Random;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IGPM;

/**
 * A base class for genotype-phenotype mappings
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class GPM<G, X> extends OptimizationModule implements IGPM<G, X> {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** Create a new instance of the GPM base class */
  protected GPM() {
    super();
  }

  /**
   * perform the mapping
   *
   * @param g
   *          the genotype
   * @param r
   *          a randomizer
   * @return the phenotype which equals genotype g (here null)
   */
  public X gpm(final G g, final Random r) {
    return null;
  }
}