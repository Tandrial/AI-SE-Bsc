package uni.dc.ubsOpti.goataaExt.algorithms;

import java.util.List;
import java.util.Random;

import org.goataa.impl.utils.Individual;
import org.goataa.spec.IGPM;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.ITerminationCriterion;
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.tracer.DelayTrace;
import uni.dc.ubsOpti.tracer.Tracable;

/**
 * A simple implementation of the Hill Climbing algorithm introduced as
 * Algorithm 26.1. adapted to be traceable by UbsOpti.
 *
 * @param <G>
 *            the search space (genome, Section 4.1)
 * @param <X>
 *            the problem space (phenome, Section 2.1)
 * @author Michael Krane
 */
public final class HillClimbingTraceable<G, X> extends
		LocalSearchAlgorithmTraceable<G, X, Individual<G, X>> implements Tracable {

	private static DelayTrace delays;
	private static long step;

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	/** instantiate the hill climbing class */
	public HillClimbingTraceable() {
		super();
	}

	@Override
	public DelayTrace getTrace() {
		return delays;
	}

	@Override
	public void setUpTrace(UbsOptiConfig config) {
		delays = new DelayTrace(getName(true), config);
		step = 1;
	}

	/**
	 * Invoke the optimization process. This method calls the optimizer and
	 * returns the list of best individuals (see Definition D4.18) found.
	 * Usually, only a single individual will be returned. Different from the
	 * parameterless call method, here a randomizer and a termination criterion
	 * are directly passed in. Also, a list to fill in the optimization results
	 * is provided. This allows recursively using the optimization algorithms.
	 *
	 * @param r
	 *            the randomizer (will be used directly without setting the
	 *            seed)
	 * @param term
	 *            the termination criterion (will be used directly without
	 *            resetting)
	 * @param result
	 *            a list to which the results are to be appended
	 */
	@Override
	public void call(final Random r, final ITerminationCriterion term,
			final List<Individual<G, X>> result) {

		result.add(HillClimbingTraceable.hillClimbing(this.getObjectiveFunction(),//
				this.getNullarySearchOperation(), //
				this.getUnarySearchOperation(),//
				this.getGPM(), term, r));

	}

	/**
	 * We place the complete Hill Climbing method as defined in Algorithm 26.1
	 * into this single procedure.
	 *
	 * @param f
	 *            the objective function (Definition D2.3)
	 * @param create
	 *            the nullary search operator (Section 4.2) for creating the
	 *            initial genotype
	 * @param mutate
	 *            the unary search operator (Section 4.2) for modifying existing
	 *            genotypes
	 * @param gpm
	 *            the genotype-phenotype mapping (Section 4.3)
	 * @param term
	 *            the termination criterion (Section 6.3.3)
	 * @return the individual holding the best candidate solution
	 * @param r
	 *            the random number generator (Definition D2.2) found
	 * @param <G>
	 *            the search space (Section 4.1)
	 * @param <X>
	 *            the problem space (Section 2.1)
	 */
	public static final <G, X> Individual<G, X> hillClimbing(
			final IObjectiveFunction<X> f,
			final INullarySearchOperation<G> create,
			final IUnarySearchOperation<G> mutate,
			final IGPM<G, X> gpm,
			final ITerminationCriterion term, final Random r) {

		Individual<G, X> p, pnew;

		p = new Individual<G, X>();
		pnew = new Individual<G, X>();

		// create the first genotype, map it to a phenotype, and evaluate it
		p.g = create.create(r);
		p.x = gpm.gpm(p.g, r);
		p.v = f.compute(p.x, r);
		if (delays != null)
			delays.addDataPoint(step, p.v, (int[]) p.x);

		// check the termination criterion
		while (!(term.terminationCriterion())) {
			step++;
			// modify the best point known, map the new point to a phenotype and
			// evaluat it
			pnew.g = mutate.mutate(p.g, r);
			pnew.x = gpm.gpm(pnew.g, r);
			pnew.v = f.compute(pnew.x, r);

			// In Algorithm 26.1, the objective functions are
			// evaluated here. By storing the objective values in the individual
			// records, we avoid evaluating p.x more than once.
			if (pnew.v < p.v) {
				p.assign(pnew);
				if (delays != null)
					delays.addDataPoint(step, p.v, (int[]) p.x);
			}
		}

		return p;
	}

	/**
	 * Get the name of the optimization module
	 *
	 * @param longVersion
	 *            true if the long name should be returned, false if the short
	 *            name should be returned
	 * @return the name of the optimization module
	 */
	@Override
	public String getName(final boolean longVersion) {
		if (longVersion) {
			return this.getClass().getSimpleName();
		}
		return "HC";
	}
}