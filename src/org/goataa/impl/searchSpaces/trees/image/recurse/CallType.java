// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.image.recurse;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * The type for the singleton call instruction
 *
 * @author Thomas Weise
 */
public class CallType extends NodeType<Call, RealFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared default call type */
  public static final CallType DEFAULT_CALL_TYPE = new CallType();

  /** Create a new constant type record */
  private CallType() {
    super(null);
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
  public final Call instantiate(final Node<?>[] children, final Random r) {
    return Call.CALL;
  }
}