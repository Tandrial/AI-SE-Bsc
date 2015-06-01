package uni.dc.ubsOpti.DelayCalc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;

public class UbsV0DelayCalc extends UBSDelayCalc {
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

			if (path.size() == 1) {
				f.setTotalDelay(0.0);
				continue;
			}
			// System.out.printf("%s : %.0f Mbps, %d bit\n", f.getName(),
			// f.getRate() / 1e6, f.getMaxFrameLength());
			double delay = 0.0;
			for (int i = 1; i < path.size(); i++) {
				EgressPort lastEgress = path.get(i - 1);

				double sizeBiggerEq = 0.0;
				double maxSmaller = 0.0;
				double shaperHigher = 0.0;

				double rate = prio.getTraffic().getNetworkSpeed();
				double size = f.getMaxFrameLength();
				int prioF = prio.getPriority(lastEgress, f);

				for (Flow other : lastEgress.getFlowList()) {
					if (f == other)
						continue;
					int prioOther = prio.getPriority(lastEgress, other);
					if (prioOther > prioF) {
						sizeBiggerEq += other.getMaxFrameLength();
						shaperHigher += other.getRate();
					} else if (prioOther == prioF) {
						sizeBiggerEq += other.getMaxFrameLength();
					} else {
						maxSmaller = Math.max(maxSmaller,
								other.getMaxFrameLength());
					}
				}
				delay += (sizeBiggerEq + maxSmaller) / (rate - shaperHigher)
						+ size / rate;
			}
			f.setTotalDelay(delay);
		}
	}

	@Override
	public void calculateDelays(PriorityConfiguration prio) {
		this.prio = prio;
		calculateDelays();
	}

	@Override
	public void calculateDelays(int[] prios) {
		prio.fromIntArray(prios);
		calculateDelays();
	}

}