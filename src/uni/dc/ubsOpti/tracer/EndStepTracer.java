package uni.dc.ubsOpti.tracer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import uni.dc.model.Pair;

public class EndStepTracer extends Tracer {
	private static final long serialVersionUID = 1L;
	private String id = "";

	Map<String, Pair<Long, Boolean>> dataPoints = new HashMap<String, Pair<Long, Boolean>>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0) {
			if (!id.equals(""))
				dataPoints.put(id + ";" + stat.getName(),
						new Pair<Long, Boolean>(stat.getStep() - 1, stat.isDelaysOkay()));
			else
				dataPoints.put(stat.getName(), new Pair<Long, Boolean>(stat.getStep() - 1, stat.isDelaysOkay()));
		}
	}

	public Map<String, Pair<Long, Boolean>> getStats() {
		return dataPoints;
	}

	public long getEndStep(String algoName) {
		return dataPoints.get(algoName).getLeft();
	}

	public void setID(String id) {
		this.id = id;
	}

	public static void saveToFile(File file, EndStepTracer tracer) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
			for (String name : tracer.dataPoints.keySet()) {
				Pair<Long, Boolean> data = tracer.dataPoints.get(name);
				writer.write(name + ";" + data.getLeft() + ";" + data.getRight() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
