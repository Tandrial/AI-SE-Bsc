package uni.dc.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import uni.dc.model.EgressPort;
import uni.dc.model.EgressTopology;
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

	public static void main(String[] args) {
	}

	public EgressTopology getTopology() {
		if (topology != null)
			return topology;
		topology = new EgressTopology();
		JSONArray connections = jsonObj.getJSONArray("connections");
		for (int i = 0; i < connections.length(); i++) {
			JSONObject curr = connections.getJSONObject(i);
			String src = JSONObject.getNames(curr)[0];
			String dest = curr.getString(src);

			topology.add(new EgressPort(src));
			topology.add(new EgressPort(dest));
			topology.addLink(src, dest);
		}
		return topology;
	}

	public Traffic getTraffic() {
		if (traffic != null)
			return traffic;
		traffic = new Traffic();

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
