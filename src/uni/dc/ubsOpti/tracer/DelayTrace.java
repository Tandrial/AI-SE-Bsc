package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.UbsOptiConfig;

public class DelayTrace implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private List<TracerStat> stats = new ArrayList<TracerStat>();

	private EgressTopology topology;
	private Traffic traffic;
	private PriorityConfiguration prio;

	public DelayTrace(String name, UbsOptiConfig config) {
		this.name = name;
		this.topology = config.getTopology();
		this.traffic = config.getTraffic();
		this.prio = config.getPriorityConfig();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public Traffic getTraffic() {
		return traffic;
	}

	public List<TracerStat> getStats() {
		return stats;
	}

	public PriorityConfiguration getPrio() {
		return prio;
	}

	public void addDataPoint(long step, double delay, int[] prio) {
		stats.add(new TracerStat(step, delay, prio));
	}

	public PriorityConfiguration getBestConfig() {
		double best = Double.MAX_VALUE;
		TracerStat bestStat = null;
		for (TracerStat stat : stats) {
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
		for (TracerStat stat : stats) {
			if (stat.getDelay() < best) {
				best = stat.getDelay();
			}
		}
		return best;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Delaytrace for algo :" + name + "\n");
		sb.append("Delay development\n");
		for (TracerStat stat : stats) {
			sb.append(stat);

		}
		sb.append("Best config\n");
		sb.append(stats.get(stats.size() - 1));
		return sb.toString();
	}
}
