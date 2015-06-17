// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.trees.binary;

import java.util.Random;

import org.goataa.impl.searchOperations.trees.TreeOperation;
import org.goataa.impl.searchOperations.trees.TreePath;
import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.spec.IBinarySearchOperation;

/**
 * A simple recombination operation for a given tree which implants a
 * randomly chosen subtree from one parent into the other parent, thereby
 * replacing a randomly picked node in the second parent. This operation
 * basically proceeds according to the ideas discussed in
 * Section 31.3.5 with the extension that
 * it also respects the type system of the strongly-typed GP system.
 *
 * @author Thomas Weise
 */
public class TreeRecombination extends TreeOperation implements
    IBinarySearchOperation<Node<?>> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the internal path 1 */
  private final TreePath<?> pa1;

  /** the internal path 2 */
  private final TreePath<?> pa2;

  /**
   * Create a new tree mutationoperation
   *
   * @param md
   *          the maximum tree depth
   */
  @SuppressWarnings("unchecked")
  public TreeRecombination(final int md) {
    super(md);
    this.pa1 = new TreePath();
    this.pa2 = new TreePath();
  }

  /**
   * This is the binary search operation. It takes two existing genotypes
   * p1 and p2 (see Definition D4.2) from the genome and produces
   * one new element in the search space. There are two basic assumptions
   * about this operator: 1) Its input elements are good because they have
   * previously been selected. 2) It is somehow possible to combine these
   * good traits and hence, to obtain a single individual which unites them
   * and thus, has even better overall qualities than its parents. The
   * original underlying idea of this operation is the
   * "Building Block Hypothesis" (see
   * Section 29.5.5) for which, so far, not much
   * evidence has been found. The hypothesis
   * "Genetic Repair and Extraction" (see Section 29.5.6)
   * has been developed as an alternative to explain the positive aspects
   * of binary search operations such as recombination.
   *
   * @param p1
   *          the first "parent" genotype
   * @param p2
   *          the second "parent" genotype
   * @param r
   *          the random number generator
   * @return a new genotype
   */
  @SuppressWarnings("unchecked")
  public Node<?> recombine(final Node<?> p1, final Node<?> p2,
      final Random r) {
    TreePath pt1, pt2;
    int trials, i;
    Node<?> e1, e2;

    pt1 = this.pa1;
    pt2 = this.pa2;

    outer: for (trials = 100; trials > 0; trials--) {
      pt1.randomPath(p1, r);
      pt2.randomPath(p2, r);

      i = pt1.size();
      if (i <= 1) {
        continue outer;
      }

      e2 = pt2.get(pt2.size() - 1);
      i -= 2;
      e1 = pt1.get(i);
      if (((i + e2.getHeight() + 1) < this.maxDepth)
          && e1.getType().getChildTypes(pt1.getChildIndex(i))
              .containsType(e2.getType())) {
        return pt1.replaceEnd(e2);
      }
    }

    return (r.nextBoolean() ? p1 : p2);
  }
}