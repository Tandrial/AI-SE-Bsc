package uni.dc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Flow implements Serializable {

	private String name;

	private EgressTopology topology;
	private EgressPort srcPort;

	private EgressPort destPort;
	private UbsDestParameters destPortParameter;

	private double rate;
	private int maxFrameLength;

	@Override
	public String toString() {
		return Arrays.toString(destPortParameter.getPath().toArray());
	}

	public String getName() {
		return name;
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

	public void setInitialMaxLatencyRequirement() {
		destPortParameter.setMaxLatencyRequirement(destPortParameter
				.getActualDelay());
	}

	public double getTotalDelay() {
		return destPortParameter.getActualDelay();
	}

	public void setTotalDelay(double totalDelay) {
		destPortParameter.setActualDelay(totalDelay);
	}

	public List<EgressPort> getPath() {
		return destPortParameter.getPath();
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

	public boolean checkDelay() {
		return destPortParameter.checkDelay();
	}

}
