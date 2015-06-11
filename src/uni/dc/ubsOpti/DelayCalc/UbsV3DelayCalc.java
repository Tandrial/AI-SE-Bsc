package uni.dc.ubsOpti.DelayCalc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;

public class UbsV3DelayCalc extends UbsDelayCalc {
	private static final long serialVersionUID = 1L;

	private Map<EgressPort, Double[]> maxDelays;

	public UbsV3DelayCalc(Map<EgressPort, Set<Flow>> traffic) {
		super(traffic);
	}

	public UbsV3DelayCalc(Set<Flow> traffic) {
		super(traffic);
	}

	@Override
	public void calculateDelays() {
		maxDelays = new HashMap<EgressPort, Double[]>();
		for (Flow f : flows) {
			List<EgressPort> path = f.getPath();

			for (int i = 1; i < path.size(); i++) {
				double delay = 0.0;
				EgressPort lastEgress = path.get(i - 1);

				delay = calcDelay(lastEgress, f);
				
				if (!maxDelays.containsKey(lastEgress)) {
					Double[] arr = new Double[2];
					for (int j = 0; j < arr.length; j++) {
						arr[j] = new Double(0.0d);
					}
					maxDelays.put(lastEgress, arr);
				}
				double currentMax = maxDelays.get(lastEgress)[prio.getPriority(
						lastEgress, f) - 1];

				maxDelays.get(lastEgress)[prio.getPriority(lastEgress, f) - 1] = Math
						.max(delay, currentMax);
			}
		}

		for (Flow f : flows) {
			double delay = 0.0d;
			for (int i = 1; i < f.getPath().size()-1; i++) {
					delay += maxDelays.get(f.getPath().get(i))[prio.getPriority(f.getPath().get(i), f) - 1];
			}
			delay += calcDelay(f.getPath().get(f.getPath().size()-1), f);
			f.setDelay(delay);
		}
	}

	private double calcDelay(EgressPort lastEgress, Flow f) {
		double delay;
		double sizeBiggerEq = 0.0;
		double maxSmaller = 0.0;
		double rateHigher = 0.0;

		double linkSpeed = lastEgress.getLinkSpeed();
		double size = f.getMaxFrameLength();
		int prioF = prio.getPriority(lastEgress, f);

		for (Flow other : lastEgress.getFlowList()) {
			if (f == other)
				continue;
			int prioOther = prio.getPriority(lastEgress, other);
			if (prioOther < prioF) {
				sizeBiggerEq += other.getMaxFrameLength();
				rateHigher += other.getRate();
			} else if (prioOther == prioF) {
				sizeBiggerEq += other.getMaxFrameLength();
			} else {
				maxSmaller = Math.max(maxSmaller,
						other.getMaxFrameLength());
			}
		}
		delay = (sizeBiggerEq + maxSmaller) / (linkSpeed - rateHigher)
				+ size / linkSpeed;
		return delay;
	}
}