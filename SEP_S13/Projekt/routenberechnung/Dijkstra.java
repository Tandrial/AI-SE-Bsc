package routenberechnung;

import java.util.*;
import Graph.*;

public class Dijkstra {

	//START CODE GRUPPE B
	
	private final List<Kante> edges;
	private Set<Knoten> settledNodes;
	private Set<Knoten> unSettledNodes;
	private Map<Knoten, Knoten> predecessors;
	private Map<Knoten, Float> distance;

	/**
	 * Kapslung für eine einmalige Anwendung
	 * 
	 * @param graph
	 *            - Der Graph auf den Dijkstra angewandt wird.
	 * 
	 * @param start
	 *            - Legt den Startpunkt fest.
	 * 
	 * @param ziel
	 *            - Legt den Endpunkt fest.
	 * 
	 * @return route - Die Route als LinkedList<Knoten>
	 */

	public static LinkedList<Knoten> findeWeg(Graph g, Knoten start, Knoten ziel) {
		Dijkstra d = new Dijkstra(g);
		d.execute(start);
		return d.getPath(ziel);
	}

	/**
	 * Erzeugt eine Dijkstra-Instanz für den gegeben Graph
	 * 
	 * @param graph
	 *            - Der Graph auf den Dijkstra angewandt wird.
	 */
	public Dijkstra(Graph graph) {

		this.edges = new ArrayList<Kante>(graph.getAlleKanten());
	}


	/**
	 * Wandelt eine LinkedList<Knoten> in eine LinkedList<Kante> um
	 * 
	 * @param path
	 *            LinkedList<Knoten> - Liste von Knoten
	 * @return LinkedList<Kante> - Liste der Kanten, die auf dem Weg der
	 *         urspruenglichen Knotenliste durchlaufen wurden
	 */
	public static LinkedList<Kante> getKantenPath(LinkedList<Knoten> path) {
		if (path != null) {
			LinkedList<Kante> kList = new LinkedList<Kante>();
			for (int i = 0; i < path.size() - 1; i++) {
				for (Kante k : path.get(i).getKanten()) {
					// Falls dies die Kante ist, die den aktuellen Knoten mit
					// dem
					// naechsten Knoten in der Liste (path) verbindet
					if (k.getOther(path.get(i)) == path.get(i + 1)) {
						kList.add(k);
					}
				}
			}
			return kList;
		} else
			return new LinkedList<Kante>();
	}
	
	//ENDE CODE GRUPPE B	

	// im Original von Lars Vogel http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html  
	// Auf unsere Datenstruktur angepasst

	/**
	 * Berechnet die Kantengewichte für die gegebene Quelle
	 * 
	 * @param quelle
	 *            - Legt den Startpunkt fest
	 */
	public void execute(Knoten quelle) {
		settledNodes = new HashSet<Knoten>();
		unSettledNodes = new HashSet<Knoten>();
		distance = new HashMap<Knoten, Float>();
		predecessors = new HashMap<Knoten, Knoten>();
		distance.put(quelle, 0f);
		unSettledNodes.add(quelle);

		while (unSettledNodes.size() > 0) {
			Knoten node = getMinimum(unSettledNodes);
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
	 * @return route - Die Route als LinkedList<Knoten>
	 */

	public LinkedList<Knoten> getPath(Knoten ziel) {
		LinkedList<Knoten> path = new LinkedList<Knoten>();
		Knoten step = ziel;
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

	private void findMinimalDistances(Knoten knoten) {
		List<Knoten> adjacentNodes = getNeighbors(knoten);
		for (Knoten target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(knoten)
					+ getDistance(knoten, target)) {
				distance.put(
						target,
						getShortestDistance(knoten)
								+ getDistance(knoten, target));
				predecessors.put(target, knoten);
				unSettledNodes.add(target);
			}
		}
	}

	private float getDistance(Knoten knoten, Knoten ziel) {
		for (Kante kante : edges) {
			if (kante.getOther(knoten) != null && kante.getOther(ziel) != null)
				return kante.getAbstand();
		}
		throw new RuntimeException("Sollte nicht passieren");
	}

	private List<Knoten> getNeighbors(Knoten knoten) {
		List<Knoten> neighbors = new ArrayList<Knoten>();

		for (Kante kante : edges) {
			if (kante.getOther(knoten) != null && !kante.getGesperrt()
					&& !isSettled(kante.getOther(knoten))) {
				neighbors.add(kante.getOther(knoten));
			}
		}
		return neighbors;
	}

	private Knoten getMinimum(Set<Knoten> knoten) {
		Knoten minimum = null;
		for (Knoten k : knoten) {
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

	private boolean isSettled(Knoten knoten) {
		return settledNodes.contains(knoten);
	}

	private float getShortestDistance(Knoten destination) {
		Float d = distance.get(destination);
		if (d == null)
			return Float.MAX_VALUE;
		else
			return d;
	}
}