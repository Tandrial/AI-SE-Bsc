// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl;

import org.goataa.spec.IOptimizationModule;

/**
 * The base class for all objects involved in this implementation of
 * optimization. This class implements the interface given in
 * Listing 55.1.
 *
 * @author Thomas Weise
 */
public class OptimizationModule implements IOptimizationModule {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** Instantiate */
  protected OptimizationModule() {
    super();
  }

  /**
   * Get the name of the optimization module
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  public String getName(final boolean longVersion) {
    return this.getClass().getSimpleName();
  }

  /**
   * Get the full configuration which holds all the data necessary to
   * describe this object.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the full configuration
   */
  public String getConfiguration(final boolean longVersion) {
    return ""; //$NON-NLS-1$
  }

  /**
   * Get the string representation of this object, i.e., the name and
   * configuration.
   *
   * @param longVersion
   *          true if the long version should be returned, false if the
   *          short version should be returned
   * @return the string version of this object
   */
  public String toString(final boolean longVersion) {
    String s, t;
    int i, j;
    char[] ch;

    s = this.getName(longVersion);
    t = this.getConfiguration(longVersion);

    if ((t == null) || ((i = t.length()) <= 0)) {
      return s;
    }

    j = s.length();
    ch = new char[i + j + 2];
    s.getChars(0, j, ch, 0);
    ch[j] = '(';
    t.getChars(0, i, ch, ++j);
    ch[i + j] = ')';
    return String.valueOf(ch);
  }

  /**
   * Obtain the string representation of this object.
   *
   * @return the string representation of this object.
   */
  @Override
  public final String toString() {
    return this.toString(false);
  }
}