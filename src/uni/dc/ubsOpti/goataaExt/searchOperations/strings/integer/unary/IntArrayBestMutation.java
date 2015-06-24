package uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.unary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.goataa.spec.IObjectiveFunction;

import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.IntVectorMutation;

/**
 *
 * @author Michael Krane
 */
public final class IntArrayBestMutation extends IntVectorMutation {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	private IObjectiveFunction<int[]> f;
	private Set<String> visited;

	/**
	 * Create a new int-vector mutation operation
	 *
	 * @param mi
	 *            the minimum value of the allele (Definition D4.4) of a gene
	 * @param ma
	 *            the maximum value of the allele of a gene
	 */
	public IntArrayBestMutation(final int mi, final int ma, final IObjectiveFunction<int[]> f) {
		super(mi, ma);
		this.f = f;
		this.visited = new HashSet<String>();
	}

	/**
	 * This is an unary search operation for vectors of int numbers. It takes
	 * one existing genotype g (see Definition D4.2) from the search space and
	 * produces one new genotype. This new element is the best possible solution which is
	 * different by 1 element
	 *
	 * @param g
	 *            the existing genotype in the search space from which a
	 *            slightly modified copy should be created
	 * @param r
	 *            the random number generator
	 * @return a new genotype
	 */
	@Override
	public final int[] mutate(final int[] g, final Random r) {
		int[] gnew;
		int[] best = Arrays.copyOf(g, g.length);
		visited.add(Arrays.toString(g));
		double vbest = Double.MAX_VALUE;
		for (int i = 0; i < g.length; i++) {
			if (g[i] < max) {
				gnew = Arrays.copyOf(g, g.length);
				gnew[i]++;
				if (visited.contains(Arrays.toString(gnew)))
					continue;
				visited.add(Arrays.toString(gnew));
				double vnew = f.compute(gnew, null);
				if (vnew < vbest) {
					best = gnew;
					vbest = vnew;
				}
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
			return super.getName(true);
		}
		return "DA-NAM"; //$NON-NLS-1$
	}
}