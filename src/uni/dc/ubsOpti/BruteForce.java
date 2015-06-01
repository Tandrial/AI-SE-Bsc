package uni.dc.ubsOpti;

import java.util.Arrays;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;

public class BruteForce {

	private UbsDelayCalc delayCalc;
	private int[] bestPrio;
	private double minDelay = Double.MAX_VALUE;

	public BruteForce(UbsDelayCalc delayCalc) {
		this.delayCalc = delayCalc;
	}

	public int[] optimize(PriorityConfiguration prio, int maxPrio) {
		bestPrio = prio.toIntArray();
		minDelay = delayCalc.compute(bestPrio, null);
		genPermutations(new int[bestPrio.length], 0, maxPrio);
		return bestPrio;
	}

	private void genPermutations(int[] n, int pos, int max) {
		if (pos == n.length) {
			double delay = delayCalc.compute(n, null);
			if (delay < minDelay) {
				minDelay = delay;
				bestPrio = Arrays.copyOf(n, n.length);
			}
			return;
		} else {
			for (int i = 1; i <= max; i++) {
				n[pos] = i;
				genPermutations(n, pos + 1, max);
			}
		}
	}
}
