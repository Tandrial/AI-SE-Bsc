package uni.dc.networkGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;

public class RandomTopologyGenerator {

	private int ports;
	private int depth;
	private double linkSpeed;
	private Random rng;

	EgressTopology topo = new EgressTopology();
	Map<Integer, LinkedHashSet<EgressPort>> rankPortMap = new TreeMap<Integer, LinkedHashSet<EgressPort>>();
	Set<Cluster> clusterSet = new LinkedHashSet<Cluster>();
	Map<EgressPort, Set<Cluster>> portClusterMap = new HashMap<EgressPort, Set<Cluster>>();

	public RandomTopologyGenerator() {
		super();
	}

	public int getPorts() {
		return ports;
	}

	public void setPorts(int ports) {
		this.ports = ports;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public double getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(double linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public Random getRng() {
		return rng;
	}

	public void setRng(Random rng) {
		this.rng = rng;
	}

	private class Cluster extends LinkedHashSet<EgressPort> {
		private static final long serialVersionUID = 2435807632577734524L;

		@Override
		public String toString() {
			return String.format("%x", this.hashCode()) + super.toString();
		}
	}

	private void clear() {
		topo = new EgressTopology();
		rankPortMap.clear();
		clusterSet.clear();
		portClusterMap.clear();
	}

	/**
	 * Can be used after generate() to retrieve the ranks of the ports (avoids
	 * DAG discovery in Topology/ keeps Topology lightweight).
	 * 
	 * @return
	 */
	public Map<Integer, LinkedHashSet<EgressPort>> getRankPortMap() {
		return rankPortMap;
	}

	private void connect(EgressPort src, EgressPort dest) {
		topo.addLink(src, dest);

		src.setLinkSpeed(linkSpeed);
		dest.setLinkSpeed(linkSpeed);

		Cluster joined;
		if (portClusterMap.get(src).size() > 0) {
			joined = portClusterMap.get(src).iterator().next();
		} else if (portClusterMap.get(dest).size() > 0) {
			joined = portClusterMap.get(dest).iterator().next();
		} else {
			joined = new Cluster();
			clusterSet.add(joined);
		}

		// Next code adds the ports of all clusters of src port
		// to the joined cluster
		Set<Cluster> fromClusters = portClusterMap.get(src);
		for (Cluster c : fromClusters) {
			if (c != joined) {
				clusterSet.remove(c);
				joined.addAll(c);
			}
		}
		if (fromClusters.size() == 0)
			joined.add(src);

		// Next code adds the ports of all clusters of dest port
		// to the joined cluster
		Set<Cluster> toClusters = portClusterMap.get(dest);
		for (Cluster c : toClusters) {
			if (c != joined) {
				clusterSet.remove(c);
				joined.addAll(c);
			}
		}
		if (toClusters.size() == 0)
			joined.add(dest);

		// Next code cleans up the portClusterMap: all ports
		// of the joined cluster shall only have the joined cluster
		// associated.
		for (EgressPort p : joined) {
			portClusterMap.get(p).clear();
			portClusterMap.get(p).add(joined);
		}
	}

	private Set<Cluster> clustersOf(EgressPort p) {
		return portClusterMap.get(p);
	}

	public EgressTopology generate() {
		if (depth < 1)
			throw new RuntimeException("Depth must be > 1!");
		if (depth > ports)
			throw new RuntimeException("There must be more ports than depth to generate!");

		for (int tryCnt = 0; tryCnt < Integer.MAX_VALUE; tryCnt++) {
			try {
				clear();
				// Initialize rank port map with ports per rank
				for (int rank = 0; rank < depth; rank++) {
					rankPortMap.put(rank, new LinkedHashSet<EgressPort>());
					rankPortMap.get(rank).add(new EgressPort(String.format("E%d,1", rank)));
				}
				int remainingPorts = ports - depth;

				// Add each remaining port at random rank
				for (int i = 0; i < remainingPorts; i++) {

					int rank = rng.nextInt(depth);

					EgressPort port = new EgressPort(String.format("E%d,%d", rank, rankPortMap.get(rank).size() + 1));

					rankPortMap.get(rank).add(port);
				}

				// Initialize port->cluster map: No port belongs to a cluster
				for (Set<EgressPort> portsAtRank : rankPortMap.values())
					for (EgressPort p : portsAtRank)
						portClusterMap.put(p, new LinkedHashSet<Cluster>());

				// Add all ports to topology
				for (Set<EgressPort> rankPorts : rankPortMap.values()) {
					topo.addAll(rankPorts);
				}

				// Connect rank r randomly to rank d+1
				for (int rank = 0; rank < depth - 1; rank++) {

					// System.out.printf("## Rank = %d\n",rank);
					LinkedHashSet<EgressPort> portsAtCurrentRank = rankPortMap.get(rank);
					LinkedHashSet<EgressPort> portsAtNextRank = rankPortMap.get(rank + 1);
					if (portsAtNextRank.size() == 0)
						portsAtNextRank = rankPortMap.get(rank + 2);

					// Identify new clusters at current rank:
					// - Unconnected ports at current rank are clusters
					// - Clusters in clusterList are clusters (already existing)
					for (EgressPort p : portsAtCurrentRank) {
						if (portClusterMap.get(p).isEmpty()) {
							Cluster c = new Cluster();
							c.add(p);
							portClusterMap.get(p).add(c);
							clusterSet.add(c);
						}
					}

					// 1. Connected every cluster C at rank to one random port p
					// at
					// next
					// rank
					// Assures that all clusters cover all ranks until the last
					// rank

					// This map is used to avoid concurrent modification by
					// connect()
					Map<EgressPort, EgressPort> clusterForwardPortMap = new HashMap<EgressPort, EgressPort>();
					for (Cluster c : clusterSet) {
						List<EgressPort> clusterPortsAtCurrentRank = new ArrayList<EgressPort>(c);
						clusterPortsAtCurrentRank.retainAll(portsAtCurrentRank);
						EgressPort srcPort = clusterPortsAtCurrentRank
								.get(rng.nextInt(clusterPortsAtCurrentRank.size()));
						EgressPort destPort = new ArrayList<EgressPort>(portsAtNextRank)
								.get(rng.nextInt(portsAtNextRank.size()));
						clusterForwardPortMap.put(srcPort, destPort);
					}
					for (EgressPort srcPort : clusterForwardPortMap.keySet()) {
						connect(srcPort, clusterForwardPortMap.get(srcPort));
					}

					// 2. Connect random clusters to random ports at next rank
					// Conditions:
					// - A cluster C is not allowed to connect to port of C
					// - All ports of the last rank must be connected to a
					// cluster
					// - There must be exactly one cluster at the last rank

					List<EgressPort> nextRankPortList = new ArrayList<EgressPort>(portsAtNextRank);
					Collections.shuffle(nextRankPortList, rng);
					for (EgressPort d : nextRankPortList) {
						List<Cluster> C = new ArrayList<Cluster>(clusterSet);
						C.removeAll(clustersOf(d));
						Collections.shuffle(C, rng);

						if (rank < depth - 2)
							C = C.subList(0, rng.nextInt(C.size() + 1));
						for (Cluster c : C) {
							List<EgressPort> clusterPortsAtCurrentRank = new ArrayList<EgressPort>(c);
							clusterPortsAtCurrentRank.retainAll(portsAtCurrentRank);
							EgressPort s = clusterPortsAtCurrentRank.get(rng.nextInt(clusterPortsAtCurrentRank.size()));
							connect(s, d);
						}
					}
				}
				break;
			} catch (Exception e) {
			}
		}
		return topo;
	}
}
