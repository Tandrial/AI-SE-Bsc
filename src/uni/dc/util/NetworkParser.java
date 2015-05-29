package uni.dc.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;

public class NetworkParser {
	private JSONObject jsonObj = null;
	private EgressTopology topology = null;
	private Traffic traffic = null;
	private PriorityConfiguration prio = null;

	public NetworkParser(File fileName) {
		this.jsonObj = getJSONFromFile(fileName);
	}

	public EgressTopology getTopology() {
		if (topology != null)
			return topology;
		topology = new EgressTopology();
		JSONArray connections = jsonObj.getJSONArray("connections");
		for (int i = 0; i < connections.length(); i++) {
			JSONObject curr = connections.getJSONObject(i);
			String n1 = JSONObject.getNames(curr)[0];
			String n2 = curr.getString(n1);

			topology.add(new EgressPort(n1));
			topology.add(new EgressPort(n2));
			topology.addLink(n1, n2);
			topology.addLink(n2, n1);
		}
		return topology;
	}

	// TODO: network speed in bps
	private double networdSpeed = 1e9;

	public PriorityConfiguration getPriorityConfig() {
		if (prio != null)
			return prio;
		prio = new PriorityConfiguration(getTraffic());
		return prio;
	}

	public Traffic getTraffic() {
		if (traffic != null)
			return traffic;
		traffic = new Traffic();		

		traffic.setTopology(getTopology());
		traffic.setNetworkSpeed(networdSpeed);
		Map<EgressPort, Set<Flow>> portFlowMap = new LinkedHashMap<EgressPort, Set<Flow>>();

		/*
		 * Parse streams: need : streamID path tspec
		 */

		JSONArray connections = jsonObj.getJSONArray("streams");
		for (int i = 0; i < connections.length(); i++) {
			JSONObject curr = connections.getJSONObject(i);
			int flowID = curr.getInt("streamID");
			JSONObject route = curr.getJSONObject("route").getJSONObject(
					"dijkstra");

			String src = JSONObject.getNames(route)[0];
			String dest = route.getJSONArray(src).getString(0);

			JSONObject tspec = curr.getJSONObject("tspec");
			int rate = convertSpeed(tspec.getString("leakRate"));
			int maxPacketLength = convertLength(tspec
					.getString("maxPacketLength"));

			Flow flow = new Flow();
			flow.setName(String.format("F%d", flowID));
			flow.setTopology(topology);
			flow.setSrcPort(topology.getPortFromName(src));
			DeterministicHashSet<EgressPort> dests = new DeterministicHashSet<EgressPort>();
			dests.add(topology.getPortFromName(dest));
			flow.setDestPortSet(dests);
			flow.setRate(rate);
			flow.setMaxFrameLength(maxPacketLength);
			traffic.add(flow);

			List<EgressPort> path = topology.getPath(src, dest);
			for (EgressPort p : path) {
				if (!portFlowMap.containsKey(p))
					portFlowMap.put(p, new DeterministicHashSet<Flow>());
				portFlowMap.get(p).add(flow);
			}
		}

		for (EgressPort port : portFlowMap.keySet()) {
			port.setFlowList(portFlowMap.get(port));
		}

		return traffic;
	}

	private static JSONObject getJSONFromFile(File file) {
		try {
			byte[] encoded = Files.readAllBytes(file.toPath());
			return new JSONObject(new String(encoded, Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static int convertLength(String str) {
		int result = Integer.parseInt(str.replaceAll("[^0-9]", ""));
		String SI = str.replaceAll("[0-9]", "");
		if (SI.equalsIgnoreCase("mb"))
			return result * 1000 * 1000;
		if (SI.equalsIgnoreCase("kb"))
			return result * 1000;
		else
			return result;
	}

	private static int convertSpeed(String str) {
		int result = Integer.parseInt(str.replaceAll("[^0-9]", ""));
		String SI = str.replaceAll("[0-9]", "");
		if (SI.equalsIgnoreCase("mbps"))
			return result * 1000 * 1000;
		if (SI.equalsIgnoreCase("kbps"))
			return result * 1000;
		else
			return result;
	}
}
