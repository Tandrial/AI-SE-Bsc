package uni.dc.ubsOpti.DelayCalc;

import java.util.List;
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
		for (Flow f : flows) {
			List<EgressPort> path = f.getPath();

			if (path.size() == 1) {
				f.setTotalDelay(0.0);
				continue;
			}
			double delay = 0.0;
			for (int i = 1; i < path.size(); i++) {
				EgressPort lastEgress = path.get(i - 1);

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
					if (prioOther > prioF) {
						sizeBiggerEq += other.getMaxFrameLength();
						rateHigher += other.getRate();
					} else if (prioOther == prioF) {
						sizeBiggerEq += other.getMaxFrameLength();
					} else {
						maxSmaller = Math.max(maxSmaller,
								other.getMaxFrameLength());
					}
				}
				delay += (sizeBiggerEq + maxSmaller) / (linkSpeed - rateHigher)
						+ size / linkSpeed;
			}
			f.setTotalDelay(delay);
		}
	}
}