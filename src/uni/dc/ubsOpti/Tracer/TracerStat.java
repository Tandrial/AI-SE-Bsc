package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.Arrays;

public class TracerStat implements Serializable {
	private static final long serialVersionUID = 1L;
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
