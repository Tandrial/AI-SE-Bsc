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

	public void generateNetwork(int depth, int portCount, int maxPrio) {
		try {
			RandomTopologyGenerator topologyGen = new RandomTopologyGenerator();
			topologyGen.setRng(new Random(System.currentTimeMillis()));
			topologyGen.setDepth(depth);
			topologyGen.setPorts(portCount);
			topology = topologyGen.generate();

			RandomMulticastPathGenerator flowPathGen = new RandomMulticastPathGenerator();
			flowPathGen.setTopology(topology);
			flowPathGen.setRng(new Random(System.currentTimeMillis()));
			flowPathGen.setMinFlowPerPort(3);
			flowPathGen.setMaxDestPerFlow(1);

			traffic = flowPathGen.generate();

			RandomPriorityGenerator prioGen = new RandomPriorityGenerator();
			prioGen.setTraffic(traffic);
			prioGen.setMaxPrio(maxPrio);

			cfg = prioGen.generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
