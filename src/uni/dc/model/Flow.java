package uni.dc.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Flow {

	private String name;

	private EgressTopology topology;
	private EgressPort srcPort;

	private Set<EgressPort> destPortSet;
	private Map<EgressPort, UbsDestParameters> destPortParameterMap;

	private double rate;
	private int maxFrameLength;

	@Override
	public String toString() {
		for (EgressPort iter : destPortParameterMap.keySet()) {
			return Arrays.toString(destPortParameterMap.get(iter).getPath()
					.toArray());
		}
		return "";
	}

	public String getName() {
		return name;
	}

	public void speedUp(double inc) {
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			iter.decSpeedFactor(inc);
		}
	}

	public void slowDown(double dec) {
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			iter.incSpeedFactor(dec);
		}
	}

	public void resetSpeed() {
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			iter.resetSpeed();
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public EgressPort getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(EgressPort srcPort) {
		this.srcPort = srcPort;
	}

	public Set<EgressPort> getDestPortSet() {
		return destPortSet;
	}

	public void setDestPortSet(Set<EgressPort> destPortSet) {
		this.destPortSet = destPortSet;
	}

	public Map<EgressPort, UbsDestParameters> getDestPortParameterMap() {
		return destPortParameterMap;
	}

	public void setDestPortParameterMap(
			Map<EgressPort, UbsDestParameters> destPortParameterMap) {
		this.destPortParameterMap = destPortParameterMap;
	}

	public void setInitialMaxLatencyRequirement() {
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			iter.setMaxLatencyRequirement(iter.getActualDelay());
		}
	}

	public double getTotalDelay() {
		// For now no multicast
		double delay = 0.0;
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			delay = iter.getActualDelay();
		}
		return delay;
	}

	public void setTotalDelay(double totalDelay) {
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			iter.setActualDelay(totalDelay);
		}
	}

	public List<EgressPort> getPath() {
		// For now no multicast
		List<EgressPort> delay = null;
		for (UbsDestParameters iter : destPortParameterMap.values()) {
			delay = iter.getPath();
		}
		return delay;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getMaxFrameLength() {
		return maxFrameLength;
	}

	public void setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}
}
