// Copyright (c) 2010 Thomas Weise (http://www.it-weise.de/, tweise@gmx.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.objectiveFunctions;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

import org.goataa.impl.utils.Constants;
import org.goataa.impl.utils.TextUtils;
import org.goataa.spec.IObjectiveFunction;

/**
 * Objective functions as defined in Section 2.2
 * rate how good a candidate solution is. Sometimes, it is interesting to
 * see how an optimization algorithm improves the solution quality over
 * time. This class acts as a wrapper around the real objective function
 * and logs the best objective values found.
 *
 * @param <X>
 *          the problem space (phenome, Section 2.1)
 * @author Thomas Weise
 */
public class LoggingObjectiveFunction<X> extends ObjectiveFunction<X> {
  /** a constant required by Java serialization */
  private static final long serialVersionUID = 1;

  /** the real objective function */
  private final IObjectiveFunction<X> delegate;

  /** the function evaluation counter */
  private int fe;

  /** the best objective value encountered so far */
  private double best;

  /** the writer to which the information is logged */
  private Writer w;

  /**
   * Create a new logging objective function which is wrapped around a real
   * objective function
   *
   * @param pdelegate
   *          the delegate
   */
  public LoggingObjectiveFunction(final IObjectiveFunction<X> pdelegate) {
    this(pdelegate, null);
  }

  /**
   * Create a new logging objective function which is wrapped around a real
   * objective function
   *
   * @param pdelegate
   *          the delegate
   * @param writ
   *          the writer to write to, or null if the stdout should be used
   */
  public LoggingObjectiveFunction(final IObjectiveFunction<X> pdelegate,
      final Writer writ) {
    super();
    this.delegate = pdelegate;
    this.w = ((writ != null) ? writ : new OutputStreamWriter(System.out));
    this.best = Constants.WORST_OBJECTIVE;
  }

  /**
   * Compute the objective value, i.e., determine the utility of the
   * solution candidate x as specified in
   * Definition D2.3.
   *
   * @param x
   *          the phenotype to be rated
   * @param r
   *          the randomizer
   * @return the objective value of x, the lower the better (see
   *         Section 6.3.4)
   */
  @Override
  public final double compute(final X x, final Random r) {
    final double d;

    // first, get the real objective value
    d = this.delegate.compute(x, r);
    // increase the number of objective function invocations
    this.fe++;

    // if this is the first function evaluation or an improvement was found
    if ((this.fe <= 1) || (d < this.best)) {
      // remember the new best value
      this.best = d;
      try {
        // and print it to the log
        this.w.write(Integer.toString(this.fe) + '\t' + Double.toString(d)
            + TextUtils.NEWLINE);
        // plus flush the log
        this.w.flush();
      } catch (Throwable t) {
        // did something go wrong?
        t.printStackTrace();
      }
    }

    // return the objective value
    return d;
  }

  /**
   * Close the writer when the object is disposed... this is a very bad way
   * for closing streams: Kids, don't try that at home! Here, for our
   * purposes, this is enough
   *
   * @throws Throwable
   *           if something goes wrong
   */
  @Override
  protected void finalize() throws Throwable {
    this.w.close();
    super.finalize();
  }

  /**
   * Get the name of the optimization module
   *
   * @param longVersion
   *          true if the long name should be returned, false if the short
   *          name should be returned
   * @return the name of the optimization module
   */
  @Override
  public String getName(final boolean longVersion) {
    return this.delegate.getName(longVersion);
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
  @Override
  public String getConfiguration(final boolean longVersion) {
    return this.delegate.getConfiguration(longVersion);
  }

}