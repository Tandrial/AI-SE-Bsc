package uni.dc.ubsOpti.goataaExt.algorithms;

import java.util.List;
import java.util.Random;

import org.goataa.impl.utils.Individual;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.ITerminationCriterion;

/**
 * A simple implementation of the Brute Force algorithm i adapted to be
 * traceable by UbsOpti.
 *
 * @author Michael Krane
 */
public final class BruteForceTraceable extends LocalSearchAlgorithmTraceable<int[], int[], Individual<int[], int[]>> {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	private static Individual<int[], int[]> best = new Individual<int[], int[]>();
	private static boolean stopRecursion = false;
	private Individual<?, ?> parent = null;

	private int dim;
	private int maxPrio;

	public void setDim(int dim) {
		this.dim = dim;
	}

	public void setMaxPrio(int maxPrio) {
		this.maxPrio = maxPrio;
	}

	/** instantiate the brute Force class */
	public BruteForceTraceable() {
		super();
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
	public void call(final Random r, final ITerminationCriterion term, final List<Individual<int[], int[]>> result) {
		result.add(bruteForce(this.getObjectiveFunction(), term, new int[dim], 0, maxPrio));
		Individual<int[], int[]> p = new Individual<int[], int[]>();
		p.x = new int[0];
		notifyTracer(p);
	}

	public Individual<int[], int[]> bruteForce(IObjectiveFunction<int[]> f, ITerminationCriterion term, int[] n,
			int pos, int maxPrio) {
		if (stopRecursion)
			return best;

		if (pos == n.length) {
			Individual<int[], int[]> p = new Individual<int[], int[]>();
			p.x = n;
			p.v = f.compute(p.x, null);
			notifyTracer(p, parent);
			parent = p;

			if (p.v < best.v) {
				best.assign(p);
				if (term.terminationCriterion())
					stopRecursion = true;
			}

		} else {
			for (int i = 1; i <= maxPrio; i++) {
				n[pos] = i;
				bruteForce(f, term, n, pos + 1, maxPrio);
			}
		}

		return best;
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
		return "BF";
	}
}