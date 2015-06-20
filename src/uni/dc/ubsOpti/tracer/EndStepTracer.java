package uni.dc.ubsOpti.tracer;

import java.util.HashMap;
import java.util.Map;

public class EndStepTracer extends Tracer {

	private static final long serialVersionUID = 1L;

	Map<String, Long> dataPoints = new HashMap<String, Long>();

	@Override
	public void update(final TracerStat stat) {
		if (stat.getPrio().length == 0) {
			dataPoints.put(stat.getName(), stat.getStep() - 1);
		}
	}

	public Map<String, Long> getStats() {
		return dataPoints;
	}
}
