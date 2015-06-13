package uni.dc.model;

import java.io.Serializable;
import java.util.List;

public class UbsDestParameters implements Serializable {
	private static final long serialVersionUID = 1L;
	private double maxLatencyRequirement = 0.0d;
	private double delay = 0.0d;
	private List<EgressPort> path;

	public UbsDestParameters(double maxLatencyRequirement) {
		this.maxLatencyRequirement = maxLatencyRequirement;
	}

	public double getMaxLatencyRequirement() {
		return maxLatencyRequirement;
	}

	public void setMaxLatencyRequirement(double maxLatencyRequirement) {
		this.maxLatencyRequirement = maxLatencyRequirement;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public List<EgressPort> getPath() {
		return path;
	}

	public void setPath(List<EgressPort> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return " maxLat = " + maxLatencyRequirement + ", actualDelay = "
				+ delay;
	}

	public boolean checkDelay() {
		return maxLatencyRequirement >= delay;
	}
}
