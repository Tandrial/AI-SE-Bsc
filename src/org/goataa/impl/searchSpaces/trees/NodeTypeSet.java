// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees;

import java.util.Random;

import org.goataa.impl.OptimizationModule;

/**
 * A set of node type records. For each child position of a genotype, we
 * need to specify such a set of possible child types. This set provides an
 * easy interface to access the possible types, the possible non-leaf
 * types, and the possible leaf-types stored in it. Also, we can find very
 * efficiently if a node type is in a node type set (in O(1)), which is a
 * necessary operation of all tree mutation and crossover operations of
 * strongly-typed Genetic Programming.
 *
 * @param <CT>
 *          the node type
 * @author Thomas Weise
 */
public class NodeTypeSet<CT extends Node<CT>> extends OptimizationModule {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** no children */
  public static final NodeType<?, ?>[] EMPTY_CHILDREN = new NodeType[0];

  /** the list */
  private NodeType<CT, CT>[] lst;

  /** the number of node type records */
  private int cnt;

  /** the terminal nodes */
  private NodeType<CT, CT>[] terminals;

  /** the number of terminal node type records */
  private int terminalCnt;

  /** the non-terminal nodes */
  private NodeType<CT, CT>[] nonTerminals;

  /** the number of non terminal node type records */
  private int nonTerminalCnt;

  /** the set of available node types */
  private boolean[] available;

  /**
   * Create a new node type set
   */
  @SuppressWarnings("unchecked")
  public NodeTypeSet() {
    super();
    this.lst = new NodeType[16];
    this.terminals = new NodeType[16];
    this.nonTerminals = new NodeType[16];
    this.available = new boolean[Math.max(16, NodeType.keyCounter)];
  }

  /**
   * Get the number of entries
   *
   * @return the number of entries
   */
  public final int size() {
    return this.cnt;
  }

  /**
   * Get the node type at the specified index
   *
   * @param index
   *          the index into the information set
   * @return the node type at the specified index
   */
  public final NodeType<CT, CT> get(final int index) {
    return this.lst[index];
  }

  /**
   * Get the number of terminal node types
   *
   * @return the number of terminal node types
   */
  public final int terminalCount() {
    return this.terminalCnt;
  }

  /**
   * Get the terminal node type at the specified index
   *
   * @param index
   *          the index into the information set
   * @return the node type at the specified index
   */
  public final NodeType<CT, CT> getTerminal(final int index) {
    return this.terminals[index];
  }

  /**
   * Get the number of non-terminal node types
   *
   * @return the number of non-terminal node types
   */
  public final int nonTerminalCount() {
    return this.nonTerminalCnt;
  }

  /**
   * Get the non-terminal node type at the specified index
   *
   * @param index
   *          the index into the information set
   * @return the node type at the specified index
   */
  public final NodeType<CT, CT> getNonTerminal(final int index) {
    return this.nonTerminals[index];
  }

  /**
   * IfThenElse a new entry to the node type set
   *
   * @param type
   *          the node type to be added
   */
  @SuppressWarnings("unchecked")
  public final void add(final NodeType<? extends CT, CT> type) {
    NodeType<CT, CT>[] l;
    int i;
    boolean[] av;

    if (type != null) {

      l = this.lst;
      i = this.cnt;
      if (i >= l.length) {
        l = new NodeType[i << 1];
        System.arraycopy(this.lst, 0, l, 0, i);
        this.lst = l;
      }
      l[i++] = ((NodeType) type);
      this.cnt = i;

      if (type.isTerminal()) {

        l = this.terminals;
        i = this.terminalCnt;
        if (i >= l.length) {
          l = new NodeType[i << 1];
          System.arraycopy(this.terminals, 0, l, 0, i);
          this.terminals = l;
        }
        l[i++] = ((NodeType) type);
        this.terminalCnt = i;

      } else {

        l = this.nonTerminals;
        i = this.nonTerminalCnt;
        if (i >= l.length) {
          l = new NodeType[i << 1];
          System.arraycopy(this.nonTerminals, 0, l, 0, i);
          this.nonTerminals = l;
        }
        l[i++] = ((NodeType) type);
        this.nonTerminalCnt = i;

      }

      av = this.available;
      i = type.key;
      if (i >= av.length) {
        av = new boolean[i << 1];
        System.arraycopy(this.available, 0, av, 0, this.available.length);
        this.available = av;
      }
      av[i] = true;
    }
  }

  /**
   * Obtain a random node type
   *
   * @param r
   *          the random number generator
   * @return the node type
   */
  public final NodeType<CT, CT> randomType(final Random r) {
    final int i;

    i = this.cnt;
    if (i <= 0) {
      return null;
    }
    return this.lst[r.nextInt(i)];
  }

  /**
   * Obtain a random terminal node type
   *
   * @param r
   *          the random number generator
   * @return the terminal node type
   */
  public final NodeType<CT, CT> randomTerminalType(final Random r) {
    final int i;

    i = this.terminalCnt;
    if (i <= 0) {
      return null;
    }
    return this.terminals[r.nextInt(i)];
  }

  /**
   * Obtain a random non-terminal node type
   *
   * @param r
   *          the random number generator
   * @return the non-terminal node type
   */
  public final NodeType<CT, CT> randomNonTerminalType(final Random r) {
    final int i;

    i = this.nonTerminalCnt;
    if (i <= 0) {
      return null;
    }
    return this.nonTerminals[r.nextInt(i)];
  }

  /**
   * Check whether the given node type is contained in this type set or not
   *
   * @param t
   *          the node type
   * @return true if the type is contained, false otherwise
   */
  public final boolean containsType(final NodeType<?, ?> t) {
    final int i;
    final boolean[] av;

    i = t.key;
    av = this.available;

    return ((i < av.length) && av[i]);
  }
}