package uni.dc.networkGenerator;

import java.util.Random;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;

public class GeneratorAPI {

	private static GeneratorAPI generator = new GeneratorAPI();

	public static GeneratorAPI getGenerator() {
		return GeneratorAPI.generator;
	}

	private static Random rng;
	private Traffic traffic = null;
	private PriorityConfiguration cfg = null;
	private EgressTopology topology = null;

	private GeneratorAPI() {
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public Traffic getTraffic() {
		return traffic;
	}

	public PriorityConfiguration getPriorityConfiguration() {
		return cfg;
	}

	public void setRandom(Random rng) {
		this.rng = rng;
	}

	public void generateNetwork(int depth, int portCount, int maxPrio, double linkSpeed, int maxFrameLength,
			int maxSpeed) {
		genTopology(depth, portCount, linkSpeed);
		genTraffic(linkSpeed, maxFrameLength, maxSpeed);
		genPrio(maxPrio);
	}

	public void genTopology(int depth, int portCount, double linkSpeed) {
		RandomTopologyGenerator topologyGen = new RandomTopologyGenerator();
		topologyGen.setRng(rng);
		topologyGen.setDepth(depth);
		topologyGen.setPorts(portCount);
		topologyGen.setLinkSpeed(linkSpeed);

		topology = topologyGen.generate();
	}

	public void genTraffic(double linkSpeed, int maxFrameLength, int maxLeakRateinPercent) {
		if (topology == null)
			return;
		RandomMulticastPathGenerator flowPathGen = new RandomMulticastPathGenerator();
		flowPathGen.setTopology(topology);
		flowPathGen.setRng(rng);
		flowPathGen.setMinFlowPerPort(3);
		flowPathGen.setMaxDestPerFlow(1);
		flowPathGen.setLinkSpeed(linkSpeed);
		flowPathGen.setMaxFrameLength(maxFrameLength);
		flowPathGen.setMaxSpeedPerStream(maxLeakRateinPercent);

		traffic = flowPathGen.generate();
	}

	public void genPrio(int maxPrio) {
		if (traffic == null)
			return;
		RandomPriorityGenerator prioGen = new RandomPriorityGenerator();
		prioGen.setTraffic(traffic);
		prioGen.setRng(rng);
		prioGen.setMaxPrio(maxPrio);

		cfg = prioGen.generate();
	}
}
