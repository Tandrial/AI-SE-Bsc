package uni.dc.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.networkGenerator.swingUI.graphviz.GraphViz;
import uni.dc.util.DeterministicHashSet;
import uni.dc.util.Dijkstra;

public class EgressTopology {

	private Set<EgressPort> portSet;
	private Map<EgressPort, Set<EgressPort>> linkMap;
	private Dijkstra dijkstra;

	public EgressTopology() {
		super();
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

		// Ports
		for (EgressPort p : portSet) {
			r.append(String.format("\t%s[label=\"%s\"];\n", GraphViz.dotUid(p),
					p.getName()));
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
	 * Returns all ports reachable from srcPort. infinite recursion.
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
			rv.addAll(getReachablePorts(dest, visited));
		}
		return rv;
	}

	private Set<EgressPort> getReachablePorts(EgressPort srcPort,
			Set<EgressPort> visited) {
		if (visited.contains(srcPort))
			return new DeterministicHashSet<EgressPort>();
		DeterministicHashSet<EgressPort> rv = new DeterministicHashSet<EgressPort>();
		visited.add(srcPort);
		for (EgressPort dest : linkMap.get(srcPort)) {
			visited.add(dest);
			rv.addAll(getReachablePorts(dest, visited));
		}

		return rv;
	}

	public List<EgressPort> getPath(String src, String dest) {
		return getPath(getPortFromName(src), getPortFromName(dest));
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
	public List<EgressPort> getPath(EgressPort src, EgressPort dest) {
		// List<EgressPort> rv = null;
		// if (dest == src) {
		// rv = new LinkedList<EgressPort>();
		// rv.add(dest);
		// } else {
		// for (EgressPort p : linkMap.get(src)) {
		// rv = getPath(p, dest);
		// if (rv != null) {
		// rv.add(0, src);
		// break;
		// }
		// }
		// }
		// return rv;

		dijkstra = new Dijkstra(this);
		dijkstra.execute(src);
		return dijkstra.getPath(dest);

	}
}
