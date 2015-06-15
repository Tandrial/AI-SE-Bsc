package uni.dc.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.util.GraphViz;
import uni.dc.util.Pair;

public class EgressTopology implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<Node, Set<EgressPort>> nodeMap;
	private Map<EgressPort, Node> portNodeMap;
	private Set<EgressPort> portSet;
	private Map<EgressPort, Set<EgressPort>> linkMap;
	private Map<Node, Set<Node>> nodeLinkMap;
	private Map<Pair<Node, Node>, EgressPort> nodeLinkEgressPortMap;

	public EgressTopology() {
		super();
		nodeMap = new HashMap<Node, Set<EgressPort>>();
		portNodeMap = new HashMap<EgressPort, Node>();
		portSet = new LinkedHashSet<EgressPort>();
		linkMap = new HashMap<EgressPort, Set<EgressPort>>();
		nodeLinkMap = new HashMap<Node, Set<Node>>();
		nodeLinkEgressPortMap = new HashMap<Pair<Node, Node>, EgressPort>();
	}

	public Set<EgressPort> getPorts() {
		return portSet;
	}

	public Map<Node, Set<EgressPort>> getNodeMap() {
		return nodeMap;
	}

	public Map<EgressPort, Set<EgressPort>> getLinkMap() {
		return linkMap;
	}


	public void addNode(Node n) {
		nodeMap.put(n, new LinkedHashSet<EgressPort>());
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

	public void add(Node n1, Node n2, double linkSpeed) {
		if (!nodeMap.containsKey(n1)) {
			addNode(n1);
			nodeLinkMap.put(n1, new LinkedHashSet<Node>());
		}
		if (!nodeMap.containsKey(n2)) {
			addNode(n2);
			nodeLinkMap.put(n2, new LinkedHashSet<Node>());
		}

		n1 = getNodeFromName(n1.getName());
		n2 = getNodeFromName(n2.getName());

		EgressPort src = new EgressPort();
		src.setNode(n1);
		src.setLinkSpeed(linkSpeed);
		n1.addPort(src);
		add(src);
		nodeMap.get(n1).add(src);
		portNodeMap.put(src, n1);

		EgressPort dest = new EgressPort();
		dest.setNode(n2);
		dest.setLinkSpeed(linkSpeed);
		n2.addPort(dest);
		add(dest);
		nodeMap.get(n2).add(dest);
		portNodeMap.put(dest, n2);

		addLink(src, dest);

		nodeLinkMap.get(n1).add(n2);
		nodeLinkMap.get(n2).add(n1);
		nodeLinkEgressPortMap.put(new Pair<Node, Node>(n1, n2), src);

	}

	public void addLink(String src, String dest) {
		addLink(getPortFromName(src), getPortFromName(dest));
	}

	public void addLink(EgressPort srcPort, EgressPort destPort) {
		linkMap.get(srcPort).add(destPort);
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

	public StringBuilder toDot() {
		StringBuilder r = new StringBuilder();

		r.append(String.format("digraph G {\n"));
		r.append(String.format("\tgraph[center=1 rankdir=LR]\n"));
		r.append(String.format("\tranksep=1.0;\n"));

		if (nodeMap.size() > 0) {
			for (Node n : nodeMap.keySet()) {
				r.append(String.format("\tsubgraph cluster_%s {\n",GraphViz.dotUid(n)));
				r.append(String.format("\t\tlabel=\"%s\";\n", n.getName()));
				String ranking = "\t\t{ rank=same ";
				// Ports
				for (EgressPort p : nodeMap.get(n)) {
					String portUid = GraphViz.dotUid(p);
					r.append(String.format("\t\t%s[label=\"%s\"];\n", portUid,
							p.getName()));
					ranking += portUid + " ";
				}
				ranking += "};\n\t}\n";
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

	public Set<EgressPort> getReachablePorts(EgressPort srcPort) {
		LinkedHashSet<EgressPort> rv = new LinkedHashSet<EgressPort>();
		rv.addAll(linkMap.get(srcPort));
		for (EgressPort dest : linkMap.get(srcPort)) {
			rv.addAll(getReachablePorts(dest));
		}
		return rv;
	}

	public List<EgressPort> getPath(String src, String dest) {
		return getPath(getNodeFromName(src), getNodeFromName(dest),
				new LinkedHashSet<Node>());
	}

	public List<EgressPort> getPath(EgressPort src, EgressPort dest) {
		List<EgressPort> rv = null;
		if (dest == src) {
			rv = new LinkedList<EgressPort>();
			rv.add(dest);
		} else {
			for (EgressPort p : linkMap.get(src)) {
				rv = getPath(p, dest);
				if (rv != null) {
					rv.add(0, src);
					break;
				}
			}
		}
		return rv;
	}

	public List<EgressPort> getPath(Node src, Node dest,
			LinkedHashSet<Node> visited) {
		List<EgressPort> rv = null;
		if (dest == src) {
			rv = new LinkedList<EgressPort>();
		} else {
			for (Node n : nodeLinkMap.get(src)) {
				if (!visited.contains(n)) {
					visited.add(n);
					rv = getPath(n, dest, visited);
					if (rv != null) {
						rv.add(0, nodeLinkEgressPortMap
								.get(new Pair<Node, Node>(src, n)));
						break;
					}
				}
			}
		}
		return rv;
	}	
}
