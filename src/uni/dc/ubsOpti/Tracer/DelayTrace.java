package uni.dc.ubsOpti.Tracer;

import java.util.ArrayList;
import java.util.List;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.OptimizerConfig;

public class DelayTrace {

	private String name;
	private List<TracerStat> stats = new ArrayList<TracerStat>();

	private EgressTopology topology;
	private Traffic traffic;
	private PriorityConfiguration prio;

	public DelayTrace(String name, OptimizerConfig config) {
		this.name = name;
		this.topology = config.getTopology();
		this.traffic = config.getTraffic();
		this.prio = config.getPriorityConfig();
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
