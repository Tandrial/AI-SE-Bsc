package uni.dc.ubsOpti;

import java.util.Arrays;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.Tracer.DelayTrace;
import uni.dc.ubsOpti.Tracer.Tracable;

public class BruteForce implements Tracable {

	private UbsDelayCalc delayCalc;
	private int[] bestPrio;
	private double minDelay = Double.MAX_VALUE;

	private static DelayTrace delays;
	private static long step;

	private boolean stopRecursion = false;

	public BruteForce(UbsDelayCalc delayCalc) {
		this.delayCalc = delayCalc;
	}

	public int[] optimize(PriorityConfiguration prio, int maxPrio) {
		bestPrio = prio.toIntArray();
		minDelay = delayCalc.compute(bestPrio, null);
		delays.addDataPoint(step, delayCalc.compute(bestPrio, null));
		genPermutations(new int[bestPrio.length], 0, maxPrio);		
		return bestPrio;
	}

	private void genPermutations(int[] n, int pos, int max) {
		if (pos == n.length) {
			double delay = delayCalc.compute(n, null);
			step++;
			if (delay < minDelay) {
				minDelay = delay;
				bestPrio = Arrays.copyOf(n, n.length);
				delays.addDataPoint(step, delay);
				if (delayCalc.checkDelays())
					stopRecursion = true;
			}
			return;
		} else {
			for (int i = 1; i <= max; i++) {
				n[pos] = i;
				genPermutations(n, pos + 1, max);
				if (stopRecursion)
					return;
			}
		}
	}

	@Override
	public DelayTrace getTrace() {
		return delays;
	}

	@Override
	public void setUpTrace(OptimizerConfig config) {
		delays = new DelayTrace("BruteForce", config);
		step = 1;
	}

}
