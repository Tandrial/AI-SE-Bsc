package uni.dc.model;

import java.util.List;

public class UbsDestParameters {

	private double maxLatencyRequirement;
	private double speedUpFactor = 1.0d;
	private double actualDelay;
	private List<EgressPort> path;

	public UbsDestParameters(double maxLatencyRequirement) {
		this.maxLatencyRequirement = maxLatencyRequirement;
	}

	public double getMaxLatencyRequirement() {
		return maxLatencyRequirement * speedUpFactor;
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

	public void incSpeedFactor(double inc) {
		speedUpFactor += inc;
	}

	public void decSpeedFactor(double dec) {
		speedUpFactor -= dec;
	}

	public List<EgressPort> getPath() {
		return path;
	}

	public void setPath(List<EgressPort> path) {
		this.path = path;
	}

	public void resetSpeed() {
		speedUpFactor = 1.0d;
	}
}
