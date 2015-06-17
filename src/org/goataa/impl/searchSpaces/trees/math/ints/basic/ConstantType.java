// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchSpaces.trees.math.ints.basic;

import java.util.Random;

import org.goataa.impl.searchSpaces.trees.Node;
import org.goataa.impl.searchSpaces.trees.NodeType;
import org.goataa.impl.searchSpaces.trees.NodeTypeSet;
import org.goataa.impl.searchSpaces.trees.math.ints.IntFunction;

/**
 * A constant type
 *
 * @author Thomas Weise
 */
public class ConstantType extends NodeType<Constant, IntFunction> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the globally shared default constant type */
  public static final ConstantType DEFAULT_CONSTANT_TYPE = new ConstantType();

  /** some good default values */
  private static final Constant[] GOOD_VALUES = new Constant[] {//
  new Constant(DEFAULT_CONSTANT_TYPE, 0),//
      new Constant(DEFAULT_CONSTANT_TYPE, 1),//
  };

  /**
   * Create a new constant type record
   */
  protected ConstantType() {
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
  public Constant instantiate(final Node<?>[] children, final Random r) {
    return GOOD_VALUES[r.nextInt(GOOD_VALUES.length)];
  }

  /**
   * Create a constant with the specified value
   *
   * @param val
   *          the value
   * @return the constant
   */
  public Constant instantiate(final int val) {
    int i;
    Constant[] v;

    v = GOOD_VALUES;
    for (i = v.length; (--i) >= 0;) {
      if (val == v[i].value) {
        return v[i];
      }
    }

    return new Constant(this, val);
  }

  /**
   * Convert a double to an integer
   *
   * @param val
   *          the value
   * @return the integer value
   */
  public static final int doubleToInt(final double val) {
    double dv;

    if (val < 0d) {
      dv = (val - 0.5d);
    } else {
      if (val > 0) {
        dv = (val + 0.5d);
      } else {
        return 0;
      }
    }

    if (dv >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    if (dv <= Integer.MIN_VALUE) {
      return Integer.MIN_VALUE;
    }
    return ((int) (dv));
  }

  /**
   * Create a constant with the specified value
   *
   * @param val
   *          the value
   * @return the constant
   */
  public Constant instantiate(final double val) {
    return this.instantiate(ConstantType.doubleToInt(val));
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

    return null;
  }
}