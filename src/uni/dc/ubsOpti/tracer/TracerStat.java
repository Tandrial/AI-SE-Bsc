package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.Arrays;

public class TracerStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private long step;
	private double delay;
	private int[] prio;

	public TracerStat(String name, long step, double delay, int[] prio) {
		this.name = name;
		this.step = step;
		this.delay = delay;
		this.prio = Arrays.copyOf(prio, prio.length);
	}

	public String getName() {
		return name;
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
