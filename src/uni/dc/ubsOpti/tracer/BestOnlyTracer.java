package uni.dc.ubsOpti.tracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestOnlyTracer extends Tracer {

	private static final long serialVersionUID = 1L;

	Map<String, List<TracerStat>> dataPoints = new HashMap<String, List<TracerStat>>();

	@Override
	public void update(final TracerStat stat) {
		if (stat.getPrio().length == 0)
			return;
		if (!dataPoints.containsKey(stat.getName())) {
			dataPoints.put(stat.getName(), new ArrayList<TracerStat>());
			dataPoints.get(stat.getName()).add(stat);
		} else {
			TracerStat last = dataPoints.get(stat.getName()).get(dataPoints.get(stat.getName()).size() - 1);
			if (last.getDelay() > stat.getDelay())
				dataPoints.get(stat.getName()).add(stat);
		}
	}

	public Map<String, List<TracerStat>> getStats() {
		return dataPoints;
	}
}
