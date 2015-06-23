package uni.dc.ubsOpti.tracer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class EndStepTracer extends Tracer {
	private static final long serialVersionUID = 1L;
	private int roundNumber = -1;

	Map<String, Long> dataPoints = new HashMap<String, Long>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0) {
			if (roundNumber != -1)
				dataPoints.put(roundNumber + ";" + stat.getName(), stat.getStep() - 1);
			else
				dataPoints.put(stat.getName(), stat.getStep() - 1);
		}
	}

	public Map<String, Long> getStats() {
		return dataPoints;
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	public static void saveToFile(File file, EndStepTracer tracer) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
			for (String name : tracer.dataPoints.keySet()) {
				writer.write(name + ";" + tracer.dataPoints.get(name) + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
