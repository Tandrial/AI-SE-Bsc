// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.real.basic;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.real.RealFunction;

/**
 * A constant type
 *
 * @author Thomas Weise
 */
public class ConstantType extends NodeType<Constant, RealFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared default constant type */
  public static final ConstantType DEFAULT_CONSTANT_TYPE = new ConstantType();

  /** some good default values */
  private static final Constant[] GOOD_VALUES = new Constant[] {//
  new Constant(DEFAULT_CONSTANT_TYPE, -1d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 1d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 2d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 3d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 4d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 5d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 0.5d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 1d / 3d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 0.25d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 0.2d),//
      new Constant(DEFAULT_CONSTANT_TYPE, 0.5d * Math.PI),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.PI),//
      new Constant(DEFAULT_CONSTANT_TYPE, 2d * Math.PI),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.E),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.sqrt(2d)),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.sqrt(3d)),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.sqrt(5d)),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.sqrt(10d)),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.sqrt(Math.PI)),//
      new Constant(DEFAULT_CONSTANT_TYPE, Math.sqrt(Math.E)) //
  };

  /** Create a new constant type record */
  protected ConstantType() {
    super(null);
  }

  /**
   * Modify a constant value
   *
   * @param orig
   *          the original value
   * @param r
   *          the randomizer
   * @return the new value
   */
  protected static final double modify(final double orig, final Random r) {
    double scale, v;

    scale = Math.abs(orig);
    if (scale <= 0d) {
      scale = 1e-10;
    }

    do {
      v = (r.nextGaussian() * scale * Math.exp(r.nextGaussian() - 4));
    } while (v == 0d);

    return (orig + v);
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
  public Constant instantiate(final Node<?>[] children, final Random r) {
    Constant c;
    c = GOOD_VALUES[r.nextInt(GOOD_VALUES.length)];
    if (r.nextBoolean()) {
      return c;
    }
    return new Constant(this, modify(c.value, r));
  }

  /**
   * Instantiate a new constant
   *
   * @param v
   *          the value of the constant
   * @return the constant
   */
  public final Constant instantiate(final double v) {
    int i;
    Constant c;
    for (i = GOOD_VALUES.length; (--i) >= 0;) {
      c = GOOD_VALUES[i];
      if (c.value == v) {
        return c;
      }
    }
    return new Constant(this, v);
  }

  /**
   * Slightly modify a constant.
   *
   * @param node
   *          the input node
   * @param r
   *          the randomizer
   * @return the modified node or the original value, if no modification is
   *         possible
   */
  @Override
  public Constant mutate(final Constant node, final Random r) {
    return this.instantiate(modify(node.value, r));
  }

  /**
   * Try to get a constant factory
   *
   * @param a
   *          the first node
   * @param b
   *          the second node
   * @param s
   *          the node type set
   * @return the constant factory
   */
  public static final ConstantType getConstantType(final Node<?> a,
      final Node<?> b, final NodeTypeSet<?> s) {
    NodeType<?, ?> t;
    int i;

    if (a != null) {
      t = a.getType();
      if (t instanceof ConstantType) {
        return ((ConstantType) t);
      }
    }

    if (b != null) {
      t = b.getType();
      if (t instanceof ConstantType) {
        return ((ConstantType) t);
      }
    }

    if (s != null) {
      for (i = s.size(); (--i) >= 0;) {
        t = s.get(i);
        if (t instanceof ConstantType) {
          return ((ConstantType) t);
        }
      }
    }

    return DEFAULT_CONSTANT_TYPE;
  }
}