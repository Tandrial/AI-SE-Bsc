package uni.dc.ubsOpti;

import java.util.Map;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.DelayCalc.UbsV0DelayCalc;

public class BruteForce {

	private UbsV0DelayCalc delayCalc;

	private PriorityConfiguration bestPrio = null;
	private double minDelay = Double.MAX_VALUE;

	public BruteForce(Map<EgressPort, Set<Flow>> traffic) {
		delayCalc = new UbsV0DelayCalc(traffic);
	}

	public void optimize(PriorityConfiguration prio, int maxPrio) {
		int[] prios = prio.toIntArray();
		bestPrio = prio;
		delayCalc.setPrio(prio);
		minDelay = delayCalc.compute(prios, null);
		genPermutations(new int[prios.length], 0, maxPrio);
		
		System.out.println("minDelay = " + minDelay);
		System.out.println(bestPrio);
	}

	private void genPermutations(int[] n, int pos, int max) {
		if (pos == n.length) {
			double delay = delayCalc.compute(n, null);
			if (delay < minDelay) {
				System.out.println("found better solution " + delay);
				minDelay = delay;
				bestPrio.fromIntArray(n);
			}
			return;
		} else {
			for (int i = 0; i <= max; i++) {
				n[pos] = i;
				genPermutations(n, pos + 1, max);
			}
		}
	}

	public PriorityConfiguration getBestConfig() {
		if (bestPrio != null)
			return bestPrio;
		else
			return null;
	}

}
