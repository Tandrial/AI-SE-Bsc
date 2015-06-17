// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.termination;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.ITerminationCriterion;

/**
 * A base class for termination criteria
 *
 * @author Thomas Weise
 */
public class TerminationCriterion extends OptimizationModule implements
    ITerminationCriterion {

  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** never terminate */
  public static final ITerminationCriterion NEVER_TERMINATE = new TerminationCriterion();

  /** Create a new step limit termination criterion */
  public TerminationCriterion() {
    super();
  }

  /**
   * the termination criterion dummy routine
   *
   * @return always false
   */
  public boolean terminationCriterion() {
    return false;
  }

  /** Reset the termination criterion: does nothing here */
  public void reset() {
    //
  }

}