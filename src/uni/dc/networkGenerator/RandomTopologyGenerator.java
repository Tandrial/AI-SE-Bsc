package uni.dc.networkGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;
import uni.dc.util.DeterministicHashSet;

public class RandomTopologyGenerator {
	
	public static final double LINK_SPEED = 1e9;
	
	private int ports;
	private int depth;
	private Random rng;

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

	public Random getRng() {
		return rng;
	}

	public void setRng(Random rng) {
		this.rng = rng;
	}

	private class Cluster extends DeterministicHashSet<EgressPort> {
		private static final long serialVersionUID = 2435807632577734524L;

		@Override
		public String toString() {
			return String.format("%x", this.hashCode()) + super.toString();
		}
	}

	EgressTopology topo = new EgressTopology();
	Map<Integer, DeterministicHashSet<EgressPort>> rankPortMap = new TreeMap<Integer, DeterministicHashSet<EgressPort>>();
	Set<Cluster> clusterSet = new DeterministicHashSet<Cluster>();
	Map<EgressPort, Set<Cluster>> portClusterMap = new HashMap<EgressPort, Set<Cluster>>();

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
	public Map<Integer, DeterministicHashSet<EgressPort>> getRankPortMap() {
		return rankPortMap;
	}

	private void connect(EgressPort src, EgressPort dest) {
		topo.addLink(src, dest);
		
		src.setLinkSpeed(LINK_SPEED);
		dest.setLinkSpeed(LINK_SPEED);

		// System.out.printf("Connecting %s -> %s\n",src,dest);
		// System.out.printf(" - clusterSet before: %s\n",clusterSet);
		// Next code uses the first cluster of the src port (if present)
		// as the joined cluster.
		// Motivation:
		// Avoids modification of clusterSet if src is in a cluster and
		// dest is not. This avoids concurrent modification on iteration
		// over all clusters in clusterSet.
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

		// System.out.printf(" - clusterSet after: %s\n",clusterSet);
	}

	private Set<Cluster> clustersOf(EgressPort p) {
		return portClusterMap.get(p);
	}

