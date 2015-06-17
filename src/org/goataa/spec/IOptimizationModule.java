// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.spec;

import java.io.Serializable;

/**
 * A simple basic interface common to all modules of optimization
 * algorithms that we provide in this package. It ensures that all objects
 * are serializable, i.e., can be written to an ObjectOutputStream and read
 * from an ObjectInputStream, for instance. It furthermore provides some
 * simple methods for converting an object to representative strings.
 *
 * @author Thomas Weise
 */
public interface IOptimizationModule extends Serializable {

  /**
   * Get the name of the optimization module
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  public abstract String getName(final boolean longVersion);

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  public abstract String getConfiguration(final boolean longVersion);

  /**
   * Get the string representation of this object, i.e., the name and
   * configuration.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the string version of this object
   */
  public abstract String toString(final boolean longVersion);
}