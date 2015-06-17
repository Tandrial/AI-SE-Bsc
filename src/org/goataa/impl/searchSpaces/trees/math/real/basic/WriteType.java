// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.basic;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A write type
 *
 * @author Thomas Weise
 */
public class WriteType extends NodeType<Write, RealFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the number of variables */
  private final int varC;

  /**
   * Create a new variable type record
   *
   * @param ch
   *          the types of the possible chTypes
   * @param varc
   *          the variable count
   */
  public WriteType(final int varc, final NodeTypeSet<RealFunction>[] ch) {
    super(ch);
    this.varC = Math.max(0, varc);
  }

  /**
   * Instantiate a node
   *
   * @param children
   *          a given set of children
   * @param r
   *          the randomizer
   * @return the new node
   */
  @Override
  public Write instantiate(final Node<?>[] children, final Random r) {
    return new Write(children, this, r.nextInt(this.varC));
  }
}