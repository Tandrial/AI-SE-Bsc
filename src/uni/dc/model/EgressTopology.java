package uni.dc.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.util.DeterministicHashSet;
import uni.dc.util.Dijkstra;
import uni.dc.util.GraphViz;

public class EgressTopology {

	private Map<Node, Set<EgressPort>> nodeMap;
	private Map<EgressPort, Node> portNodeMap;
	private Set<EgressPort> portSet;
	private Map<EgressPort, Set<EgressPort>> linkMap;
	private Dijkstra dijkstra;

	public EgressTopology() {
		super();
		nodeMap = new LinkedHashMap<Node, Set<EgressPort>>();
		portNodeMap = new LinkedHashMap<EgressPort, Node>();
		portSet = new LinkedHashSet<EgressPort>();
		linkMap = new LinkedHashMap<EgressPort, Set<EgressPort>>();
	}

	public Set<EgressPort> getPorts() {
		DeterministicHashSet<EgressPort> rv = new DeterministicHashSet<EgressPort>();
		rv.addAll(this.portSet);
		return rv;
	}

	public void add(EgressPort p) {
		portSet.add(p);
		if (!linkMap.containsKey(p))
			linkMap.put(p, new HashSet<EgressPort>());
	}

	public void addNode(Node n) {
		nodeMap.put(n, new DeterministicHashSet<EgressPort>());
	}

	public void add(Node n1, Node n2) {
		if (!nodeMap.containsKey(n1))
			addNode(n1);
		if (!nodeMap.containsKey(n2))
			addNode(n2);

		// 1. Check of link existiert : Ja ==> done

		// Nein => 2. 2 neue Ports in n1 und n2 erzeugen und verlinken
		n1 = getNodeFromName(n1.getName());
		n2 = getNodeFromName(n2.getName());

		EgressPort src = new EgressPort();
		n1.addPort(src);
		add(src);
		nodeMap.get(n1).add(src);
		portNodeMap.put(src, n1);

		EgressPort dest = new EgressPort();
		n2.addPort(dest);
		add(dest);
		nodeMap.get(n2).add(dest);
		portNodeMap.put(dest, n2);

		addLink(src, dest);

	}

	public void addAll(Collection<? extends EgressPort> ports) {
		portSet.addAll(ports);
		for (EgressPort p : ports)
			linkMap.put(p, new LinkedHashSet<EgressPort>());
	}

	public void addLink(EgressPort srcPort, EgressPort destPort) {
		linkMap.get(srcPort).add(destPort);
	}

	public void addLinks(EgressPort srcPort,
			Collection<? extends EgressPort> destPorts) {
		linkMap.get(srcPort).addAll(destPorts);
	}

	public void addLink(String src, String dest) {
		addLink(getPortFromName(src), getPortFromName(dest));
	}

	public void addLinks(Collection<? extends EgressPort> srcPorts,
			EgressPort destPort) {
		for (EgressPort pSrc : srcPorts)
			linkMap.get(pSrc).add(destPort);
	}

	public EgressPort getPortFromName(String name) {
		for (EgressPort port : portSet) {
			if (port.getName().equals(name))
				return port;
		}
		return null;
	}

	public Node getNodeFromName(String name) {
		for (Node n : nodeMap.keySet()) {
			if (n.getName().equals(name))
				return n;
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public Map<EgressPort, Set<EgressPort>> getLinkMap() {
		return linkMap;
	}

	public StringBuilder toDot() {
		StringBuilder r = new StringBuilder();

		r.append(String.format("digraph G {\n"));
		r.append(String.format("\tgraph[center=1 rankdir=LR]\n"));
		r.append(String.format("\tranksep=1.0;\n"));

		if (nodeMap.size() > 0) {
			for (Node n : nodeMap.keySet()) {
				r.append(String.format("subgraph cluster_%s {\n",
						GraphViz.dotUid(n)));
				r.append(String.format("label=\"%s\"", n.getName()));
				String ranking = "{rank=same ";
				// Ports
				for (EgressPort p : nodeMap.get(n)) {
					String portUid = GraphViz.dotUid(p);
					r.append(String.format("\t%s[label=\"%s\"];\n", portUid,
							p.getName()));
					ranking += portUid + " ";
				}
				ranking += "};\n\t}";
				r.append(ranking);
			}
		} else {

			// Ports
			for (EgressPort p : portSet) {
				r.append(String.format("\t%s[label=\"%s\"];\n",
						GraphViz.dotUid(p), p.getName()));
			}
		}

		// Links
		for (EgressPort pSrc : portSet) {
			Set<EgressPort> outLinks = linkMap.get(pSrc);
			for (EgressPort pDest : outLinks) {
				r.append(String.format("\t%s->%s;\n", GraphViz.dotUid(pSrc),
						GraphViz.dotUid(pDest)));
			}
		}

		r.append(String.format("}\n"));
		return r;
	}

	/**
	 * Returns all ports reachable from srcPort.
	 * 
	 * @param srcPort
	 * @return A set of reachable ports, not including srcPort.
	 * 
	 */
	public Set<EgressPort> getReachablePorts(EgressPort srcPort) {

		DeterministicHashSet<EgressPort> rv = new DeterministicHashSet<EgressPort>();
		DeterministicHashSet<EgressPort> visited = new DeterministicHashSet<EgressPort>();
		visited.add(srcPort);
		rv.addAll(linkMap.get(srcPort));
		for (EgressPort dest : linkMap.get(srcPort)) {
			rv.addAll(getReachablePorts(dest));
		}
		return rv;

	}

	public List<EgressPort> getPath(EgressPort src, EgressPort dest) {
		dijkstra = new Dijkstra(this);
		dijkstra.execute(src);
		return dijkstra.getPath(dest);
	}

	public List<EgressPort> getPath(String src, String dest) {
		return getPath(getNodeFromName(src), getNodeFromName(dest));
	}

	/**
	 * Finds a path from src to dest, if present.
	 * 
	 * 
	 * @param src
	 *            The source port.
	 * @param dest
	 *            The destination port.
	 * @return A list with src as the first element and dest as the last
	 *         element, if there is a path from src to dest. Returns
	 *         <tt>null</tt> if there is no path.
	 * 
	 */
	public List<EgressPort> getPath(Node src, Node dest) {
		dijkstra = new Dijkstra(this);

		for (EgressPort start : src.getPorts()) {
			for (EgressPort finish : dest.getPorts()) {
				dijkstra.execute(start);
				List<EgressPort> path = dijkstra.getPath(finish);
				if (path != null)
					return path;
			}
		}
		return null;
	}
}
