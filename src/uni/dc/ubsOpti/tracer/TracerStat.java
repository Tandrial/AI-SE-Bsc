package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.goataa.impl.utils.Individual;

public class TracerStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private long step;
	private boolean delaysOkay;
	private Individual<?, ?> data;
	private List<Individual<int[], int[]>> parents = new ArrayList<Individual<int[], int[]>>();

	@SuppressWarnings("unchecked")
	public TracerStat(String name, long step, boolean delaysOkay, Individual<?, ?> data, Individual<?, ?>... parents) {
		this.name = name;
		this.step = step;
		this.data = data;
		this.delaysOkay = delaysOkay;
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

	@SuppressWarnings("unchecked")
	public Individual<int[], int[]> getData() {
		return (Individual<int[], int[]>) data;
	}

	public double getDelay() {
		return data.v;
	}

	public int[] getPrio() {
		return (int[]) data.x;
	}

	public List<Individual<int[], int[]>> getParents() {
		return parents;
	}

	@Override
	public String toString() {
		return String.format("%d;%.8f;%s\n", step, data.v, parents);
	}
}
