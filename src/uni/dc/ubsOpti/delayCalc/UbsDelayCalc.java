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

public abstract class UbsDelayCalc extends OptimizationModule implements IObjectiveFunction<int[]>, Serializable {
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
		double delay = 0.0;
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
			if (prioOther == -1)
				continue;
			if (prioOther < prioF) {
				sizeBiggerEq += other.getMaxFrameLength();
				rateHigher += other.getRate();
			} else if (prioOther == prioF) {
				sizeBiggerEq += other.getMaxFrameLength();
			} else {
				maxSmaller = Math.max(maxSmaller, other.getMaxFrameLength());
			}
		}
		delay = (sizeBiggerEq + maxSmaller) / (linkSpeed - rateHigher) + size / linkSpeed;

		return delay;
	}

	public void setInitialDelays(PriorityConfiguration prio, double modifier) {
		this.prio = prio;
		calculateDelays();
		for (Flow f : flows) {
			f.setInitialMaxAllowedDelay(modifier);
		}
	}

	@Override
	public String toString(boolean longVersion) {
		StringBuilder sb = new StringBuilder();
		for (Flow flow : flows) {
			sb.append(String.format("%s : %.0f Mbps, %d bit\n", flow.getName(), flow.getRate() / 1e6,
					flow.getMaxFrameLength()));
			sb.append(String.format("Path : %s\n", flow));
			if (flow.getMaxAllowedDelay() == Double.MAX_VALUE) {
				sb.append(String.format("Destination %s delay is %.3e s\n", flow.getDestPort(), flow.getDelay()));
			} else {
				sb.append(String.format("Destination %s maxLat of %.3e s, delay is %.3e s\n", flow.getDestPort(),
						flow.getMaxAllowedDelay(), flow.getDelay()));
			}
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
			// TODO fitness Function
			delay += Math.abs(f.getDiffDelayAllowedDelay());
			if (!f.checkDelay()) {
				// TODO: Strafe für Delay > maxLatencyReq
				delay += 2 * Math.abs(f.getDiffDelayAllowedDelay());
			}
		}
		return delay;
	}
}
