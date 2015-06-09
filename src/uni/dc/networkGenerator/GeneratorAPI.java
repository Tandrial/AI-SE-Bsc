package uni.dc.networkGenerator;

import java.util.Random;

import uni.dc.model.EgressTopology;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;

public class GeneratorAPI {

	private static Traffic traffic = null;
	private static PriorityConfiguration cfg = null;
	private static EgressTopology topology = null;
	
	public static EgressTopology getTopology() {
		return topology;
	}

	public static Traffic getTraffic() {
		return traffic;
	}

	public static PriorityConfiguration getPriorityConfiguration() {
		return cfg;
	}

	public static void printGeneratedNetwork() {
		if (traffic == null || cfg == null) {
			System.out
					.println("Need to generate a network before it can be displayed - generateNetwork(depth, portCount)");
			return;
		}
		for (Flow f : traffic) {
			System.out.printf("Flow %s: %s -> %s\n", f.getName(),
					f.getSrcPort(), f.getDestPort());
		}
		System.out.printf("Port -> Set<Flow> map: %s\n",
				traffic.getPortFlowMap());

		System.out.print(cfg);
	}

	public static void generateNetwork(int depth, int portCount, int maxPrio) {
		try {
			RandomTopologyGenerator topologyGen = new RandomTopologyGenerator();
			topologyGen.setRng(new Random());
			topologyGen.setDepth(depth);
			topologyGen.setPorts(portCount);
			topology = topologyGen.generate();

			RandomMulticastPathGenerator flowPathGen = new RandomMulticastPathGenerator();
			flowPathGen.setTopology(topology);
			flowPathGen.setRng(new Random());
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
