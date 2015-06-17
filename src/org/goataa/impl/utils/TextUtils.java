// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.utils;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A utility class for text
 *
 * @author Thomas Weise
 */
public class TextUtils {

  /** the newline string */
  public static final String NEWLINE = System
      .getProperty("line.separator"); //$NON-NLS-1$

  /**
   * Print an object to a string builder while handling arrays correctly
   *
   * @param o
   *          the object
   * @param sb
   *          the string builder
   */
  public static final void toStringBuilder(final Object o,
      final StringBuilder sb) {
    int i, j;
    String s;

    if (o == null) {
      sb.append((Object) null);
      return;
    }

    if (o.getClass().isArray()) {

      sb.append('[');

      i = Array.getLength(o);
      for (j = 0; j < i; j++) {
        if (j > 0) {
          sb.append(',');
          sb.append(' ');
        }
        toStringBuilder(Array.get(o, j), sb);
      }

      sb.append(']');
      return;
    }

    if (o instanceof String) {
      sb.append('"');
      s = ((String) o);
      i = s.length();
      for (j = 0; j < i; j++) {
        escapeCharToStringBuilder(s.charAt(j), sb);
      }
      sb.append('"');
      return;
    }

    if (o instanceof Character) {
      sb.append('\'');
      escapeCharToStringBuilder(((Character) o).charValue(), sb);
      sb.append('\'');
    }

    sb.append(o.toString());
  }

  /**
   * Print a characte to a string builder while escaping special characters
   *
   * @param ch
   *          the character
   * @param sb
   *          the string builder
   */
  public static final void escapeCharToStringBuilder(final char ch,
      final StringBuilder sb) {
    if ((ch < 32) || (ch > 127)) {
      sb.append('\\');
      sb.append('u');
      sb.append(Integer.toHexString(ch));
    } else {
      sb.append(ch);
    }
  }

  /**
   * Convert an object to a string while handling arrays correctly
   *
   * @param o
   *          the object
   * @return the string
   */
  public static final String toString(final Object o) {
    StringBuilder sb;
    sb = new StringBuilder();
    toStringBuilder(o, sb);
    return sb.toString();
  }

  /** the internal formatter */
  private static final NumberFormat FORMATTER = new DecimalFormat(
      "0.00E00", new DecimalFormatSymbols(Locale.US)); //$NON-NLS-1$

  /**
   * Format a given number for output
   *
   * @param v
   *          the number
   * @return the corresponding string
   */
  public static final String formatNumberSameWidth(final double v) {
    return FORMATTER.format(v);
  }

  /**
   * Format a given number for output
   *
   * @param v
   *          the number
   * @return the corresponding string
   */
  public static final String formatNumber(final double v) {
    String s1, s2;
    int i;

    s1 = String.valueOf(v);

    for (i = s1.length(); (--i) >= 0;) {
      if (s1.charAt(i) != '0') {
        break;
      }
    }
    if (i >= 0) {
      if (s1.charAt(i) == '.') {
        s1 = s1.substring(0, i);
      } else {
        if (s1.indexOf('.') < i) {
          s1 = s1.substring(0, i + 1);
        }
      }
    }

    s2 = TextUtils.formatNumberSameWidth(v);
    if (s2.endsWith("E00")) { //$NON-NLS-1$
      s2 = s2.substring(0, s2.length() - 3);
    }

    return (s1.length() <= s2.length()) ? s1 : s2;
  }
}