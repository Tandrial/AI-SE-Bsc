package uni.dc.ubsOpti.DelayCalc;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IObjectiveFunction;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.util.DeterministicHashSet;

public abstract class UbsDelayCalc extends OptimizationModule implements
		IObjectiveFunction<int[]>, Serializable {
	private static final long serialVersionUID = 1L;
	protected Set<Flow> flows = null;
	protected PriorityConfiguration prio = null;
	private double fitness = 0.0d;

	public PriorityConfiguration getPrio() {
		return prio;
	}

	public double getFitness() {
		return fitness;
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

	public void setInitialDelays(PriorityConfiguration prio) {
		this.prio = prio;
		calculateDelays();
		for (Flow f : flows) {
			f.setInitialMaxLatencyRequirement();
		}
	}

	public void printDelays() {
		for (Flow flow : flows) {
			System.out.printf("%s : %.0f Mbps, %d bit\n", flow.getName(),
					flow.getRate() / 1e6, flow.getMaxFrameLength());
			System.out.printf("Path : %s\n", flow);

			System.out
					.printf("Destination %s has maxLat of %.3e ms, actual delay is %.3e ms\n",
							flow.getDestPort(), flow.getDestPortParameter()
									.getMaxLatencyRequirement() * 1000,
							flow.getDestPortParameter().getActualDelay() * 1000);
			System.out.println();
		}
	}

	public boolean checkDelays() {
		for (Flow flow : flows) {
			if (!flow.checkDelay())
				return false;
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
			if (!f.checkDelay()) {
				// TODO: Strafe für Delay > maxLatencyReq
				delay += 1;
			}
		}
		fitness = delay;
		return delay;
	}
}
