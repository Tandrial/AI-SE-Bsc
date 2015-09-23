package uni.dc.networkGenerator;

import java.util.Random;

import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;

public class RandomPriorityGenerator {
	private Random rng;
	private Traffic traffic;
	private int maxPrio;

	public void setTraffic(Traffic traffic) {
		this.traffic = traffic;
	}

	public void setMaxPrio(int maxPrio) {
		this.maxPrio = maxPrio;
	}

	public void setRng(Random rng) {
		this.rng = rng;
	}

	public PriorityConfiguration generate() {
		PriorityConfiguration prio = new PriorityConfiguration(traffic);
		int[] arr = prio.toIntArray();
		for (int i = 0; i < arr.length; i++) {
			arr[i] = rng.nextInt(maxPrio) + 1;
		}
		prio.fromIntArray(arr);
		prio.squash();
		return prio;
	}
}
