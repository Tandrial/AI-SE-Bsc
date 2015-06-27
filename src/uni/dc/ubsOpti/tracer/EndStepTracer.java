package uni.dc.ubsOpti.tracer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uni.dc.model.Pair;

public class EndStepTracer extends Tracer {
	private static final long serialVersionUID = 1L;

	Map<String, ArrayList<Pair<Integer, Boolean>>> dataPoints = new HashMap<String, ArrayList<Pair<Integer, Boolean>>>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0) {
			if (!dataPoints.containsKey(stat.getName())) {
				dataPoints.put(stat.getName(), new ArrayList<Pair<Integer, Boolean>>());
			}
			dataPoints.get(stat.getName()).add(new Pair<Integer, Boolean>(stat.getStep(), stat.isDelaysOkay()));
		}
	}

	public static void saveToFile(String fileName, EndStepTracer tracer) {
		for (String algoName : tracer.dataPoints.keySet()) {
			try {
				Writer w = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName + algoName + ".csv")));
				w.write("Algo;Steps;delayOkay\n");
				for (Pair<Integer, Boolean> data : tracer.dataPoints.get(algoName)) {
					w.write(algoName + ";" + data.getLeft() + ";" + data.getRight() + "\n");
				}
				w.close();
			} catch (IOException e) {

			}
		}
	}

	@Override
	public void clearData() {
		dataPoints.clear();
	}
}
