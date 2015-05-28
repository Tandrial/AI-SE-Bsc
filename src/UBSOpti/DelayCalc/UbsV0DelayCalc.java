package UBSOpti.DelayCalc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import networkGenerator.model.EgressPort;
import networkGenerator.model.Flow;
import networkGenerator.model.PriorityConfiguration;
import networkGenerator.model.UbsDestParameters;
import networkGenerator.util.DeterministicHashSet;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IObjectiveFunction;

public class UbsV0DelayCalc extends OptimizationModule implements
		IObjectiveFunction<int[]> {
	private static final long serialVersionUID = 1L;
	private Set<Flow> flows = null;
	private PriorityConfiguration prio = null;

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

	public void calcuateDelays(PriorityConfiguration prio) {
		this.prio = prio;
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

				double sizeBiggerEq = 0;
				double maxSmaller = 0;
				double shaperHigher = 0.0;

				double rate = prio.getTraffic().getNetworkSpeed();
				double size = f.getMaxFrameLength();
				int prioF = prio.getPriority(lastEgress, f);

				for (Flow other : lastEgress.getFlowList()) {
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
						.printf("Destination %s has maxLat of %f, actual delay is %fs\n",
								j.getKey(), j.getValue()
										.getMaxLatencyRequirement(), j
										.getValue().getActualDelay());
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
		System.out.println(prio);
		calculateDelays();
		double delay = 0.0d;
		for (Flow f : flows) {
			for (Entry<EgressPort, UbsDestParameters> j : f
					.getDestPortParameterMap().entrySet()) {
				if (j.getValue().getActualDelay() > j.getValue()
						.getMaxLatencyRequirement())
					// TODO: Strafe für Delay > maxLatencyReq
					delay += f.getTotalDelay();
				else
					delay -= f.getTotalDelay();
			}
		}
		return delay;
	}
}