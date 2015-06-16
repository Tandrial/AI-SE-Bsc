package uni.dc.ubsOpti.goataaExt.algorithms;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.tracer.DelayTrace;
import uni.dc.ubsOpti.tracer.Traceable;

public class BackTrackingTraceable implements Traceable {

	private UbsDelayCalc delayCalc;
	private int[] bestPrio;
	private double minDelay = Double.MAX_VALUE;

	private DelayTrace delays;
	private long step;

	@Override
	public DelayTrace getTrace() {
		return delays;
	}

	@Override
	public void setUpTrace(UbsOptiConfig config) {
		delays = new DelayTrace("BackTracking", config);
		step = 1;
	}

	public int[] optimize(PriorityConfiguration prio, int maxPrio) {
		bestPrio = prio.toIntArray();
		minDelay = delayCalc.compute(bestPrio, null);
		delays.addDataPoint(step, delayCalc.compute(bestPrio, null), bestPrio);
		return bestPrio;
	}

	private void backTrack() {
		// TODO
	}
}
