package uni.dc.model;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import uni.dc.networkGenerator.swingUI.HSLColorGenerator;
import uni.dc.networkGenerator.swingUI.graphviz.GraphViz;
import uni.dc.util.DeterministicHashSet;

public class Traffic extends DeterministicHashSet<Flow> {

	private static final long serialVersionUID = 1L;
	// TODO: maxDelay in s
	private static final double maxDelay = 0.0001;
	private Random rnd = new Random();

	private double networkSpeed = 0;

	private EgressTopology topology;

	public Traffic() {
		super();
	}

	public Traffic(Collection<? extends Flow> c) {
		super(c);
	}

	public Traffic(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public Traffic(int initialCapacity) {
		super(initialCapacity);
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public void setNetworkSpeed(double networdSpeed) {
		this.networkSpeed = networdSpeed;
	}

	public double getNetworkSpeed() {
		return networkSpeed;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public Map<EgressPort, Set<Flow>> getPortFlowMap() {
		Map<EgressPort, Set<Flow>> rv = new LinkedHashMap<EgressPort, Set<Flow>>();

		for (EgressPort p : topology.getPorts()) {
			rv.put(p, new DeterministicHashSet<Flow>());
		}

		for (Flow f : this) {
			EgressPort srcPort = f.getSrcPort();

			Map<EgressPort, UbsDestParameters> maxLat = new HashMap<EgressPort, UbsDestParameters>();
			for (EgressPort destPort : f.getDestPortSet()) {
				// max rate = 10% of full networkspeed
				maxLat.put(destPort,
						new UbsDestParameters(maxDelay
								* ((rnd.nextInt(70) + 1)) / 100));
				List<EgressPort> path = topology.getPath(srcPort, destPort);
				maxLat.get(destPort).setPath(path);
				for (EgressPort p : path)
					rv.get(p).add(f);
			}
			f.setDestPortParameterMap(maxLat);
		}
		return rv;
	}

	public StringBuilder toDot() {

		StringBuilder r = new StringBuilder();

		Map<EgressPort, Set<Flow>> portFlowMap = this.getPortFlowMap();
		Set<EgressPort> portSet = topology.getPorts();
		Map<EgressPort, Set<EgressPort>> linkMap = topology.getLinkMap();
		Map<Object, Color> flowColorMap = uniqueObjectColors(this);

		r.append(String.format("digraph G {\n"));
		r.append(String.format("\tgraph[center=1 rankdir=LR]\n"));
		r.append(String.format("\tranksep=1.0;\n"));

		// Ports

		for (EgressPort p : portSet) {
			String portUid = GraphViz.dotUid(p);
			r.append(String.format("\tsubgraph cluster_%s {\n", portUid));
			r.append(String.format("\t\tlabel=\"%s\"", p.getName()));
			r.append(String.format("\t\t%s_src[label=\"%s\"];\n", portUid, "src"));
			r.append(String.format("\t\t%s[label=\"%s\"];\n",portUid,p.getName()));
			r.append(String.format("\t\t%s_src->%s[label=%s];\n", portUid, portUid, "Fx"));
			r.append(String.format("\t\t%s_sink[label=\"%s\"];\n", portUid,	"sink"));
			r.append(String.format("\t\t%s->%s_sink[label=%s];\n", portUid,	portUid, "Fx"));
			String prioTableString = "<<FONT POINT-SIZE=\"8\"><TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\"><TR><TD>F1</TD><TD>F2</TD></TR><TR><TD>X</TD><TD></TD></TR></TABLE></FONT>>";
			r.append(String.format("\t\t %s[shape=none, label=%s];\n", portUid,	prioTableString));
			r.append(String.format("\t\t{rank=same %s_sink %s %s_src};\n", portUid, portUid, portUid, portUid));
			r.append("\t}\n");
		}

		List<EgressPort> done = new LinkedList<EgressPort>();
		
		// Links
		for (EgressPort pSrc : portSet) {
			Set<EgressPort> outLinks = linkMap.get(pSrc);

			Set<Flow> srcOnlyFlows = new DeterministicHashSet<Flow>(portFlowMap.get(pSrc));

			for (EgressPort pDest : outLinks) {
				if (done.contains(pDest))
					continue;
				srcOnlyFlows.removeAll(portFlowMap.get(pDest));

				Set<Flow> srcDestFlows = new DeterministicHashSet<Flow>(portFlowMap.get(pSrc));
				srcDestFlows.retainAll(portFlowMap.get(pDest));
				String srcDestLabel = buildflowDotLabelString(srcDestFlows);

				Set<Flow> destOnlyFlows = new DeterministicHashSet<Flow>(portFlowMap.get(pDest));
				destOnlyFlows.removeAll(portFlowMap.get(pSrc));

				r.append(String.format("\t%s->%s[label=%s,color=%s];\n",
						GraphViz.dotUid(pSrc), GraphViz.dotUid(pDest),
						srcDestLabel,
						buildDotColorString(srcDestFlows, flowColorMap)));
			}
			done.add(pSrc);
		}

		r.append(String.format("}\n"));
		System.out.println(r.toString());
		return r;
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
		Map<Object, Color> rv = new LinkedHashMap<Object, Color>();

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
