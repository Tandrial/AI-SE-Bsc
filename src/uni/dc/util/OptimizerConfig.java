package uni.dc.util;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;

public class OptimizerConfig {
	private int maxPrio = 2;
	private int maxSteps = 1000;
	private int runs = 20;

	private double speedFactor = 0.1d;
	private Traffic traffic;
	private EgressTopology topology;
	private PriorityConfiguration prio;
	private UbsDelayCalc delayCalc;

	public OptimizerConfig(EgressTopology topology, Traffic traffic,
			UbsDelayCalc delayCalc) {
		this.topology = topology;
		this.traffic = traffic;
		this.delayCalc = delayCalc;
	}

	public int getMaxPrio() {
		return maxPrio;
	}

	public void setMaxPrio(int maxPrio) {
		this.maxPrio = maxPrio;
	}

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

	public Traffic getTraffic() {
		return traffic;
	}

	public void setTraffic(Traffic traffic) {
		this.traffic = traffic;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public PriorityConfiguration getPriorityConfig() {
		return prio;
	}

	public void setPriorityConfig(PriorityConfiguration prio) {
		this.prio = prio;
	}

	public UbsDelayCalc getDelayCalc() {
		return delayCalc;
	}

	public void setDelayCalc(UbsDelayCalc delayCalc) {
		this.delayCalc = delayCalc;
	}
}
