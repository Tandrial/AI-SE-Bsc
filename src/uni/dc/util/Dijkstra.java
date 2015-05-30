package uni.dc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;

public class Dijkstra {

	EgressTopology topo;

	private Set<EgressPort> unSettledNodes;
	private Map<EgressPort, EgressPort> predecessors;
	private Map<EgressPort, Float> distance;

	public Dijkstra(EgressTopology topo) {
		this.topo = topo;
	}

	public void execute(EgressPort src) {
		unSettledNodes = new HashSet<EgressPort>();
		distance = new HashMap<EgressPort, Float>();
		predecessors = new HashMap<EgressPort, EgressPort>();
		distance.put(src, 0f);
		unSettledNodes.add(src);

		while (unSettledNodes.size() > 0) {
			EgressPort node = getMinimum(unSettledNodes);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	public LinkedList<EgressPort> getPath(EgressPort dest) {
		LinkedList<EgressPort> path = new LinkedList<EgressPort>();
		EgressPort step = dest;
		if (predecessors.get(step) == null)
			return null;
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		Collections.reverse(path);
		return path;
	}

	private void findMinimalDistances(EgressPort dest) {
		List<EgressPort> adjacentNodes = new ArrayList<EgressPort>(
				topo.getReachablePorts(dest));
		for (EgressPort target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(dest) + 1f) {
				distance.put(target, getShortestDistance(dest) + 1f);
				predecessors.put(target, dest);
				unSettledNodes.add(target);
			}
		}
	}

	private EgressPort getMinimum(Set<EgressPort> dest) {
		EgressPort minimum = null;
		for (EgressPort k : dest) {
			if (minimum == null) {
				minimum = k;
			} else {
				if (getShortestDistance(k) < getShortestDistance(minimum)) {
					minimum = k;
				}
			}
		}
		return minimum;
	}

	private float getShortestDistance(EgressPort dest) {
		Float d = distance.get(dest);
		if (d == null)
			return Float.MAX_VALUE;
		else
			return d;
	}

}
