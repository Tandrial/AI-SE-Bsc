package uni.dc.util;

import java.util.Set;

import uni.dc.model.Flow;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;

public class OptimizerConfig {
	private int maxPrio = 2;
	private int maxSteps = 1000;
	private int runs = 20;

	private double speedFactor = 0.1d;
	private Set<Flow> flows;
	private NetworkParser parser;
	private UbsDelayCalc delayCalc;

	public int getMaxSteps() {
		return maxSteps;
	}

	public void setMaxSteps(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public double getSpeedFactor() {
		return speedFactor;
	}

	public void setSpeedFactor(double speedFactor) {
		this.speedFactor = speedFactor;
	}

	public Set<Flow> getFlows() {
		return flows;
	}

	public void setFlows(Set<Flow> flows) {
		this.flows = flows;
	}

	public NetworkParser getParser() {
		return parser;
	}

	public void setParser(NetworkParser parser) {
		this.parser = parser;
	}

	public UbsDelayCalc getDelayCalc() {
		return delayCalc;
	}

	public void setDelayCalc(UbsDelayCalc delayCalc) {
		this.delayCalc = delayCalc;
	}

	public int getMaxPrio() {
		return maxPrio;
	}

	public void setMaxPrio(int maxPrio) {
		this.maxPrio = maxPrio;
	}

}
