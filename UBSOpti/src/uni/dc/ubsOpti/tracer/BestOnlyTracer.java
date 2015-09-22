package uni.dc.ubsOpti.tracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestOnlyTracer extends Tracer {
	private static final long serialVersionUID = 1L;

	Map<String, List<TracerStat>> dataPoints = new HashMap<String, List<TracerStat>>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0)
			return;
		if (!dataPoints.containsKey(stat.getName())) {
			dataPoints.put(stat.getName(), new ArrayList<TracerStat>());
		}

		List<TracerStat> stats = dataPoints.get(stat.getName());

		if (stats.size() > 0) {
			if (stats.get(stats.size() - 1).getDelay() > stat.getDelay()) {
				stats.add(stat);
			}
		} else {
			stats.add(stat);
		}
	}

	public Map<String, List<TracerStat>> getStats() {
		return dataPoints;
	}

	@Override
	public void clearData() {
		dataPoints.clear();
	}
}
