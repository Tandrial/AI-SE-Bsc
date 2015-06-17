package uni.dc.ubsOpti.goataaExt.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.tracer.DelayTrace;
import uni.dc.ubsOpti.tracer.Traceable;

public class BackTrackingTraceable implements Traceable {

	private UbsDelayCalc delayCalc;
	private UbsOptiConfig config;
	private int[] bestPrio;
	private double minDelay = Double.MAX_VALUE;

	private DelayTrace delays;
	private long step;

	private boolean stopRecursion = false;

	@Override
	public DelayTrace getTrace() {
		return delays;
	}

	@Override
	public void setUpTrace(UbsOptiConfig config) {
		delays = new DelayTrace("BackTracking", config);
		step = 1;
	}

	public BackTrackingTraceable(UbsOptiConfig config) {
		this.config = config;
		this.delayCalc = config.getDelayCalc();
	}

	public int[] optimize(PriorityConfiguration prio, int maxPrio) {
		bestPrio = prio.toIntArray();
		minDelay = delayCalc.compute(bestPrio, null);
		delays.addDataPoint(step, delayCalc.compute(bestPrio, null), bestPrio);
		backTrack(bestPrio, new HashSet<int[]>());
		return bestPrio;
	}

	private void backTrack(int[] prio, Set<int[]> visisted) {
		// 0) Falls Prio schon besucht abbruch, sonst Prio zu besucht hinzufügen
		if (stopRecursion || visisted.contains(prio)) {
			return;
		}
		visisted.add(prio);
		step++;

		// 1) Delays berechnen, falls besser ==> speichern in trace
		double delay = delayCalc.compute(prio, null);
		if (delay < minDelay) {
			PriorityConfiguration prioConfig = (PriorityConfiguration) config
					.getPriorityConfig().clone();
			prioConfig.fromIntArray(prio);
			minDelay = delay;
			bestPrio = Arrays.copyOf(prio, prio.length);
			delays.addDataPoint(step, delay, prio);
		}

		// 2) checkDelays, falls ja : Rekursion fertig beste Prio zurück geben
		if (delayCalc.checkDelays()) {
			stopRecursion = true;
			return;
		}

		// 2) Sortiere Stream f absteigend nach (maxLatency - delay)
		List<Flow> flows = new ArrayList<Flow>(config.getTraffic());
		Collections.sort(flows, new Comparator<Flow>() {
			@Override
			public int compare(Flow o1, Flow o2) {
				return Double.compare(o1.getDiffDelayMaxLat(), o2.getDiffDelayMaxLat());
			}
		});
		for (Flow f : flows) {
			// 3) Erzeuge n = f.getPath().length neue Prios mit Prio von f + 1
			// bei f.getPath().get(i) für i = 0..n (falls möglich)
			for (int pos = 0; pos < f.getPath().size() - 1; pos++) {
				EgressPort p = f.getPath().get(pos);
				if (stopRecursion) {
					return;
				}
				PriorityConfiguration prioConfig = (PriorityConfiguration) config
						.getPriorityConfig().clone();
				prioConfig.fromIntArray(prio);
				int currPrio = prioConfig.getPriority(p, f);
				if (currPrio < config.getMaxPrio()) {
					prioConfig.setPriority(p, f, currPrio + 1);
					// 4) Rekursion start mit allen erzeugen Prios
					backTrack(prioConfig.toIntArray(), visisted);
				}
			}
		}
	}
}
