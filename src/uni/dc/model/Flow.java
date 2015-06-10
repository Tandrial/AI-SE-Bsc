package uni.dc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Flow implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private double rate;
	private int maxFrameLength;
	private EgressPort srcPort;
	private EgressPort destPort;
	private UbsDestParameters destPortParameter;
	private EgressTopology topology;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public EgressPort getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(EgressPort srcPort) {
		this.srcPort = srcPort;
	}

	public EgressPort getDestPort() {
		return destPort;
	}

	public void setDestPort(EgressPort destPort) {
		this.destPort = destPort;
	}

	public UbsDestParameters getDestPortParameter() {
		return destPortParameter;
	}

	public void setDestPortParameter(UbsDestParameters destPortParameter) {
		this.destPortParameter = destPortParameter;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public void setInitialMaxLatencyRequirement() {
		destPortParameter
				.setMaxLatencyRequirement(destPortParameter.getDelay());
	}

	public List<EgressPort> getPath() {
		return destPortParameter.getPath();
	}

	public double getDelay() {
		return destPortParameter.getDelay();
	}

	public void setDelay(double delay) {
		destPortParameter.setDelay(delay);
	}

	public boolean checkDelay() {
		return destPortParameter.checkDelay();
	}

	@Override
	public String toString() {
		return Arrays.toString(destPortParameter.getPath().toArray());
	}
}
