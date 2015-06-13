package uni.dc.ubsOpti.DelayCalc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;

public class UbsV0DelayCalc extends UbsDelayCalc {
	private static final long serialVersionUID = 1L;

	public UbsV0DelayCalc(Map<EgressPort, Set<Flow>> traffic) {
		super(traffic);
	}

	public UbsV0DelayCalc(Set<Flow> traffic) {
		super(traffic);
	}

	@Override
	public void calculateDelays() {
		for (Flow f : flows) {
			List<EgressPort> path = f.getPath();
			double delay = 0.0;
			for (int i = 1; i < path.size(); i++) {
				delay += calcDelay(path.get(i - 1), f);
			}
			f.setDelay(delay);
		}
	}
}