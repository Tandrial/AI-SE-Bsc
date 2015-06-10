package uni.dc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PriorityConfiguration implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_PRIORITY = 1;
	public static final String NO_PRIORITY_STRING = "";
	private Traffic traffic;
	private Set<PortFlowPriority> tripleSet;
	private Map<Flow, Map<EgressPort, PortFlowPriority>> flowPortPriorityMap;
	private Map<EgressPort, Map<Flow, PortFlowPriority>> portFlowPriorityMap;

	private PriorityConfiguration() {
		tripleSet = new LinkedHashSet<PortFlowPriority>();
		flowPortPriorityMap = new HashMap<Flow, Map<EgressPort, PortFlowPriority>>();
		portFlowPriorityMap = new HashMap<EgressPort, Map<Flow, PortFlowPriority>>();
	}

	public PriorityConfiguration(Traffic traffic) {
		this();
		this.traffic = traffic;
		this.constructFromTraffic();
	}

	private void constructFromTraffic() {
		for (EgressPort port : traffic.getTopology().getPorts()) {
			for (Flow flow : traffic.getPortFlowMap().get(port)) {
				if (!flow.getDestPort().equals(port)) {
					PortFlowPriority pfp = new PortFlowPriority(port, flow,
							DEFAULT_PRIORITY);
					this.add(pfp);
				}
			}
		}
	}

	private void add(PortFlowPriority x) {
		if (!this.portFlowPriorityMap.containsKey(x.getPort())) {
			this.portFlowPriorityMap.put(x.getPort(),
					new HashMap<Flow, PortFlowPriority>());
		}

		if (!this.flowPortPriorityMap.containsKey(x.getFlow())) {
			this.flowPortPriorityMap.put(x.getFlow(),
					new HashMap<EgressPort, PortFlowPriority>());
		}

		tripleSet.add(x);
		portFlowPriorityMap.get(x.getPort()).put(x.getFlow(), x);
		flowPortPriorityMap.get(x.getFlow()).put(x.getPort(), x);
	}

	public void setPriority(EgressPort port, Flow flow, Integer priority) {
		this.portFlowPriorityMap.get(port).get(flow).setPriority(priority);
	}

	public int getPriority(EgressPort port, Flow flow) {
		return this.portFlowPriorityMap.get(port).get(flow).getPriority();
	}

	public boolean hasPriority(EgressPort port, Flow flow) {
		if (this.portFlowPriorityMap.get(port) != null) {
			return this.portFlowPriorityMap.get(port).get(flow) != null;
		} else {
			return false;
		}
	}

	/**
	 * Creates a deep clone of this configuration. Modification of the priority
	 * values of the clone do not affect this object.
	 */
	@Override
	public Object clone() {
		PriorityConfiguration rv = new PriorityConfiguration();
		for (PortFlowPriority e : this.tripleSet) {
			rv.add((PortFlowPriority) e.clone());
		}
		rv.traffic = traffic;
		return rv;
	}

	@Override
	public String toString() {

		String[][] table = new String[traffic.getTopology().getPorts().size() + 1][traffic
				.size() + 1];

		int c = 1;
		for (Flow flow : traffic)
			table[0][c++] = flow.getName();

		int r = 1;
		for (EgressPort port : traffic.getTopology().getPorts())
			table[r++][0] = port.getName();

		c = 1;
		for (Flow flow : traffic) {
			r = 1;
			for (EgressPort port : traffic.getTopology().getPorts()) {
				table[r][c] = this.hasPriority(port, flow) ? ""
						+ this.getPriority(port, flow) : NO_PRIORITY_STRING;
				r++;
			}
			c++;
		}

		return stringTable(table);
	}

	public int[] toIntArray() {
		List<Integer> resList = new ArrayList<Integer>();

		for (Flow flow : traffic) {
			for (EgressPort port : traffic.getTopology().getPorts()) {
				if (hasPriority(port, flow))
					resList.add(getPriority(port, flow));
			}
		}

		return resList.stream().mapToInt(i -> i).toArray();
	}

	public void fromIntArray(int[] prio) {
		int pos = 0;
		for (Flow flow : traffic) {
			for (EgressPort port : traffic.getTopology().getPorts()) {
				if (hasPriority(port, flow))
					this.setPriority(port, flow, prio[pos++]);
			}
		}
	}

	private static String stringTable(String[][] table) {
		int[] colWidth = new int[table[0].length];
		int r, c;

		for (c = 0; c < table[0].length; c++) {
			colWidth[c] = 0;
			for (r = 0; r < table.length; r++) {
				if (table[r][c] != null)
					colWidth[c] = Math.max(colWidth[c], table[r][c].length());
			}
		}

		String rv = "";
		for (r = 0; r < table.length; r++) {
			for (c = 0; c < table[0].length; c++) {
				rv += String.format("%" + colWidth[c] + "s ",
						(table[r][c] != null) ? table[r][c] : "");
			}
			rv += "\n";
		}

		return rv;
	}
}
