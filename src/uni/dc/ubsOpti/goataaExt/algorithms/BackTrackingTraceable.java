package uni.dc.ubsOpti.goataaExt.algorithms;

import java.util.Arrays;
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

		// 1) Delays berechnen, falls besser ==> speichern in trace
		double delay = delayCalc.compute(prio, null);
		if (delay < minDelay) {
			minDelay = delay;
			bestPrio = Arrays.copyOf(prio, prio.length);
			delays.addDataPoint(step, delay, prio);
		}

		// 2) checkDelays, falls ja : Rekursion fertig beste Prio zurück geben
		if (delayCalc.checkDelays()) {
			stopRecursion = true;
			return;
		}
		// 2) Finde Stream f mit max(maxLatency - delay)
		Flow maxF = null;
		double maxDiff = 0.0d;
		for (Flow f : config.getTraffic()) {
			double diff = f.getMaxLatency() - f.getDelay();
			if (diff > maxDiff) {
				maxDiff = diff;
				maxF = f;
			}
		}
		// 3) Erzeuge n = f.getPath().length neue Prios mit Prio von f + 1 bei
		// f.getPath().get(i) für i = 0..n (falls möglich)
		// 4) Rekursion start mit allen erzeugen Prios
		List<EgressPort> path = maxF.getPath();
		for (EgressPort p : path) {
			int currPrio = config.getPriorityConfig().getPriority(p, maxF);
			if (currPrio < config.getMaxPrio()) {
				PriorityConfiguration prioConfig = (PriorityConfiguration) config
						.getPriorityConfig().clone();
				prioConfig.setPriority(p, maxF, currPrio + 1);
				//TODO Can't be recursiv!!!!
				backTrack(prioConfig.toIntArray(), visisted);
			}
		}
	}
}
