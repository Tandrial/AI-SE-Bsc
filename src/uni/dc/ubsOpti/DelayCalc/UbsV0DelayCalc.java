package uni.dc.ubsOpti.DelayCalc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IObjectiveFunction;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.UbsDestParameters;
import uni.dc.util.DeterministicHashSet;

public class UbsV0DelayCalc extends OptimizationModule implements
		IObjectiveFunction<int[]> {
	private static final long serialVersionUID = 1L;
	private Set<Flow> flows = null;
	private PriorityConfiguration prio = null;

	public PriorityConfiguration getPrio() {
		return prio;
	}

	public void setPrio(PriorityConfiguration prio) {
		this.prio = prio;
	}

	public UbsV0DelayCalc(Map<EgressPort, Set<Flow>> traffic) {
		flows = new DeterministicHashSet<Flow>();
		for (Entry<EgressPort, Set<Flow>> x : traffic.entrySet()) {
			for (Flow bla : x.getValue()) {
				flows.add(bla);
			}
		}
	}

	public UbsV0DelayCalc(Set<Flow> flows) {
		this.flows = flows;
	}

	public void calculateDelays(PriorityConfiguration prio) {
		this.prio = prio;
		calculateDelays();
	}

	public void calculateDelays(int[] prios) {
		prio.fromIntArray(prios);
		calculateDelays();
	}

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

	public void printDelays() {
		for (Flow x : flows) {
			System.out.printf("%s : %.0f Mbps, %d bit\n", x.getName(),
					x.getRate() / 1e6, x.getMaxFrameLength());
			System.out.printf("Path : %s\n", x);

			for (Entry<EgressPort, UbsDestParameters> j : x
					.getDestPortParameterMap().entrySet()) {
				System.out
						.printf("Destination %s has maxLat of %.3e ms, actual delay is %.3e ms\n",
								j.getKey(), j.getValue()
										.getMaxLatencyRequirement()*1000, j
										.getValue().getActualDelay() * 1000);
			}
			System.out.println();
		}
	}

	public boolean checkDelays() {
		for (Flow x : flows) {
			for (Entry<EgressPort, UbsDestParameters> j : x
					.getDestPortParameterMap().entrySet()) {
				if (j.getValue().getActualDelay() > j.getValue()
						.getMaxLatencyRequirement())
					return false;
			}
		}
		return true;
	}

	int cnt = 0;

	@Override
	public double compute(int[] x, Random r) {
		prio.fromIntArray(x);
		calculateDelays();
		double delay = 0.0d;
		for (Flow f : flows) {
			delay += f.getTotalDelay();
			for (Entry<EgressPort, UbsDestParameters> j : f
					.getDestPortParameterMap().entrySet()) {
				if (j.getValue().getActualDelay() > j.getValue()
						.getMaxLatencyRequirement())
					// TODO: Strafe für Delay > maxLatencyReq
					delay += 1;
			}
		}
		return delay;
	}
}