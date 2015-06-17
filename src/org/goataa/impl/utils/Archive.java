// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.utils;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;

import org.goataa.impl.comparators.Lexicographic;
import org.goataa.impl.comparators.Pareto;
import org.goataa.spec.IIndividualComparator;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ISelectionAlgorithm;

/**
 * An archive of non-dominated individuals as discussed in
 * Section 28.6 for multi-objective
 * optimization Section 3.3.
 *
 * @param <G>
 *          the search space (genome, Section 4.1)
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class Archive<G, X> extends AbstractList<MOIndividual<G, X>>
    implements IOptimizationModule {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the maximum size */
  private final int maxSize;

  /** the list */
  @SuppressWarnings("unchecked")
  private MOIndividual[] lst;

  /** the individual comparator */
  private final IIndividualComparator ic;

  /** the replacement index */
  private int replaceIdx;

  /** the epsilons */
  private transient double[] epsilon;

  /** the number of individuals in the list */
  private int cnt;

  /** the copy index */
  private int copyIndex;

  /** Create an archive with the default maximum size 100 */
  public Archive() {
    this(100, null);
  }

  /**
   * Create an archive with the given maximum size
   *
   * @param ms
   *          the maximum size, -1 for unrestrained
   * @param i
   *          the individual comparator to use
   */
  public Archive(final int ms, final IIndividualComparator i) {
    super();

    boolean b;
    int is;

    b = ((ms > 0) && (ms < Integer.MAX_VALUE));
    if (b) {
      is = Math.min(1024, ms);
    } else {
      is = 1024;
    }
    this.maxSize = (b ? ms : Integer.MAX_VALUE);
    this.lst = new MOIndividual[is];

    this.ic = ((i != null) ? i : Pareto.PARETO_COMPARATOR);
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
    return (longVersion ? "archive" : "arch"); //$NON-NLS-1$ //$NON-NLS-2$
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
    StringBuilder sb;

    sb = new StringBuilder();
    if ((this.maxSize > 0) && (this.maxSize < Integer.MAX_VALUE)) {
      sb.append("maxLen="); //$NON-NLS-1$
      sb.append(this.maxSize);
    }

    if (this.ic != null) {
      if (sb.length() > 0) {
        sb.append(", ");//$NON-NLS-1$
      }
      sb.append("cmp=");//$NON-NLS-1$
      sb.append(this.ic.toString(longVersion));
    }

    return sb.toString();
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
    return this.getName(longVersion) + '('
        + this.getConfiguration(longVersion) + ')';
  }

  /**
   * Get the size of the list
   *
   * @return the number of individuals in the list
   */
  @Override
  public final int size() {
    return this.cnt;
  }

  /**
   *Get a specific individual
   *
   * @param index
   *          the index
   *@return the individual
   */
  @Override
  @SuppressWarnings("unchecked")
  public final MOIndividual<G, X> get(final int index) {
    return this.lst[index];
  }

  /**
   * The set operation actually performs a deletion followed by an addition
   *
   * @param index
   *          ignored
   * @param ind
   *          the individual
   * @return the removed indivdual
   */
  @Override
  public final MOIndividual<G, X> set(final int index,
      final MOIndividual<G, X> ind) {
    MOIndividual<G, X> r;
    r = this.remove(index);
    this.add(ind);
    return r;
  }

  /**
   * IfThenElse an individual to the end of the list
   *
   * @param index
   *          ignored
   * @param ind
   *          the individual
   */
  @Override
  public final void add(final int index, final MOIndividual<G, X> ind) {
    this.add(ind);
  }

  /**
   *Delete the individual at the given index
   *
   * @param index
   *          the index
   *@return the deleted individual
   */
  @SuppressWarnings("unchecked")
  @Override
  public MOIndividual<G, X> remove(final int index) {
    MOIndividual<G, X> x;
    MOIndividual[] v;
    int q;

    v = this.lst;
    x = v[index];
    q = (--this.cnt);
    v[index] = v[q];
    v[q] = null;
    return x;
  }

  /**
   * @param fromIndex
   *          index of first element to be removed
   * @param toIndex
   *          index after last element to be removed
   */
  @Override
  protected final void removeRange(final int fromIndex, final int toIndex) {
    int x;
    x = toIndex - fromIndex;
    System.arraycopy(this.lst, toIndex, this.lst, fromIndex, x);
    Arrays.fill(this.lst, fromIndex, this.cnt, null);
    this.cnt -= x;
  }

  /**
   * Check in an individual into the archive while maintaining the proper
   * maximum size of the archive by using techniques such as those
   * discussed in Section 28.6.3.
   *
   * @param ind
   *          the individual to be checked in
   * @return true if the list changed, falseotherwise
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean add(final MOIndividual<G, X> ind) {
    int i, x;
    int sze;
    final int ms;
    MOIndividual<G, X> m;
    MOIndividual[] l;

    sze = this.cnt;
    l = this.lst;
    // check if we already have that individual or if we can remove some
    // others
    for (i = sze; (--i) >= 0;) {

      m = l[i];
      if (Utils.equals(m.g, ind.g) && Utils.equals(m.x, ind.x)) {
        return false;
      }

      x = this.ic.compare(ind, m);
      if (x > 0) {
        return false;
      }

      if (x < 0) {
        this.remove(i);
        sze--;
      }
    }

    ms = this.maxSize;
    if (sze < ms) {
      // ok, we do not yet have that individual, but we have enough space
      if (sze >= l.length) {
        l = new MOIndividual[Math.min(ms, Math.max(sze + 1, sze << 1))];
        System.arraycopy(this.lst, 0, l, 0, sze);
        this.lst = l;
      }

      l[sze++] = ind;
      this.cnt = sze;
      return true;
    }

    // the individual is new but the space is not sufficient -> delete one
    return this.replaceOne(ind);
  }

  /**
   * Appends all of the elements in the specified collection to the end of
   * this list, in the order that they are returned by the specified
   * collection's Iterator. The behavior of this operation is undefined if
   * the specified collection is modified while the operation is in
   * progress. (This implies that the behavior of this call is undefined if
   * the specified collection is this list, and this list is nonempty.)
   *
   * @param c
   *          collection containing elements to be added to this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws NullPointerException
   *           if the specified collection is null
   */
  @Override
  public boolean addAll(Collection<? extends MOIndividual<G, X>> c) {
    boolean b;

    b = false;
    for (MOIndividual<G, X> e : c) {
      if (this.add(e)) {
        b = true;
      }
    }
    return b;
  }

  /**
   * Replace one individual in the archive. Currently, this method just
   * replaces a random individual. However, more sophisticated strategies
   * could be employed as well.
   *
   * @param ind
   *          the individual
   * @return true if the new individual was added, false otherwise
   */
  @SuppressWarnings("unchecked")
  protected boolean replaceOne(final MOIndividual<G, X> ind) {
    double w, deps;
    int i, j, bi, ci, z;
    double[] a, b, eps;
    final int s, r;
    final MOIndividual[] l;
    boolean cr;

    a = ind.f;
    r = a.length;
    s = this.cnt;
    l = this.lst;

    // do the replacement
    for (i = s; (--i) >= 0;) {
      // if the new individual equals and existing one, skip
      if (Utils.equals(l[i].x, ind.x) || Utils.equals(l[i].g, ind.g)) {
        return false;
      }
    }

    bi = (this.replaceIdx % s);

    eps = this.epsilon;
    if (eps == null) {
      this.epsilon = eps = new double[r];
    } else {
      Arrays.fill(eps, 0, r, 0d);
    }

    // try to find the most similar individual and replace it
    ci = bi;
    for (z = (r * 3); (--z) >= 0;) {
      deps = Double.POSITIVE_INFINITY;
      ci = ((ci + 1) % r);

      for (i = s; (--i) >= 0;) {
        bi = ((bi + 1) % s);
        b = l[bi].f;
        cr = true;
        for (j = r; (--j) >= 0;) {
          w = Math.abs(a[j] - b[j]);
          if (w > eps[j]) {
            cr = false;
            if ((j == ci) && (w < deps)) {
              deps = w;
            }
          }
        }

        if (cr) {
          l[bi] = ind;
          this.replaceIdx = bi;
          return true;
        }
      }
      eps[ci] = deps;
    }

    // individuals are not sufficiently similar: delete more or less
    // randomly
    bi = ((bi + 1) % s);
    this.replaceIdx = bi;
    l[bi] = ind;
    return true;
  }

  /**
   * Sort by a given comparator. Notice that sorting with the archive's
   * default comparator makes no sense at all.
   *
   * @param cmp
   *          the comparator
   */
  @SuppressWarnings("unchecked")
  public void sort(final IIndividualComparator cmp) {
    Arrays.sort(this.lst, 0, this.cnt, ((Comparator) cmp));
  }

  /** Sort lexicographically */
  public void sortLexicographically() {
    this.sort(Lexicographic.LEXICOGRAPHIC_COMPARATOR);
  }

  /**
   * Select a couple of individuals
   *
   * @param sel
   *          the selection algorithm
   * @param mate
   *          the mating pool to select to
   * @param mateStart
   *          the start index
   * @param mateCount
   *          the number of individuals to select
   * @param r
   *          the randomizer
   */
  public final void select(final ISelectionAlgorithm sel,
      final Individual<?, ?>[] mate, final int mateStart,
      final int mateCount, final Random r) {
    final int c;

    c = this.cnt;
    if (c <= 0) {
      Arrays.fill(mate, mateStart, mateStart + mateCount, null);
    } else {
      sel.select(this.lst, 0, c, mate, mateStart, mateCount, r);
    }
  }

  /**
   * Copy some the individuals
   *
   * @param mate
   *          the mating pool to select to
   * @param mateStart
   *          the start index
   * @param mateCount
   *          the maximum number of individuals to select
   * @return the number of selected individuals (may be less)
   */
  @SuppressWarnings("unchecked")
  public final int copy(final Individual<?, ?>[] mate,
      final int mateStart, final int mateCount) {
    final int c;
    final MOIndividual[] l;
    int i, cpy;

    c = this.cnt;
    l = this.lst;
    if (mateCount >= c) {
      System.arraycopy(l, 0, mate, mateStart, c);
      Arrays.fill(mate, mateStart + c, mateStart + mateCount, null);
      return c;
    }

    cpy = this.copyIndex;
    for (i = (mateStart + mateCount); (--i) >= mateStart;) {
      cpy = ((cpy + 1) % c);
      mate[i] = l[cpy];
    }

    this.copyIndex = cpy;

    return mateCount;
  }
}