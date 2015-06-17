package uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer;

import org.goataa.impl.searchOperations.strings.FixedLengthStringCreation;
import org.goataa.impl.utils.TextUtils;

/**
 * A base class for vector creation operations
 *
 * @author Michael Krane
 */
public class IntVectorCreation extends FixedLengthStringCreation<int[]> {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	/** the minimum value an allel of a gene of the genotype can take on */
	public final int min;

	/** the maximum value an allel of a gene of the genotype can take on */
	public final int max;

	/**
	 * Create a new int-vector operation
	 *
	 * @param dim
	 *            the dimension of the search space
	 * @param mi
	 *            the minimum value of the allele (Definition D4.4) of a gene
	 * @param ma
	 *            the maximum value of the allele of a gene
	 */
	protected IntVectorCreation(final int dim, final int mi, final int ma) {
		super(dim);
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
		return getConfiguration(longVersion, this.min, this.max) + "^" + this.n; //$NON-NLS-1$
	}

	/**
	 * Print a configuration
	 *
	 * @param longVersion
	 *            true if the long version should be returned, false if the
	 *            short version should be returned
	 * @param mi
	 *            the minimum
	 * @param ma
	 *            the maximum
	 * @return the full configuration
	 */
	static final String getConfiguration(final boolean longVersion, final int mi, final int ma) {
		return (longVersion ? "G=[" : "[")//$NON-NLS-1$//$NON-NLS-2$
				+ TextUtils.formatNumber(mi) + ',' + TextUtils.formatNumber(ma) + "]"; //$NON-NLS-1$
	}
}