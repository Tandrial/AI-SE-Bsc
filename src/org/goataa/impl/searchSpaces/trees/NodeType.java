// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees;

import java.util.Random;

import org.goataa.impl.OptimizationModule;

/**
 * The node type of a node has two main functions: 1) It describes which
 * types of children are allowed to occur at which position (if any), and
 * 2) it is used to instantiate nodes of the type. Notice that 1) is the
 * basic requirement for strongly-typed GP: We can specify which type of
 * node can occur as child of which other node and thus, build a complex
 * type system and pre-define node structures precisely. Point 2) somehow
 * reproduces the capabilities of Java's reflection system with the
 * extension of allowing us to perform some additional, randomized actions.
 * Matter of fact, with the class ReflectionNodeType, we defer the node
 * creation to Java's reflection mechanisms in cases where no randomized
 * instantiation actions are required.
 *
 * @param <NT>
 *          the specific node type
 * @param <CT>
 *          the child node type (i.e., the general type where NT is an
 *          instance of)
 * @author Thomas Weise
 */
public class NodeType<NT extends Node<CT>, CT extends Node<CT>> extends
    OptimizationModule {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the key counter */
  static int keyCounter = 0;

  /** no children */
  public static final NodeTypeSet<?>[] EMPTY_CHILDREN = new NodeTypeSet[0];

  /** a list of possible node types for each child */
  private final NodeTypeSet<CT>[] chTypes;

  /** a search key */
  final int key;

  /**
   * Create a new node type record
   *
   * @param ch
   *          the types of the possible chTypes
   */
  @SuppressWarnings("unchecked")
  public NodeType(final NodeTypeSet<CT>[] ch) {
    super();

    this.chTypes = (((ch != null) && (ch.length) > 0) ? ((NodeTypeSet[]) ch)
        : (NodeTypeSet[]) (EMPTY_CHILDREN));
    this.key = (keyCounter++);
  }

  /**
   * Get the number of chTypes of this node type
   *
   * @return the number of chTypes of this node type
   */
  public final int getChildCount() {
    return this.chTypes.length;
  }

  /**
   * Get the possible types for the chTypes at the specific index.
   *
   * @param index
   *          the child index
   * @return the possible types of that child
   */
  public final NodeTypeSet<CT> getChildTypes(final int index) {
    return this.chTypes[index];
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
  public NT instantiate(final Node<?>[] children, final Random r) {
    throw new UnsupportedOperationException();
  }

  /**
   * Create a new, slightly modified copy of the given node. This may come
   * in handy if a node represents a real constant or something. We would
   * then be able to randomly modify it which is better than replacing it
   * with a completely random node. If no useful way to modify the node in
   * a suitable way exists, just return the original-
   *
   * @param node
   *          the input node
   * @param r
   *          the randomizer
   * @return the modified node or the original value, if no modification is
   *         possible
   */
  public NT mutate(final NT node, final Random r) {
    return node;
  }

  /**
   * Does this node type describe a terminal node?
   *
   * @return true if this node type describes a terminal node, false
   *         otherwise
   */
  public final boolean isTerminal() {
    return (this.chTypes.length <= 0);
  }

}