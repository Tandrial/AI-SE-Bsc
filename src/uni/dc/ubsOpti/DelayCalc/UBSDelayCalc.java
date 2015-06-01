package uni.dc.ubsOpti.DelayCalc;

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

public abstract class UbsDelayCalc extends OptimizationModule implements
		IObjectiveFunction<int[]> {
	private static final long serialVersionUID = 1L;
	protected Set<Flow> flows = null;
	protected PriorityConfiguration prio = null;

	public PriorityConfiguration getPrio() {
		return prio;
	}

	public void setPrio(PriorityConfiguration prio) {
		this.prio = prio;
	}

	public UbsDelayCalc(Map<EgressPort, Set<Flow>> traffic) {
		flows = new DeterministicHashSet<Flow>();
		for (Entry<EgressPort, Set<Flow>> x : traffic.entrySet()) {
			for (Flow bla : x.getValue()) {
				flows.add(bla);
			}
		}
	}

	public UbsDelayCalc(Set<Flow> flows) {
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

	public abstract void calculateDelays();

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
										.getMaxLatencyRequirement() * 1000, j
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
					// TODO: Strafe f�r Delay > maxLatencyReq
					delay += 1;
			}
		}
		return delay;
	}
}
