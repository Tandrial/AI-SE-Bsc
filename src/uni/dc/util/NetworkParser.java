package uni.dc.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;
import uni.dc.model.Flow;
import uni.dc.model.Traffic;

public class NetworkParser {
	private String fileName;
	private JSONObject jsonObj = null;
	private EgressTopology topology = null;
	private Traffic traffic = null;

	public NetworkParser(String fileName) {
		this.fileName = fileName;
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
		}
		return topology;
	}

	// TODO: network speed in bps
	private double networdSpeed = 1e9;

	// TODO: maxFrameLength in bit
	private int maxFrameLength = 12350;

	private Random rng = new Random();

	public Traffic getTraffic() {
		if (traffic != null)
			return traffic;
		traffic = new Traffic();

		traffic.setTopology(topology);
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
			String rate = tspec.getString("leakRate");
			String maxPacketLength = tspec.getString("maxPacketLength");
			System.out.println(flowID);
			System.out.println(src);
			System.out.println(dest);
			System.out.println(rate);
			System.out.println(maxPacketLength);
			System.out.println();

			Flow flow = new Flow();
			flow.setName(String.format("F%d", flowID));
			flow.setTopology(topology);
			flow.setSrcPort(topology.getPortFromName(src));
			DeterministicHashSet<EgressPort> dests = new DeterministicHashSet<EgressPort>();
			dests.add(topology.getPortFromName(dest));
			flow.setDestPortSet(dests);
			// max rate = 10% of full networkspeed
			flow.setRate(networdSpeed * ((rng.nextInt(10) + 1)) / 100);
			flow.setMaxFrameLength(rng.nextInt(maxFrameLength) + 1);
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

	private static JSONObject getJSONFromFile(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new JSONObject(new String(encoded, Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
