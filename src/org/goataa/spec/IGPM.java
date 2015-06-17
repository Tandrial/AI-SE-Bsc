// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.util.Random;

/**
 * This interface provides the method for genotype-phenotype mappings as
 * discussed in Section 4.3.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public interface IGPM<G, X> extends IOptimizationModule {

  /**
   * This function carries out the genotype-phenotype mapping as defined in
   * Definition D4.11. In other words, it translates one genotype (an
   * element in the search space) to one element in the problem space,
   * i.e., a phenotype.
   *
   * @param g
   *          the genotype (Definition D4.2)
   * @param r
   *          a randomizer
   * @return the phenotype (see Definition D2.2)
   *         corresponding to the genotype g
   */
  public abstract X gpm(final G g, final Random r);

}