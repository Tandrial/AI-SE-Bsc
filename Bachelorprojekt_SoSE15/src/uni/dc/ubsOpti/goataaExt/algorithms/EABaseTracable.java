package uni.dc.ubsOpti.goataaExt.algorithms;

import org.goataa.impl.algorithms.ea.selection.RandomSelection;
import org.goataa.impl.searchOperations.BinarySearchOperation;
import org.goataa.impl.utils.Individual;
import org.goataa.impl.utils.TextUtils;
import org.goataa.spec.IBinarySearchOperation;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.ISelectionAlgorithm;

/**
 * The base class for evolutionary algorithms as discussed in Chapter 28.
 * adapted to be traceable by UbsOpti.
 *
 * @param <G>
 *            the search space (genome, Section 4.1)
 * @param <X>
 *            the problem space (phenome, Section 2.1)
 * @author Michael Krane
 */
public abstract class EABaseTracable<G, X> extends LocalSearchAlgorithmTraceable<G, X, Individual<G, X>> {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	/** the default crossover rate */
	public static final double DEFAULT_CROSSOVER_RATE = 0.7d;

	/** the default mutation rate */
	public static final double DEFAULT_MUTATION_RATE = (1d - DEFAULT_CROSSOVER_RATE);

	/** the default population size */
	public static final int DEFAULT_POPULATION_SIZE = 100;

	/** the default mating pool size */
	public static final int DEFAULT_MATING_POOL_SIZE = DEFAULT_POPULATION_SIZE;

	/** the binary search operation */
	private IBinarySearchOperation<G> o2;

	/** the crossover rate */
	private double crossoverRate;

	/** the mutation rate */
	private double mutationRate;

	/** the selection algorithm */
	private ISelectionAlgorithm selectionAlgorithm;

	/** the population size */
	private int ps;

	/** the mating pool size */
	private int mps;

	/** instantiate the ea base */
	@SuppressWarnings("unchecked")
	protected EABaseTracable() {
		super();

		this.crossoverRate = DEFAULT_CROSSOVER_RATE;
		this.mutationRate = DEFAULT_MUTATION_RATE;
		this.ps = DEFAULT_POPULATION_SIZE;
		this.mps = DEFAULT_MATING_POOL_SIZE;
		this.selectionAlgorithm = RandomSelection.RANDOM_SELECTION;
		this.o2 = ((IBinarySearchOperation<G>) (BinarySearchOperation.IDENTITY_CROSSOVER));
	}

	/**
	 * Set the binary search operation to be used by this optimization algorithm
	 * (see Section 4.2 and Listing 55.4). Notice that not all optimization
	 * algorithms do support this method.
	 *
	 * @param op
	 *            the binary search operation to use
	 */
	@SuppressWarnings("unchecked")
	public final void setBinarySearchOperation(final IBinarySearchOperation<G> op) {
		this.o2 = ((op != null) ? op : ((IBinarySearchOperation<G>) (BinarySearchOperation.IDENTITY_CROSSOVER)));
	}

	/**
	 * Get the binary search operation which is used by this optimization
	 * algorithm (see Section 4.2 and Listing 55.4).Notice that not all
	 * optimization algorithms do support this method.
	 *
	 * @return the binary search operation to use
	 */
	public final IBinarySearchOperation<G> getBinarySearchOperation() {
		return this.o2;
	}

	/**
	 * Set the crossover rate, i.e., the fraction of selected individuals to
	 * which the recombination operation (see Definition D28.14) should be
	 * applied.
	 *
	 * @param cr
	 *            the crossover rate
	 */
	public final void setCrossoverRate(final double cr) {
		this.crossoverRate = Math.max(0d, Math.min(1d, cr));
	}

	/**
	 * Get the crossover rate, i.e., the fraction of selected individuals to
	 * which the recombination operation (see Definition D28.14) should be
	 * applied.
	 *
	 * @return the crossover rate
	 */
	public final double getCrossoverRate() {
		return this.crossoverRate;
	}

	/**
	 * Set the mutation rate, i.e., the fraction of selected individuals to
	 * which the mutation operation (see Definition D28.13) should be applied.
	 *
	 * @param mr
	 *            the mutation rate
	 */
	public final void setMutationRate(final double mr) {
		this.mutationRate = Math.max(0d, Math.min(1d, mr));
	}

	/**
	 * Get the mutation rate, i.e., the fraction of selected individuals to
	 * which the mutation operation (see Definition D28.13) should be applied.
	 *
	 * @return the mutation rate
	 */
	public final double getMutationRate() {
		return this.mutationRate;
	}

	/**
	 * Set the population size, i.e., the number of individuals which are kept
	 * in the population.
	 *
	 * @param aps
	 *            the population size
	 */
	public final void setPopulationSize(final int aps) {
		this.ps = Math.max(1, aps);
	}

	/**
	 * Set the population size, i.e., the number of individuals which are kept
	 * in the population.
	 *
	 * @return the population size
	 */
	public final int getPopulationSize() {
		return this.ps;
	}

	/**
	 * Set the mating pool size, i.e., the number of individuals which will be
	 * selected into the mating pool.
	 *
	 * @param amps
	 *            the mating pool size
	 */
	public final void setMatingPoolSize(final int amps) {
		this.mps = Math.max(1, amps);
	}

	/**
	 * Set the mating pool size, i.e., the number of individuals which will be
	 * selected into the mating pool.
	 *
	 * @return the mating pool size
	 */
	public final int getMatingPoolSize() {
		return this.mps;
	}

	/**
	 * Set the selection algorithm (see Section 28.4) to be used by this
	 * evolutionary algorithm.
	 *
	 * @param sel
	 *            the selection algorithm
	 */
	public final void setSelectionAlgorithm(final ISelectionAlgorithm sel) {
		this.selectionAlgorithm = sel;
	}

	/**
	 * Get the selection algorithm (see Section 28.4) which is used by this
	 * evolutionary algorithm.
	 *
	 * @return the selection algorithm
	 */
	public final ISelectionAlgorithm getSelectionAlgorithm() {
		return this.selectionAlgorithm;
	}

	/**
	 * Get the full configuration which holds all the data necessary to describe
	 * this object.
	 *
	 * @param longVersion
	 *            true if the long version should be returned, false if the
	 *            short version should be returned
	 * @return the full configuration
	 */
	@Override
	public String getConfiguration(final boolean longVersion) {
		StringBuilder sb;
		IOptimizationModule m;

		sb = new StringBuilder(super.getConfiguration(longVersion));

		m = this.getBinarySearchOperation();
		if (m != null) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append("o2=");//$NON-NLS-1$
			sb.append(m.toString(longVersion));
		}

		sb.append(",cr="); //$NON-NLS-1$
		sb.append(TextUtils.formatNumber(this.getCrossoverRate()));

		sb.append(",mr=");//$NON-NLS-1$
		sb.append(TextUtils.formatNumber(this.getMutationRate()));

		sb.append(",ps=");//$NON-NLS-1$
		sb.append(TextUtils.formatNumber(this.getPopulationSize()));

		sb.append(",mps=");//$NON-NLS-1$
		sb.append(TextUtils.formatNumber(this.getMatingPoolSize()));

		m = this.getSelectionAlgorithm();
		if (m != null) {
			sb.append(",sel=");//$NON-NLS-1$
			sb.append(m.toString(longVersion));
		}

		return sb.toString();
	}
}