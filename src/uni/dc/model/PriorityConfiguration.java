package uni.dc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
					PortFlowPriority pfp = new PortFlowPriority(port, flow, DEFAULT_PRIORITY);
					this.add(pfp);
				}
			}
		}
	}

	private void add(PortFlowPriority x) {
		if (!this.portFlowPriorityMap.containsKey(x.getPort())) {
			this.portFlowPriorityMap.put(x.getPort(), new HashMap<Flow, PortFlowPriority>());
		}

		if (!this.flowPortPriorityMap.containsKey(x.getFlow())) {
			this.flowPortPriorityMap.put(x.getFlow(), new HashMap<EgressPort, PortFlowPriority>());
		}

		tripleSet.add(x);
		portFlowPriorityMap.get(x.getPort()).put(x.getFlow(), x);
		flowPortPriorityMap.get(x.getFlow()).put(x.getPort(), x);
	}

	public void setPriority(EgressPort port, Flow flow, Integer priority) {
		this.portFlowPriorityMap.get(port).get(flow).setPriority(priority);
	}

	public int getPriority(EgressPort port, Flow flow) {
		Map<Flow, PortFlowPriority> map = this.portFlowPriorityMap.get(port);
		if (map == null || map.get(flow) == null)
			return -1;
		return map.get(flow).getPriority();
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
		return stringTable(toTable());
	}

	public String toHTMLString() {
		return array2HTML(toTable());
	}

	public String toDiffHTMLString(int[] parent) {
		int[] p = toIntArray();

		String[][] table = new String[traffic.getTopology().getPorts().size() + 1][traffic.size() + 1];
		table[0][0] = " ";
		int c = 1;
		for (Flow flow : traffic)
			table[0][c++] = flow.getName();

		int r = 1;
		for (EgressPort port : traffic.getTopology().getPorts())
			table[r++][0] = port.getName();

		int pos = 0;
		c = 1;
		for (Flow flow : traffic) {
			r = 1;
			for (EgressPort port : traffic.getTopology().getPorts()) {
				if (this.hasPriority(port, flow)) {
					table[r][c] = "" + this.getPriority(port, flow);
					int diff = p[pos] - parent[pos];
					pos++;
					if (diff < 0) {
						table[r][c] += "(" + diff + ")";
					} else if (diff > 0) {
						table[r][c] += "(+" + diff + ")";
					}
				} else {
					table[r][c] = NO_PRIORITY_STRING;
				}
				r++;
			}
			c++;
		}
		return array2HTML(table);
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

	public String[][] toTable() {
		String[][] table = new String[traffic.getTopology().getPorts().size() + 1][traffic.size() + 1];
		table[0][0] = "";
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
				table[r][c] = this.hasPriority(port, flow) ? "" + this.getPriority(port, flow) : NO_PRIORITY_STRING;
				r++;
			}
			c++;
		}
		return table;
	}

	public static String array2HTML(Object[][] array) {
		StringBuilder html = new StringBuilder("<html><table>");
		html.append("<th></th>");
		for (Object elem : array[0]) {
			html.append("<th>" + elem.toString() + "</th>");
		}
		for (int i = 1; i < array.length; i++) {
			Object[] row = array[i];
			html.append("<tr>");
			for (int j = 0; j < row.length; j++) {
				if (j == 0)
					html.append("<td><th>" + row[j].toString() + "</th></td>");
				else
					html.append("<td>" + row[j].toString() + "</td>");
			}
			html.append("</tr>");
		}
		html.append("</table></html>");
		return html.toString();
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
				rv += String.format("%" + colWidth[c] + "s ", (table[r][c] != null) ? table[r][c] : "");
			}
			rv += "\n";
		}

		return rv;
	}

	public int getPos(EgressPort port, Flow flow) {
		int pos = 0;
		for (Flow f : traffic) {
			for (EgressPort p : traffic.getTopology().getPorts()) {
				if (f == flow && p == port)
					return pos;
				if (hasPriority(p, f))
					pos++;
			}
		}
		return pos;
	}

	public void squash() {
		for (EgressPort port : portFlowPriorityMap.keySet()) {
			Map<Flow, PortFlowPriority> fMap = portFlowPriorityMap.get(port);
			List<Integer> priosInPort = new ArrayList<Integer>();
			for (Flow f : fMap.keySet()) {
				int prio = fMap.get(f).getPriority();
				if (!priosInPort.contains(prio))
					priosInPort.add(prio);
			}
			Collections.sort(priosInPort);

			int currPrio = 1;
			for (Integer prio : priosInPort) {
				for (Flow f : fMap.keySet()) {
					if (fMap.get(f).getPriority() == prio) {
						fMap.get(f).setPriority(currPrio);
					}
				}
				currPrio++;
			}
		}
	}

	public int calcMaxSteps(int maxPrio) {
		int count = 1;
		for (EgressPort port : portFlowPriorityMap.keySet()) {
			Map<Flow, PortFlowPriority> fMap = portFlowPriorityMap.get(port);
			if (fMap.size() > 1) {
				count *= Math.pow(fMap.size(), maxPrio);
			}
		}
		// TODO: 50% better than BruteForce
		return count / 2;
	}
}
