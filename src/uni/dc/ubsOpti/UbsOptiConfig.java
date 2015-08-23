package uni.dc.ubsOpti;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.GeneratorAPI;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV3DelayCalc;
import uni.dc.ubsOpti.tracer.AllTracer;
import uni.dc.ubsOpti.tracer.BestOnlyTracer;
import uni.dc.ubsOpti.tracer.EndStepTracer;
import uni.dc.ubsOpti.tracer.SingleBestTracer;
import uni.dc.ubsOpti.tracer.Tracer;

public class UbsOptiConfig implements Serializable {

	private static long seed = 0x1337;
	private static Random rng = new Random(seed);

	private static final long serialVersionUID = 1L;
	private int depth = 6;
	private int portCount = 9;
	private int maxFlowCount = 6;
	private int maxFrameLength = 12350;
	private int maxLeakRateinPercent = 10;
	private double modifier = 1.0d;
	private double linkSpeed = 1e9;
	private int maxPrio = 2;
	private BigInteger maxSteps = new BigInteger("500000");
	private int runs = 1;
	private int dim;

	private boolean ubsV0 = true;

	private Traffic traffic = null;
	private EgressTopology topology = null;
	private PriorityConfiguration prio = null;
	private UbsDelayCalc delayCalc = null;

	private BestOnlyTracer bestOnlyTracer;
	private SingleBestTracer singleBestTracer;
	private EndStepTracer endStepTracer;
	private AllTracer allTracer;

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
		sb.append("\n\nPriorityConfig: \n" + prio);
		sb.append("\nStreams and Delays: \n" + delayCalc);
		return sb.toString();
	}

	public UbsOptiConfig(EgressTopology topology, Traffic traffic, PriorityConfiguration prio, UbsDelayCalc delayCalc) {
		this.topology = topology;
		this.traffic = traffic;
		this.prio = prio;
		dim = prio.toIntArray().length;
		this.delayCalc = delayCalc;
	}

	public void fromParser(NetworkParser parser) {
		topology = parser.getTopology();
		traffic = parser.getTraffic();
		prio = parser.getPriorityConfig();
		maxSteps = prio.calcMaxSteps(maxPrio);
		dim = prio.toIntArray().length;
		delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(traffic);
		delayCalc.calculateDelays(prio);
	}

	public void fromGenerator() {
		GeneratorAPI generator = GeneratorAPI.getGenerator();
		generator.setRandom(rng);
		generator.generateNetwork(depth, portCount, maxPrio, linkSpeed, maxFrameLength, maxLeakRateinPercent,
				maxFlowCount);
		topology = generator.getTopology();
		traffic = generator.getTraffic();
		genRemaining(generator);

		delayCalc.setInitialDelays(prio, modifier);
	}

	public void newTopology() {
		GeneratorAPI generator = GeneratorAPI.getGenerator();
		generator.setRandom(rng);
		generator.genTopology(depth, portCount, linkSpeed);
		topology = generator.getTopology();
		newTraffic();
	}

	public void newTraffic() {
		GeneratorAPI generator = GeneratorAPI.getGenerator();
		generator.genTraffic(linkSpeed, maxFrameLength, maxLeakRateinPercent, maxFlowCount);
		generator.genPrio(maxPrio);
		traffic = generator.getTraffic();
		genRemaining(generator);
		delayCalc.setInitialDelays(prio, modifier);
	}

	public void genRemaining(GeneratorAPI generator) {
		prio = generator.getPriorityConfiguration();
		maxSteps = prio.calcMaxSteps(maxPrio);
		dim = prio.toIntArray().length;
		delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic) : new UbsV3DelayCalc(traffic);
	}

	public List<Tracer> resetAllTracers() {
		bestOnlyTracer = new BestOnlyTracer();
		singleBestTracer = new SingleBestTracer();
		endStepTracer = new EndStepTracer();
		allTracer = new AllTracer();
		ArrayList<Tracer> tracers = new ArrayList<Tracer>();
		tracers.add(bestOnlyTracer);
		tracers.add(singleBestTracer);
		tracers.add(endStepTracer);
		tracers.add(allTracer);
		return tracers;
	}

	public BestOnlyTracer newBestOnlyTracer() {
		bestOnlyTracer = new BestOnlyTracer();
		return bestOnlyTracer;
	}

	public SingleBestTracer newSingleBestTracer() {
		singleBestTracer = new SingleBestTracer();
		return singleBestTracer;
	}

	public EndStepTracer newEndStepTracer() {
		endStepTracer = new EndStepTracer();
		return endStepTracer;
	}

	public AllTracer newAllTracer() {
		allTracer = new AllTracer();
		return allTracer;
	}

	public BestOnlyTracer getBestOnlyTracer() {
		return bestOnlyTracer;
	}

	public SingleBestTracer getSingleBestTracer() {
		return singleBestTracer;
	}

	public EndStepTracer getEndStepTracer() {
		return endStepTracer;
	}

	public AllTracer getAllTracer() {
		return allTracer;
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

	public int getMaxFlowCount() {
		return maxFlowCount;
	}

	public void setFlowCount(int maxFlowCount) {
		this.maxFlowCount = maxFlowCount;
	}

	public void setSeed(long seed) {
		UbsOptiConfig.seed = seed;
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

	public BigInteger getMaxSteps() {
		return maxSteps;
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

	public void setPriorityConfig(int[] prio) {
		this.prio.fromIntArray(prio);
	}

	public UbsDelayCalc getDelayCalc() {
		return delayCalc;
	}

	public void setDelayCalc(UbsDelayCalc delayCalc) {
		this.delayCalc = delayCalc;
	}

	public boolean isUbsV0() {
		return ubsV0;
	}

	public void setUbsV0(boolean ubsV0) {
		this.ubsV0 = ubsV0;
	}

	public double getModifier() {
		return modifier;
	}

	public void setModifier(double modifier) {
		this.modifier = modifier;
	}
}
