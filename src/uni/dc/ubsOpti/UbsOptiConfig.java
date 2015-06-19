package uni.dc.ubsOpti;

import java.io.Serializable;
import java.util.Random;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.GeneratorAPI;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV3DelayCalc;
import uni.dc.ubsOpti.tracer.TraceCollection;

public class UbsOptiConfig implements Serializable {

	private static Random rng;

	private static final long serialVersionUID = 1L;
	private int depth = 6;
	private int portCount = 9;
	private int maxFrameLength = 12350;
	private int maxLeakRateinPercent = 10;
	private long seed = 0x31337;
	private double modifier = 1.0d;
	private double linkSpeed = 1e9;
	private int maxPrio = 2;
	private int maxSteps = 7500000;
	private int runs = 1;
	private int dim;

	private boolean ubsV0 = true;

	private Traffic traffic = null;
	private EgressTopology topology = null;
	private PriorityConfiguration prio = null;
	private UbsDelayCalc delayCalc = null;
	private TraceCollection traces = null;

	public UbsOptiConfig() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NetworkConfig");
		sb.append("Traffic Model : " + (ubsV0 ? "Ubs V0" : "Ubs V3"));
		sb.append("\nmaxPrio:" + maxPrio);
		sb.append("\nmaxSteps per run:" + maxSteps);
		sb.append("\nRun Count:" + runs);
		sb.append("\n\nStreams and Delays: \n" + delayCalc);
		sb.append("\nbest PriorityConfig: \n" + prio);
		return sb.toString();
	}

	public UbsOptiConfig(EgressTopology topology, Traffic traffic, PriorityConfiguration prio, UbsDelayCalc delayCalc,
			TraceCollection traces) {
		this.topology = topology;
		this.traffic = traffic;
		this.prio = prio;
		dim = prio.toIntArray().length;
		this.delayCalc = delayCalc;
		this.traces = traces;
	}

	public void fromParser(NetworkParser parser) {
		topology = parser.getTopology();
		traffic = parser.getTraffic();
		prio = parser.getPriorityConfig();
		dim = prio.toIntArray().length;
		delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(traffic);
		delayCalc.calculateDelays(prio);
		traces = new TraceCollection();
	}

	public void fromGenerator() {
		GeneratorAPI generator = GeneratorAPI.getGenerator();
		generator.setRandom(rng);
		generator.generateNetwork(depth, portCount, maxPrio, linkSpeed, maxFrameLength, maxLeakRateinPercent);
		topology = generator.getTopology();
		traffic = generator.getTraffic();
		prio = generator.getPriorityConfiguration();
		dim = prio.toIntArray().length;
		delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(traffic);
		delayCalc.setInitialDelays(prio, 1.0d);
		traces = new TraceCollection();
	}

	public void newTopology() {
		GeneratorAPI generator = GeneratorAPI.getGenerator();
		generator.setRandom(rng);
		generator.genTopology(depth, portCount, linkSpeed);
		generator.genTraffic(linkSpeed, maxFrameLength, maxLeakRateinPercent);
		generator.genPrio(maxPrio);
		topology = generator.getTopology();
		traffic = generator.getTraffic();
		prio = generator.getPriorityConfiguration();
		dim = prio.toIntArray().length;
		delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(traffic);
		delayCalc.setInitialDelays(prio, modifier);
		traces = new TraceCollection();

	}

	public void newTraffic() {
		GeneratorAPI generator = GeneratorAPI.getGenerator();
		generator.genTraffic(linkSpeed, maxFrameLength, maxLeakRateinPercent);
		generator.genPrio(maxPrio);
		traffic = generator.getTraffic();
		prio = generator.getPriorityConfiguration();
		dim = prio.toIntArray().length;
		delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(traffic);
		delayCalc.setInitialDelays(prio, modifier);
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

	public int getMaxFrameLength() {
		return maxFrameLength;
	}

	public void setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}

	public int getMaxSpeed() {
		return maxLeakRateinPercent;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxLeakRateinPercent = maxSpeed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		rng = new Random(seed);
	}

	public long getSeed() {
		return seed;
	}

	public double getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(double linkSpeed) {
		this.linkSpeed = linkSpeed;
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

	public boolean isUbsV0() {
		return ubsV0;
	}

	public void setUbsV0(boolean ubsV0) {
		this.ubsV0 = ubsV0;
	}

	public void setBestConfig() {
		prio = traces.getBestConfig();
	}

	public PriorityConfiguration getBestConfig() {
		return traces.getBestConfig();
	}

	public double getModifier() {
		return modifier;
	}

	public void setModifier(double modifier) {
		this.modifier = modifier;
	}
}
