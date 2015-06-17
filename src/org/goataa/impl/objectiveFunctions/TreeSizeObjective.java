// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.objectiveFunctions;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.spec.IObjectiveFunction;

/**
 * An objective function minimizing tree sizes to be used in
 * multi-objective in Genetic Programming.
 *
 * @author Thomas Weise
 */
public class TreeSizeObjective extends ObjectiveFunction<Node<?>> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared instance of the tree size objective function */
  public static final IObjectiveFunction<Node<?>> TREE_SIZE_OBJECTIVE = new TreeSizeObjective(
      1);

  /** the minimum tree size */
  private final int min;

  /**
   * create a new tree-size objective function
   *
   * @param m
   *          the minimum tree size
   */
  public TreeSizeObjective(final int m) {
    super();
    this.min = Math.max(0, m);
  }

  /**
   * Compute the objective value, i.e., determine the utility of the
   * solution candidate x as specified in
   * Definition D2.3.
   *
   * @param x
   *          the phenotype to be rated
   * @param r
   *          the randomizer
   * @return the objective value of x, the lower the better (see
   *         Section 6.3.4)
   */
  @Override
  public double compute(final Node<?> x, final Random r) {
    if (x == null) {
      return (Integer.MAX_VALUE + 1l);
    }
    return Math.max(this.min, x.getWeight());
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
    return "size"; //$NON-NLS-1$
  }
}