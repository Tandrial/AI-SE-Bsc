package uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer;

import org.goataa.impl.searchOperations.UnarySearchOperation;

/**
 * A base class for vector mutation operations
 *
 * @author Michael Krane
 */
public class IntVectorMutation extends UnarySearchOperation<int[]> {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	/** the minimum value an allel of a gene of the genotype can take on */
	public final int min;

	/** the maximum value an allel of a gene of the genotype can take on */
	public final int max;

	/**
	 * Create a new int-vector operation
	 *
	 * @param mi
	 *            the minimum value of the allele (Definition D4.4) of a gene
	 * @param ma
	 *            the maximum value of the allele of a gene
	 */
	protected IntVectorMutation(final int mi, final int ma) {
		super();
		this.min = mi;
		this.max = ma;
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
		return IntVectorCreation.getConfiguration(longVersion, this.min, this.max);
	}
}