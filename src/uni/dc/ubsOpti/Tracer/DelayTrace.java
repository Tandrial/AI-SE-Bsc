package uni.dc.ubsOpti.Tracer;

import java.util.ArrayList;
import java.util.List;

import uni.dc.model.EgressTopology;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.OptimizerConfig;

public class DelayTrace {

	private String name;
	private List<TracerStat> stats = new ArrayList<TracerStat>();

	private EgressTopology topology;
	private Traffic traffic;

	public DelayTrace(String algoName, OptimizerConfig config) {
		this.name = algoName;
		this.topology = config.getTopology();
		this.traffic = config.getTraffic();
	}

	public String getAlgoName() {
		return name;
	}

	public void setAlgoName(String algoName) {
		this.name = algoName;
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

	public void addDataPoint(long step, double delay) {
		stats.add(new TracerStat(step, delay));
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
