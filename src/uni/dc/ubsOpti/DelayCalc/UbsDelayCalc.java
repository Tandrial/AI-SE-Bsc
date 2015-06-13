package uni.dc.ubsOpti.delayCalc;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.goataa.impl.OptimizationModule;
import org.goataa.spec.IObjectiveFunction;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;

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
		flows = new LinkedHashSet<Flow>();
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

	protected double calcDelay(EgressPort lastEgress, Flow f) {
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
				maxSmaller = Math.max(maxSmaller, other.getMaxFrameLength());
			}
		}
		delay = (sizeBiggerEq + maxSmaller) / (linkSpeed - rateHigher) + size
				/ linkSpeed;
		return delay;
	}

	public void setInitialDelays(PriorityConfiguration prio) {
		this.prio = prio;
		calculateDelays();
		for (Flow f : flows) {
			f.setInitialMaxLatencyRequirement();
		}
	}

	@Override
	public String toString(boolean longVersion) {
		StringBuilder sb = new StringBuilder();
		for (Flow flow : flows) {
			sb.append(String.format("%s : %.0f Mbps, %d bit\n", flow.getName(),
					flow.getRate() / 1e6, flow.getMaxFrameLength()));
			sb.append(String.format("Path : %s\n", flow));

			sb.append(String
					.format("Destination %s has maxLat of %.3e s, actual delay is %.3e s\n",
							flow.getDestPort(), flow.getDestPortParameter()
									.getMaxLatencyRequirement(), flow
									.getDestPortParameter().getDelay()));
		}

		return sb.toString();
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
			delay += f.getDelay();
			if (!f.checkDelay()) {
				// TODO: Strafe für Delay > maxLatencyReq
				delay += 1;
			}
		}
		fitness = delay;
		return delay;
	}
}
