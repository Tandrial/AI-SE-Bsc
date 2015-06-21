package uni.dc.ubsOpti.goataaExt.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.goataa.impl.utils.Individual;
import org.goataa.spec.IObjectiveFunction;
import org.goataa.spec.ITerminationCriterion;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.UbsOptiConfig;

/**
 * A simple implementation of the backtracking algorithm adapted to be traceable
 * by UbsOpti.
 *
 * @author Michael Krane
 */
public final class BackTrackingTraceable extends LocalSearchAlgorithmTraceable<int[], int[], Individual<int[], int[]>> {

	/** a constant required by Java serialization */
	private static final long serialVersionUID = 1;

	private static Traffic traffic;
	private static PriorityConfiguration prioConfig;
	private static int maxPrio;

	public void setConfig(UbsOptiConfig config) {
		traffic = config.getTraffic();
		prioConfig = config.getPriorityConfig();
	}

	public void setMaxPrio(int maxPrio) {
		BackTrackingTraceable.maxPrio = maxPrio;
	}

	private static Individual<int[], int[]> best = new Individual<int[], int[]>();

	/** instantiate the BackTracking class */
	public BackTrackingTraceable() {
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
		result.add(backTrack(this.getObjectiveFunction(), term, this.getNullarySearchOperation().create(null),
				new HashSet<int[]>(), null));
		Individual<int[], int[]> p = new Individual<int[], int[]>();
		p.x = new int[0];
		notifyTracer(p);
	}

	public final Individual<int[], int[]> backTrack(IObjectiveFunction<int[]> f, ITerminationCriterion term,
			int[] prio, Set<int[]> visisted, Individual<int[], int[]> parent) {
		// 0) Falls Prio schon besucht abbruch, sonst Prio zu besucht hinzufügen
		if (visisted.contains(prio))
			return best;

		visisted.add(prio);

		Individual<int[], int[]> p = new Individual<int[], int[]>();
		p.x = prio;
		p.v = f.compute(p.x, null);
		// System.out.println(parent + "->" + p);
		notifyTracer(p, parent);

		// 1) Delays berechnen, falls besser ==> speichern in trace
		// 2) checkDelays, falls ja : Rekursion fertig beste Prio zurück geben
		if (p.v < best.v) {
			best.assign(p);
		}

		// 2) Sortiere Stream f absteigend nach (maxLatency - delay)
		List<Flow> flows = new ArrayList<Flow>(traffic);
		Collections.sort(flows, new Comparator<Flow>() {
			@Override
			public int compare(Flow o1, Flow o2) {
				return Double.compare(o1.getDiffDelayAllowedDelay(), o2.getDiffDelayAllowedDelay());
			}
		});
		for (Flow flow : flows) {
			// 3) Erzeuge n = f.getPath().length neue Prios mit Prio von f + 1
			// bei f.getPath().get(i) für i = 0..n (falls möglich)
			for (int pos = 0; pos < flow.getPath().size() - 1; pos++) {
				if (term.terminationCriterion())
					return best;
				EgressPort port = flow.getPath().get(pos);

				int posToChange = prioConfig.getPos(port, flow);
				int currPrio = p.x[posToChange];
				int[] newP = Arrays.copyOf(p.x, p.x.length);

				if (currPrio < maxPrio) {
					newP[posToChange] = currPrio + 1;
					// 4) Rekursion start mit allen erzeugen Prios
					backTrack(f, term, newP, visisted, p);
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
			return this.getClass().getSimpleName();
		}
		return "BT";
	}
}