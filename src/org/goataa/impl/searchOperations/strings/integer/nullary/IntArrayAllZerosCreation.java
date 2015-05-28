// Copyright (c) 2015 Michael Krane (Michael.Krane@stud.uni-due.de)
// GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)

package org.goataa.impl.searchOperations.strings.integer.nullary;

import java.util.Random;

import org.goataa.impl.searchOperations.strings.integer.IntVectorCreation;

/**
 * A nullary search operation (see Section 4.2) for int vectors in a bounded
 * subspace of the int vectors of dimension n. This operation creates vectors
 * which are uniformly distributed in the interval [min,max]^n.
 *
 * @author Michael Krane
 */
public class IntArrayAllZerosCreation extends IntVectorCreation {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	/**
	 * Instantiate the real-vector creation operation
	 *
	 * @param dim
	 *            the dimension of the search space
	 * @param mi
	 *            the minimum value of the allele (Definition D4.4) of a gene
	 * @param ma
	 *            the maximum value of the allele of a gene
	 */
	public IntArrayAllZerosCreation(final int dim, final int mi, final int ma) {
		super(dim, 0, 0);
	}

	/**
	 * This operation just produces uniformly distributed random vectors in the
	 * interval [this.min, this.max]^this.n
	 *
	 * @param r
	 *            the random number generator
	 * @return a new random real vector of dimension n
	 */
	@Override
	public final int[] create(final Random r) {
		int[] g;

		// create a new int vector (genotype, Definition D4.2) of
		// dimension n with all 0s
		g = new int[this.n];

		return g;
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
		return "DA-U"; //$NON-NLS-1$
	}
}