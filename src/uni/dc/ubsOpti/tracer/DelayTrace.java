package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.UbsOptiConfig;

public class DelayTrace implements Serializable {
	private static final long serialVersionUID = 1L;
	private String algoName;
	private List<TracerStat> dataPoints = new ArrayList<TracerStat>();

	private Traffic traffic;
	private PriorityConfiguration prio;

	public DelayTrace(String algoName, UbsOptiConfig config) {
		this.algoName = algoName;
		this.traffic = config.getTraffic();
		this.prio = config.getPriorityConfig();
	}

	public void setAlgoName(String algoName) {
		this.algoName = algoName;
	}

	public String getAlgoName() {
		return algoName;
	}

	public Traffic getTraffic() {
		return traffic;
	}

	public List<TracerStat> getDataPoints() {
		return dataPoints;
	}

	public PriorityConfiguration getPrio() {
		return prio;
	}

	public void addDataPoint(long step, double delay, int[] prio) {
		dataPoints.add(new TracerStat(step, delay, prio));
	}

	public PriorityConfiguration getBestConfig() {
		double best = Double.MAX_VALUE;
		TracerStat bestStat = null;
		for (TracerStat stat : dataPoints) {
			if (stat.getDelay() < best) {
				best = stat.getDelay();
				bestStat = stat;
			}
		}
		PriorityConfiguration res = (PriorityConfiguration) prio.clone();
		res.fromIntArray(bestStat.getPrio());
		return res;
	}

	public double getBestValue() {
		double best = Double.MAX_VALUE;
		for (TracerStat stat : dataPoints) {
			if (stat.getDelay() < best) {
				best = stat.getDelay();
			}
		}
		return best;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Delaytrace for algo :" + algoName + "\n");
		sb.append("Delay development\n");
		for (TracerStat stat : dataPoints) {
			sb.append(stat);

		}
		sb.append("Best config\n");
		sb.append(dataPoints.get(dataPoints.size() - 1));
		return sb.toString();
	}
}
