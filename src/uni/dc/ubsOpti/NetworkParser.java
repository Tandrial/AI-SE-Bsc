package uni.dc.ubsOpti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;
import uni.dc.model.Flow;
import uni.dc.model.Node;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.tracer.TraceCollection;

public class NetworkParser {

	private static NetworkParser parser = new NetworkParser();

	public static NetworkParser getParser() {
		return NetworkParser.parser;
	}

	private JSONObject jsonObj = null;
	private EgressTopology topology = null;
	private Traffic traffic = null;
	private PriorityConfiguration prio = null;
	private File fileName = null;

	private NetworkParser() {
	}

	public void setFileName(File fileName) {
		this.fileName = fileName;
		jsonObj = null;
		topology = null;
		traffic = null;
		prio = null;
		String extension = fileName.getName().substring(
				fileName.getName().lastIndexOf("."));
		if (extension.equals(".ser")) {
			UbsOptiConfig config = NetworkParser.loadFromFile(fileName);
			topology = config.getTopology();
			traffic = config.getTraffic();
			prio = config.getPriorityConfig();
		} else {
			this.jsonObj = getJSONFromFile(fileName);
		}
	}

	public File getFile() {
		return fileName;
	}

	public EgressTopology getTopology() {
		if (topology != null)
			return topology;
		topology = new EgressTopology();
		JSONArray connections = jsonObj.getJSONArray("connections");
		for (int i = 0; i < connections.length(); i++) {
			JSONObject curr = connections.getJSONObject(i);
			String n1 = JSONObject.getNames(curr)[0];
			curr = curr.getJSONObject(n1);
			String n2 = curr.getString("dest");
			long linkSpeed = convertSpeed(curr.getString("linkSpeed"));

			topology.add(new Node(n1), new Node(n2), linkSpeed);
			topology.add(new Node(n2), new Node(n1), linkSpeed);

		}
		return topology;
	}

	public PriorityConfiguration getPriorityConfig() {
		if (prio != null)
			return prio;
		prio = new PriorityConfiguration(getTraffic());
		return prio;
	}

	public PriorityConfiguration resetPriorityConfig() {
		prio = new PriorityConfiguration(getTraffic());
		return prio;
	}

	public Traffic getTraffic() {
		if (traffic != null)
			return traffic;
		traffic = new Traffic(getTopology());

		Map<EgressPort, Set<Flow>> portFlowMap = new HashMap<EgressPort, Set<Flow>>();

		JSONArray connections = jsonObj.getJSONArray("streams");
		for (int i = 0; i < connections.length(); i++) {
			JSONObject curr = connections.getJSONObject(i);
			int flowID = curr.getInt("streamID");
			JSONObject route = curr.getJSONObject("route").getJSONObject(
					"dijkstra");

			String src = JSONObject.getNames(route)[0];
			String dest = route.getJSONArray(src).getString(0);

			JSONObject tspec = curr.getJSONObject("tspec");
			long rate = convertSpeed(tspec.getString("leakRate"));
			int maxPacketLength = convertLength(tspec
					.getString("maxPacketLength"));
			double maxLatency;
			if (tspec.has("maxLatency"))
				maxLatency = convertTime(tspec.getString("maxLatency"));
			else
				maxLatency = -1;

			Flow flow = new Flow();
			flow.setName(String.format("F%d", flowID));
			flow.setTopology(topology);
			flow.setRate(rate);
			flow.setMaxFrameLength(maxPacketLength);
			traffic.add(flow);

			List<EgressPort> path = topology.getPath(src, dest);
			flow.setSrcPort(path.get(0));
			EgressPort lastEgress = path.get(path.size() - 1);

			for (EgressPort p : topology.getLinkMap().get(lastEgress)) {
				flow.setDestPort(p);
				flow.setMaxLatency(maxLatency);
			}

			for (EgressPort p : path) {
				if (!portFlowMap.containsKey(p))
					portFlowMap.put(p, new LinkedHashSet<Flow>());
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

	public static void saveToFile(File file, UbsOptiConfig config) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(config.getTopology());
			out.writeObject(config.getTraffic());
			out.writeObject(config.getPriorityConfig());
			out.writeObject(config.getTraces());
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static UbsOptiConfig loadFromFile(File file) {
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			EgressTopology topo = (EgressTopology) in.readObject();
			Traffic traffic = (Traffic) in.readObject();
			PriorityConfiguration prio = (PriorityConfiguration) in
					.readObject();
			TraceCollection traces = (TraceCollection) in.readObject();
			UbsDelayCalc delayCalc = new UbsV0DelayCalc(traffic);
			UbsOptiConfig config = new UbsOptiConfig(topo, traffic, prio,
					delayCalc, traces);
			in.close();
			fileIn.close();
			return config;
		} catch (Exception e) {
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

	private static long convertSpeed(String str) {
		long result = Long.parseLong(str.replaceAll("[^0-9]", ""));
		String SI = str.replaceAll("[0-9]", "");
		if (SI.equalsIgnoreCase("gbps"))
			return result * 1000 * 1000 * 1000;
		else if (SI.equalsIgnoreCase("mbps"))
			return result * 1000 * 1000;
		else if (SI.equalsIgnoreCase("kbps"))
			return result * 1000;
		else
			return result;
	}

	private static double convertTime(String str) {
		long result = Long.parseLong(str.replaceAll("[^0-9]", ""));
		String SI = str.replaceAll("[0-9]", "");
		if (SI.equalsIgnoreCase("ns"))
			return result / 10e9;
		else if (SI.equalsIgnoreCase("µs"))
			return result / 10e6;
		else if (SI.equalsIgnoreCase("ms"))
			return result / 10e3;
		else
			return result;
	}
}
