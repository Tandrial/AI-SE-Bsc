package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.goataa.impl.utils.Individual;

public class TracerStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private long step;
	private boolean delaysOkay;
	private double delay;
	private Individual<int[], int[]> data;
	private List<Individual<int[], int[]>> parents = new ArrayList<Individual<int[], int[]>>();

	@SuppressWarnings("unchecked")
	public TracerStat(String name, long step, boolean delaysOkay, Individual<?, ?> data, Individual<?, ?>... parents) {
		this.name = name;
		this.step = step;
		this.data = (Individual<int[], int[]>) data;

		this.delaysOkay = delaysOkay;
		this.delay = data.v;
		for (Individual<?, ?> parent : parents)
			this.parents.add((Individual<int[], int[]>) parent);
	}

	public String getName() {
		return name;
	}

	public long getStep() {
		return step;
	}

	public boolean isDelaysOkay() {
		return delaysOkay;
	}

	public Individual<int[], int[]> getData() {
		return data;
	}

	public double getDelay() {
		return delay;
	}

	public int[] getPrio() {
		return data.x;
	}

	public List<Individual<int[], int[]>> getParents() {
		return parents;
	}

	@Override
	public String toString() {
		if (parents.size() == 0)
			return String.format("%d;%.4e;this:%s;parent:[]\n", step, data.v, Arrays.toString(data.x));
		else
			return String.format("%d;%.4e;this:%s;parent:%s\n", step, data.v, Arrays.toString(data.x),
					Arrays.toString(parents.get(0).x));
	}
}
