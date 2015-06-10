package uni.dc.ubsOpti.Tracer;

import java.util.Arrays;

public class TracerStat{

	private long step;
	private double delay;
	private int[] prio;

	public TracerStat(long step, double delay, int[] prio) {
		this.step = step;
		this.delay = delay;
		this.prio = Arrays.copyOf(prio, prio.length);
	}

	public long getStep() {
		return step;
	}

	public double getDelay() {
		return delay;
	}

	public int[] getPrio() {
		return prio;
	}

	@Override
	public String toString() {
		return String.format("%d;%.8f\n", step, delay);
	}
}
