package uni.dc.ubsOpti.DelayCalc;

import java.util.Map;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;

public class UbsV3DelayCalc extends UbsDelayCalc {
	private static final long serialVersionUID = 1L;

	public UbsV3DelayCalc(Map<EgressPort, Set<Flow>> traffic) {
		super(traffic);
	}

	public UbsV3DelayCalc(Set<Flow> traffic) {
		super(traffic);
	}

	@Override
	public void calculateDelays() {
		//TODO
	}
}