package uni.dc.ubsOpti.Tracer;


public class TracerStat {

	private long step;
	private double delay;

	public TracerStat(long step, double delay) {
		this.step = step;
		this.delay = delay;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	@Override
	public String toString() {
		return String.format("Step %d - delay = %.8f s\n", step, delay);
	}
}
