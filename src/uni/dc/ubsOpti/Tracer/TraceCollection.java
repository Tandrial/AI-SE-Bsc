package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.ArrayList;

import uni.dc.model.PriorityConfiguration;

public class TraceCollection extends ArrayList<DelayTrace> implements Serializable{

	private static final long serialVersionUID = 1L;

	public PriorityConfiguration getBestConfig() {
		PriorityConfiguration prio = null;
		double best = Double.MAX_VALUE;
		for (DelayTrace trace : this) {
			if (trace.getBestValue() < best) {
				prio = trace.getBestConfig();
				best = trace.getBestValue();
			}
		}
		return prio;
	}

}
