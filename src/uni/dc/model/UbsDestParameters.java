package uni.dc.model;

import java.util.List;

public class UbsDestParameters {

	private double maxLatencyRequirement;
	private double actualDelay;
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

	public double getActualDelay() {
		return actualDelay;
	}

	public void setActualDelay(double actualDelay) {
		this.actualDelay = actualDelay;
	}

	public List<EgressPort> getPath() {
		return path;
	}

	public void setPath(List<EgressPort> path) {
		this.path = path;
	}
}
