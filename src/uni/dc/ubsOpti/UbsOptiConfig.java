package uni.dc.ubsOpti;

import java.io.Serializable;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.Tracer.TraceCollection;

public class UbsOptiConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	private int maxPrio = 2;
	private int maxSteps = 7500000;
	private int runs = 1;
	private int dim;

	private Traffic traffic;
	private EgressTopology topology;
	private PriorityConfiguration prio;
	private UbsDelayCalc delayCalc;
	private TraceCollection traces;

	public UbsOptiConfig(EgressTopology topology, Traffic traffic,
			PriorityConfiguration prio, UbsDelayCalc delayCalc,
			TraceCollection traces) {
		this.topology = topology;
		this.traffic = traffic;
		this.prio = prio;
		dim = prio.toIntArray().length;
		this.delayCalc = delayCalc;
		this.traces = traces;
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

	public int getDim() {
		return dim;
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

	public TraceCollection getTraces() {
		return traces;
	}

	public void setTraces(TraceCollection traces) {
		this.traces = traces;
	}
}
