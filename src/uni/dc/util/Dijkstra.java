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

	private Set<EgressPort> settledNodes;
	private Set<EgressPort> unSettledNodes;
	private Map<EgressPort, EgressPort> predecessors;
	private Map<EgressPort, Float> distance;

	public Dijkstra(EgressTopology topo) {
		this.topo = topo;
	}

	/**
	 * Berechnet die Kantengewichte für die gegebene Quelle
	 * 
	 * @param quelle
	 *            - Legt den Startpunkt fest
	 */
	public void execute(EgressPort quelle) {
		settledNodes = new HashSet<EgressPort>();
		unSettledNodes = new HashSet<EgressPort>();
		distance = new HashMap<EgressPort, Float>();
		predecessors = new HashMap<EgressPort, EgressPort>();
		distance.put(quelle, 0f);
		unSettledNodes.add(quelle);

		while (unSettledNodes.size() > 0) {
			EgressPort node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	/**
	 * Berechnet die günstige Route durch den Graphen
	 * 
	 * @param ziel
	 *            - Legt das Ziel fest
	 * 
	 * @return route - Die Route als LinkedList<EgressPort>
	 */

	public LinkedList<EgressPort> getPath(EgressPort ziel) {
		LinkedList<EgressPort> path = new LinkedList<EgressPort>();
		EgressPort step = ziel;
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

	private void findMinimalDistances(EgressPort knoten) {
		List<EgressPort> adjacentNodes = getNeighbors(knoten);
		for (EgressPort target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(knoten) + 1f) {
				distance.put(target, getShortestDistance(knoten) + 1f);
				predecessors.put(target, knoten);
				unSettledNodes.add(target);
			}
		}
	}

	private List<EgressPort> getNeighbors(EgressPort knoten) {
		List<EgressPort> neighbors = new ArrayList<EgressPort>(
				topo.getReachablePorts(knoten));
		return neighbors;
	}

	private EgressPort getMinimum(Set<EgressPort> knoten) {
		EgressPort minimum = null;
		for (EgressPort k : knoten) {
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

	private boolean isSettled(EgressPort knoten) {
		return settledNodes.contains(knoten);
	}

	private float getShortestDistance(EgressPort destination) {
		Float d = distance.get(destination);
		if (d == null)
			return Float.MAX_VALUE;
		else
			return d;
	}

}
