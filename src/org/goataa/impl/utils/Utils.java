/*
 * Copyright (c) 2010 Thomas Weise
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package org.goataa.impl.utils;

import java.util.Arrays;

/**
 * Some other utilities
 *
 * @author Thomas Weise
 */
public class Utils {

  /**
   * Check whether two objects are equal
   *
   * @param a
   *          the first object
   * @param b
   *          the second object
   * @return true only if the two objects are equal, false otherwise
   */
  public static final boolean equals(final Object a, final Object b) {
    Class<?> c;

    if (a == b) {
      return true;
    }
    if ((a == null) || (b == null)) {
      return false;
    }

    c = a.getClass();
    if ((c == b.getClass()) && (c.isArray())) {
      if (c == byte[].class) {
        return Arrays.equals(((byte[]) a), ((byte[]) b));
      }
      if (c == short[].class) {
        return Arrays.equals(((short[]) a), ((short[]) b));
      }
      if (c == int[].class) {
        return Arrays.equals(((int[]) a), ((int[]) b));
      }
      if (c == long[].class) {
        return Arrays.equals(((long[]) a), ((long[]) b));
      }
      if (c == char[].class) {
        return Arrays.equals(((char[]) a), ((char[]) b));
      }
      if (c == boolean[].class) {
        return Arrays.equals(((boolean[]) a), ((boolean[]) b));
      }
      if (c == float[].class) {
        return Arrays.equals(((float[]) a), ((float[]) b));
      }
      if (c == double[].class) {
        return Arrays.equals(((double[]) a), ((double[]) b));
      }
      return Arrays.deepEquals(((Object[]) a), ((Object[]) b));
    }

    return (a.equals(b));
  }

  /** Perform the garbage collection */
  public static final void gc() {
    final Runtime r;
    long nl, l;

    r = Runtime.getRuntime();
    nl = r.freeMemory();
    do {
      l = nl;
      r.gc();
      r.runFinalization();
      r.gc();
      nl = r.freeMemory();
    } while (nl < l);
  }
}