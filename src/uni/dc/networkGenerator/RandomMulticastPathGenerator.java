package uni.dc.networkGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;
import uni.dc.model.Flow;
import uni.dc.model.Traffic;

public class RandomMulticastPathGenerator {

	private EgressTopology topology;
	private Random rng;

	private int minFlowPerPort = 3;
	// 1: Unicast
	// topology.getPorts().size() : Arbitrary multicast
	private int maxDestPerFlow = 1;

	private int maxFlowCount = 6;

	private double linkSpeed;
	private int maxFrameLength;
	private int maxSpeedPerStream;

	public EgressTopology getTopology() {
		return topology;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public int getMinFlowPerPort() {
		return minFlowPerPort;
	}

	public void setMinFlowPerPort(int minFlowPerPort) {
		this.minFlowPerPort = minFlowPerPort;
	}

	public int getMaxDestPerFlow() {
		return maxDestPerFlow;
	}

	public void setMaxDestPerFlow(int maxDestPerFlow) {
		this.maxDestPerFlow = maxDestPerFlow;
	}

	public int getMaxFlowCount() {
		return maxFlowCount;
	}

	public void setMaxFlowCount(int maxFlowCount) {
		this.maxFlowCount = maxFlowCount;
	}

	public int getMaxFrameLength() {
		return maxFrameLength;
	}

	public void setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}

	public double getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(double linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public int getMaxSpeedPerStream() {
		return maxSpeedPerStream;
	}

	public void setMaxSpeedPerStream(int maxSpeedPerStream) {
		this.maxSpeedPerStream = maxSpeedPerStream;
	}

	public Random getRng() {
		return rng;
	}

	public void setRng(Random rng) {
		this.rng = rng;
	}

	public Traffic generate() {
		Traffic rv = new Traffic(topology);

		int flowId = 0;
		Map<EgressPort, Set<Flow>> portFlowMap = new HashMap<EgressPort, Set<Flow>>();

		for (EgressPort src : topology.getPorts()) {
			portFlowMap.put(src, new LinkedHashSet<Flow>());
		}

		while (flowId < maxFlowCount) {
			for (EgressPort src : topology.getPorts()) {

				// Pick a random sub set of destination ports (at least one)
				List<EgressPort> destPorts = new ArrayList<EgressPort>(topology.getReachablePorts(src));

				while (destPorts.size() > maxDestPerFlow) {
					destPorts.remove(rng.nextInt(destPorts.size() - 1));
				}

				if (destPorts.size() == 0)
					break;

				Collections.shuffle(destPorts);

				if (destPorts.size() > 1) {
					destPorts = destPorts.subList(0, 1 + rng.nextInt(destPorts.size() - 1));
				}

				Flow flow = new Flow();
				flow.setName(String.format("F%d", flowId++));
				flow.setSrcPort(src);
				flow.setDestPort(destPorts.get(0));
				flow.setRate(linkSpeed * ((rng.nextInt(maxSpeedPerStream) + 1)) / 100);
				flow.setMaxFrameLength(rng.nextInt(maxFrameLength) + 1);

				rv.add(flow);

				for (EgressPort dest : destPorts) {
					List<EgressPort> path = topology.getPath(src, dest);
					for (EgressPort p : path) {
						portFlowMap.get(p).add(flow);
					}
				}
			}
		}

		for (EgressPort port : portFlowMap.keySet()) {
			port.setFlowList(portFlowMap.get(port));
		}
		return rv;
	}
}