	public EgressTopology generate() {

		if (depth < 1)
			throw new RuntimeException("Depth must be > 1!");

		if (depth > ports)
			throw new RuntimeException(
					"There must be more ports than depth to generate!");

		// Initialize rank port map with ports per rank
		for (int rank = 0; rank < depth; rank++) {
			rankPortMap.put(rank, new DeterministicHashSet<EgressPort>());
			rankPortMap.get(rank).add(
					new EgressPort(String.format("E%d,1", rank)));
		}
		int remainingPorts = ports - depth;

		// Add each remaining port at random rank
		for (int i = 0; i < remainingPorts; i++) {

			int rank = rng.nextInt(depth);

			EgressPort port = new EgressPort(String.format("E%d,%d", rank,
					rankPortMap.get(rank).size() + 1));

			rankPortMap.get(rank).add(port);
		}

		// Initialize port->cluster map: No port belongs to a cluster
		for (Set<EgressPort> portsAtRank : rankPortMap.values())
			for (EgressPort p : portsAtRank)
				portClusterMap.put(p, new DeterministicHashSet<Cluster>());

		// Add all ports to topology
		for (Set<EgressPort> rankPorts : rankPortMap.values()) {
			topo.addAll(rankPorts);
		}

		// Connect rank r randomly to rank d+1
		for (int rank = 0; rank < depth - 1; rank++) {

			// System.out.printf("## Rank = %d\n",rank);
			DeterministicHashSet<EgressPort> portsAtCurrentRank = rankPortMap
					.get(rank);
			DeterministicHashSet<EgressPort> portsAtNextRank = rankPortMap
					.get(rank + 1);

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

			// 1. Connected every cluster C at rank to one random port p at next
			// rank
			// Assures that all clusters cover all ranks until the last rank

			// System.out.println("-- Step 1 --");

			// This map is used to avoid concurrent modification by connect()
			Map<EgressPort, EgressPort> clusterForwardPortMap = new HashMap<EgressPort, EgressPort>();
			for (Cluster c : clusterSet) {
				List<EgressPort> clusterPortsAtCurrentRank = new ArrayList<EgressPort>(
						c);
				clusterPortsAtCurrentRank.retainAll(portsAtCurrentRank);
				EgressPort srcPort = clusterPortsAtCurrentRank.get(rng
						.nextInt(clusterPortsAtCurrentRank.size()));
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
			// - All ports of the last rank must be connected to a cluster
			// - There must be exactly one cluster at the last rank
			//
			//
			// Destination Port centric approach:
			// for each Port d in shuffle(portsAtNextRank) {
			// if (rank < lastRank-1) C = selectRandomClusterSubSet(clusterSet
			// without cluster(d))
			// else C = clusterSet without cluster(d)
			// for each cluster c : C {
			// s = selectRandomPort (c intersect portsAtCurrentRank)
			// connect(s,d) // Cleans up portClusterMap and clusterSet
			// }
			// }
			//
			// OPEN Issue:
			// Assure that all clusters are connected at the last rank
			// System.out.println("-- Step 2 --");
			List<EgressPort> nextRankPortList = new ArrayList<EgressPort>(
					portsAtNextRank);
			Collections.shuffle(nextRankPortList, rng);
			for (EgressPort d : nextRankPortList) {
				List<Cluster> C = new ArrayList<Cluster>(clusterSet);
				C.removeAll(clustersOf(d));
				Collections.shuffle(C, rng);

				if (rank < depth - 2)
					C = C.subList(0, rng.nextInt(C.size() + 1));
				for (Cluster c : C) {
					List<EgressPort> clusterPortsAtCurrentRank = new ArrayList<EgressPort>(
							c);
					clusterPortsAtCurrentRank.retainAll(portsAtCurrentRank);
					EgressPort s = clusterPortsAtCurrentRank.get(rng
							.nextInt(clusterPortsAtCurrentRank.size()));
					connect(s, d);
				}
			}
		}
		return topo;
	}

	public EgressTopology generate2() {
		clear();
		EgressTopology topo = new EgressTopology();

		if (depth < 1)
			throw new RuntimeException("Depth must be > 1!");

		if (depth > ports)
			throw new RuntimeException(
					"There must be more ports than depth to generate!");

		// Initialize rank port map with ports per rank
		for (int rank = 0; rank < depth; rank++) {
			rankPortMap.put(rank, new DeterministicHashSet<EgressPort>());
			rankPortMap.get(rank).add(
					new EgressPort(String.format("E%d,1", rank)));
		}
		int remainingPorts = ports - depth;

		// Add each remaining port at random rank
		for (int i = 0; i < remainingPorts; i++) {

			int rank = rng.nextInt(depth);

			EgressPort port = new EgressPort(String.format("E%d,%d", rank,
					rankPortMap.get(rank).size() + 1));

			rankPortMap.get(rank).add(port);
		}

		// Initialize port->cluster map: No port belongs to a cluster
		for (Set<EgressPort> portsAtRank : rankPortMap.values())
			for (EgressPort p : portsAtRank)
				portClusterMap.put(p, new DeterministicHashSet<Cluster>());

		// Add all ports to topology
		for (Set<EgressPort> rankPorts : rankPortMap.values()) {
			topo.addAll(rankPorts);
		}

		// Connect rank r randomly to rank d+1
		for (int rank = 0; rank < depth - 1; rank++) {

			// System.out.printf("## Rank = %d\n",rank);
			DeterministicHashSet<EgressPort> portsAtCurrentRank = rankPortMap
					.get(rank);
			DeterministicHashSet<EgressPort> portsAtNextRank = rankPortMap
					.get(rank + 1);

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
			System.out.printf("Rank=%d  Clusters = %s\n", rank, clusterSet);

			// 1. Connected every cluster C at rank to one random port p at next
			// rank
			// Assures that all clusters cover all ranks until the last rank
			// 2. Connect random clusters to random ports at next rank
			// Conditions:
			// - A cluster C is not allowed to connect to port of C
			// - All ports of the last rank must be connected to a cluster
			// - There must be exactly one cluster at the last rank
			//
			// Step 2:
			// Approach from left to right:
			// boolean fail=false;
			// while (not fail){
			// c = selectRandomCluster(all Clusters)
			// s = selectRandomPort(c intersect portsAtCurrentRank)
			// d = selectRandomPort(portsAtNextRank)
			// if (d not in c) c.add(p);
			// else fail=true;
			// }
			//
			// Destination Port centric approach:
			// for each Port d in shuffle(portsAtNextRank) {
			// if (rank < lastRank)
			// C = selectRandomClusterSet(clusterSet without cluster(d))
			// else
			// C = clusterSet without cluster(d)
			// for each cluster c : C {
			// s = selectRandomPort (c intersect portsAtCurrentRank)
			// connect(s,d) // Cleans up portClusterMap and clusterSet
			// }
			// }
			//
			// OPEN Issue:
			// Assure that all clusters are connected at the last rank
			//
			// ---------------------------------------------------------------
			// Generate random connections per cluster c:
			// - Identify a random set of ports destPorts with
			// 1. |destPorts| > 0 AND
			// 2. destPorts in portsAtNextRank
			// - Connect each port dest in destPorts to a random port src with
			// 1. src in c AND
			// 2. src in portsAtCurrentRank
			for (Cluster c : clusterSet) {

				List<EgressPort> destPorts = new ArrayList<EgressPort>();
				destPorts.addAll(portsAtNextRank);
				Collections.shuffle(destPorts, rng);
				if (rank < depth - 2)
					destPorts = destPorts.subList(0,
							1 + rng.nextInt(destPorts.size()));

				List<EgressPort> srcPorts = new ArrayList<EgressPort>();
				srcPorts.addAll(portsAtCurrentRank);
				srcPorts.retainAll(c);

				// System.out.printf("  srcPorts=%s, destPorts=%s\n",
				// srcPorts,destPorts);
				for (EgressPort dest : destPorts) {
					EgressPort src = srcPorts.get(rng.nextInt(srcPorts.size()));
					topo.addLink(src, dest);
					portClusterMap.get(dest).add(c);
					c.add(dest);
				}
			}

			// Update clusters
			// - Join clusters that share ports
			for (EgressPort p : portsAtNextRank) {
				if (portClusterMap.get(p).size() > 1) {
					Set<Cluster> clustersAtPort = portClusterMap.get(p);

					// System.out.printf("Port %s clusters = %s\n",p,clustersAtPort);
					// Build a new common cluster
					Cluster cNew = new Cluster();
					for (Cluster c : clustersAtPort) {
						clusterSet.remove(c);
						cNew.addAll(c);
					}
					clusterSet.add(cNew);
					// System.out.printf("Port %s new cluster = %s\n",p,cNew);

					// Remove the old split clusters per port in port->Cluster
					// map
					// and if at least one cluster was removed, add the new
					// common cluster

					for (EgressPort p2 : portClusterMap.keySet()) {
						Set<Cluster> clustersAtP2 = portClusterMap.get(p2);
						if (clustersAtP2.removeAll(clustersAtPort)) {
							clustersAtP2.add(cNew);
						}

					}

				}
			}
			// System.out.printf("  Port cluster map = %s\n",portClusterMap);
		}
		return topo;
	}

}
