package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.List;

import org.goataa.impl.utils.Individual;

public class TracerStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private long step;
	private Individual<?, ?> data;
	private List<Individual<?, ?>> parents;

	public TracerStat(String name, long step, Individual<?, ?> data, Individual<?, ?>... parents) {
		this.name = name;
		this.step = step;
		this.data = data;
		for (Individual<?, ?> parent : parents)
			this.parents.add(parent);
	}

	public String getName() {
		return name;
	}

	public long getStep() {
		return step;
	}

	public double getDelay() {
		return data.v;
	}

	public int[] getPrio() {
		return (int[]) data.x;
	}
	
	public List<Individual<?, ?>> getParents() {
		return parents;
	}

	@Override
	public String toString() {
		return String.format("%d;%.8f\n", step, data.v);
	}
}
