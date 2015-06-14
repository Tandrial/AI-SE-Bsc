package uni.dc.ubsOpti;

import java.io.Serializable;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.GeneratorAPI;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV3DelayCalc;
import uni.dc.ubsOpti.tracer.TraceCollection;

public class UbsOptiConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	private int depth = 6;
	private int portCount = 9;
	private int maxPrio = 5;
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

	public UbsOptiConfig(NetworkParser parser, boolean v0) {
		topology = parser.getTopology();
		traffic = parser.getTraffic();
		prio = parser.getPriorityConfig();
		delayCalc = v0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(
				traffic);
		delayCalc.calculateDelays(prio);
		traces = new TraceCollection();
	}

	public UbsOptiConfig(GeneratorAPI generator, boolean v0) {
		generator.generateNetwork(depth, portCount, maxPrio);
		topology = generator.getTopology();
		traffic = generator.getTraffic();
		prio = generator.getPriorityConfiguration();
		delayCalc = v0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(
				traffic);
		delayCalc.setInitialDelays(prio);
		traces = new TraceCollection();
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getPortCount() {
		return portCount;
	}

	public void setPortCount(int portCount) {
		this.portCount = portCount;
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

	public void setDim(int dim) {
		this.dim = dim;
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

	public void setBestConfig() {
		prio = traces.getBestConfig();
	}
}
