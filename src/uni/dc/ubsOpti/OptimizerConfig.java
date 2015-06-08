package uni.dc.ubsOpti;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;

public class OptimizerConfig {
	private int maxPrio = 2;
	private int maxSteps = 3000000;
	private int runs = 2;

	private Traffic traffic;
	private EgressTopology topology;
	private PriorityConfiguration prio;
	private UbsDelayCalc delayCalc;

	public OptimizerConfig(EgressTopology topology, Traffic traffic,
			PriorityConfiguration prio, UbsDelayCalc delayCalc) {
		this.topology = topology;
		this.traffic = traffic;
		this.prio = prio;
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
