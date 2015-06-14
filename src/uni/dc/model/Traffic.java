package uni.dc.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.util.GraphViz;
import uni.dc.util.HSLColorGenerator;

public class Traffic extends LinkedHashSet<Flow> implements Serializable {
	private static final long serialVersionUID = 1L;
	private EgressTopology topology;
	
	public Traffic(EgressTopology topology) {
		super();
		this.topology = topology;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public Map<EgressPort, Set<Flow>> getPortFlowMap() {
		Map<EgressPort, Set<Flow>> rv = new HashMap<EgressPort, Set<Flow>>();

		for (EgressPort p : topology.getPorts()) {
			rv.put(p, new LinkedHashSet<Flow>());
		}

		for (Flow f : this) {
			EgressPort srcPort = f.getSrcPort();

			UbsDestParameters maxLat = new UbsDestParameters(0);
			if (f.getDestPortParameter() != null)
				maxLat = f.getDestPortParameter();
			List<EgressPort> path;

			if (srcPort.getNode() == null) {
				path = topology.getPath(srcPort, f.getDestPort());
			} else {
				path = topology.getPath(srcPort.getNode(), f.getDestPort()
						.getNode(), new LinkedHashSet<Node>());
			}
			f.setDestPortParameter(maxLat);
			f.getDestPortParameter().setPath(path);
			for (EgressPort p : path)
				rv.get(p).add(f);
		}
		return rv;
	}

	public StringBuilder toDot(PriorityConfiguration prio) {

		StringBuilder r = new StringBuilder();

		Map<EgressPort, Set<Flow>> portFlowMap = this.getPortFlowMap();
		Set<EgressPort> portSet = topology.getPorts();
		Map<EgressPort, Set<EgressPort>> linkMap = topology.getLinkMap();
		Map<Object, Color> flowColorMap = uniqueObjectColors(this);
		Map<Node, Set<EgressPort>> nodeMap = topology.getNodeMap();

		r.append(String.format("digraph G {\n"));
		r.append(String.format("\tgraph[center=1 rankdir=LR]\n"));
		r.append(String.format("\tranksep=1.0;\n"));

		// Ports
		if (nodeMap.size() > 0) {
			for (Node n : nodeMap.keySet()) {
				r.append(String.format("\tsubgraph cluster%s {\n",
						GraphViz.dotUid(n)));
				r.append(String.format("\t\tlabel=\"%s\";\n", n.getName()));

				for (EgressPort p : nodeMap.get(n)) {
					String portUid = GraphViz.dotUid(p);
					String prioString = generatePrioTable(p, prio);
					if (prioString.equals("\"\"")) {
						r.append(String.format("\t%s[label=\"%s\"];\n",
								portUid, p.getName()));
					} else {
						r.append(String.format("\t\tsubgraph cluster%s {\n",
								portUid));
						r.append(String.format("\t\t\tlabel=\"%s\";\n",
								p.getName()));
						r.append(String.format(
								"\t\t\t%s[shape=none,label=%s];\n", portUid,
								prioString));
						r.append("\t\t}\n");
					}
				}
				r.append("\t}\n");
			}
		} else {
			for (EgressPort p : portSet) {
				String portUid = GraphViz.dotUid(p);
				String prioString = generatePrioTable(p, prio);
				if (prioString.equals("\"\"")) {
					r.append(String.format("\t%s[label=\"%s\"];\n", portUid,
							p.getName()));
				} else {
					r.append(String
							.format("\tsubgraph cluster_%s {\n", portUid));
					r.append(String.format("\t\tlabel=\"%s\";\n", p.getName()));
					r.append(String.format("\t\t\t%s[shape=none, label=%s];\n",
							portUid, prioString));
					r.append("\t}\n");
				}
			}
		}

		// Links
		List<EgressPort> done = new LinkedList<EgressPort>();
		for (EgressPort pSrc : portSet) {
			for (EgressPort pDest : linkMap.get(pSrc)) {
				if (done.contains(pDest))
					continue;

				Set<Flow> srcDestFlows = new LinkedHashSet<Flow>(
						portFlowMap.get(pSrc));
				if (nodeMap.size() == 0)
					srcDestFlows.retainAll(portFlowMap.get(pDest));

				r.append(String.format("\t%s->%s[label=%s,color=%s];\n",
						GraphViz.dotUid(pSrc), GraphViz.dotUid(pDest),
						buildflowDotLabelString(srcDestFlows),
						buildDotColorString(srcDestFlows, flowColorMap)));
			}
			done.add(pSrc);
		}
		r.append(String.format("}\n"));
		return r;
	}

	private String generatePrioTable(EgressPort p, PriorityConfiguration prio) {
		Set<Flow> flows = p.getFlowList();
		boolean hasFlow = false;

		if (flows.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\t\t<<FONT POINT-SIZE=\"8\"><TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">\n");
			for (Flow f : flows) {
				if (!f.getDestPort().equals(p)) {
					sb.append(String.format(
							"\t\t\t<TR><TD>%s</TD><TD>%d</TD></TR>\n",
							f.getName(), prio.getPriority(p, f)));
					hasFlow = true;
				}
			}
			sb.append("\t\t</TABLE></FONT>>");

			return hasFlow ? sb.toString() : "\"\"";
		} else {
			return "\"\"";
		}

	}

	private String buildflowDotLabelString(Set<Flow> flows) {
		String rv = "";

		if (flows.size() > 0) {
			Iterator<Flow> it = flows.iterator();
			rv += it.next().getName();
			while (it.hasNext())
				rv += "," + it.next().getName();
		}
		rv = String.format("\"{%s}\"", rv);

		return rv;
	}

	private static Map<Object, Color> uniqueObjectColors(Collection<?> objects) {
		Map<Object, Color> rv = new HashMap<Object, Color>();

		HSLColorGenerator colorGenerator = new HSLColorGenerator();

		int colorIndex = 0;
		for (Object o : objects) {
			rv.put(o, colorGenerator.getColor(colorIndex++));
		}
		return rv;
	}

	private static String buildDotColorString(Collection<?> objects,
			Map<Object, Color> colorMap) {
		String rv = "";

		if (objects.size() > 0) {
			Iterator<?> it = objects.iterator();
			rv += getDotColorString(colorMap.get(it.next()));
			while (it.hasNext())
				rv += ":" + getDotColorString(colorMap.get(it.next()));
		}
		rv = String.format("\"%s\"", rv);

		return rv;
	}

	private static String getDotColorString(Color c) {
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(),
				c.getBlue());
	}
}
