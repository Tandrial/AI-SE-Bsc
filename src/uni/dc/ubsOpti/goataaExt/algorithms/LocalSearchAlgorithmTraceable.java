package uni.dc.ubsOpti.goataaExt.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.goataa.impl.algorithms.SOOptimizationAlgorithm;
import org.goataa.impl.searchOperations.NullarySearchOperation;
import org.goataa.impl.searchOperations.UnarySearchOperation;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.IOptimizationModule;
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.ubsOpti.tracer.Traceable;
import uni.dc.ubsOpti.tracer.Tracer;
import uni.dc.ubsOpti.tracer.TracerStat;

/**
 * A simple local search algorithm which has a nullary and a unary search
 * operation adapted to be traceable by UbsOpti.
 *
 * @param <G>
 *            the search space (genome, Section 4.1)
 * @param <X>
 *            the problem space (phenome, Section 2.1)
 * @param <IT>
 *            the individual type
 * @author Michael Krane
 */
public class LocalSearchAlgorithmTraceable<G, X, IT extends Individual<G, X>> extends SOOptimizationAlgorithm<G, X, IT>
		implements Traceable {
	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	protected long step = 1;
	private List<Tracer> tracers = new ArrayList<Tracer>();

	@Override
	public void attach(Tracer tracer) {
		tracers.add(tracer);
	}

	@Override
	public void attach(Collection<Tracer> tracer) {
		tracers.addAll(tracer);
	}

	@Override
	public void detach(Tracer tracer) {
		tracers.remove(tracer);
	}

	@Override
	public void notifyTracer(Individual<?, ?> p) {
		TracerStat stat = new TracerStat(this.getName(false), step, p.v, (int[]) p.x);
		for (Tracer t : tracers) {
			t.update(stat);
		}
		step++;
	}

	/** the nullary search operation */
	private INullarySearchOperation<G> o0;

	/** the unary search operation */
	private IUnarySearchOperation<G> o1;

	/** Create a new optimization algorithm */
	@SuppressWarnings("unchecked")
	protected LocalSearchAlgorithmTraceable() {
		super();
		this.o0 = (INullarySearchOperation<G>) (NullarySearchOperation.NULL_CREATION);
		this.o1 = (IUnarySearchOperation<G>) (UnarySearchOperation.IDENTITY_MUTATION);
	}

	/**
	 * Set the nullary search operation to be used by this optimization
	 * algorithm (see Section 4.2 and Listing 55.2).
	 *
	 * @param op
	 *            the nullary search operation to use
	 */
	@SuppressWarnings("unchecked")
	public void setNullarySearchOperation(final INullarySearchOperation<G> op) {
		this.o0 = ((op != null) ? op : ((INullarySearchOperation<G>) (NullarySearchOperation.NULL_CREATION)));
	}

	/**
	 * Get the nullary search operation which is used by this optimization
	 * algorithm (see Section 4.2 and Listing 55.2).
	 *
	 * @return the nullary search operation to use
	 */
	public final INullarySearchOperation<G> getNullarySearchOperation() {
		return this.o0;
	}

	/**
	 * Set the unary search operation to be used by this optimization algorithm
	 * (see Section 4.2 and Listing 55.3).
	 *
	 * @param op
	 *            the unary search operation to use
	 */
	@SuppressWarnings("unchecked")
	public void setUnarySearchOperation(final IUnarySearchOperation<G> op) {
		this.o1 = ((op != null) ? op : (IUnarySearchOperation<G>) (UnarySearchOperation.IDENTITY_MUTATION));
	}

	/**
	 * Get the unary search operation which is used by this optimization
	 * algorithm (see Section 4.2 and Listing 55.3).
	 *
	 * @return the unary search operation to use
	 */
	public final IUnarySearchOperation<G> getUnarySearchOperation() {
		return this.o1;
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
		IOptimizationModule o;

		sb = new StringBuilder();

		sb.append(super.getConfiguration(longVersion));

		o = this.getNullarySearchOperation();
		if (o != null) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append("o0=");//$NON-NLS-1$
			sb.append(o.toString(longVersion));
		}

		o = this.getUnarySearchOperation();
		if (o != null) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append("o1=");//$NON-NLS-1$
			sb.append(o.toString(longVersion));
		}

		return sb.toString();
	}
}