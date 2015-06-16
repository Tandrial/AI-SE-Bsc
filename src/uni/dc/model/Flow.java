package uni.dc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Flow implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private double rate;
	private int maxFrameLength;
	private double maxLatency = 0.0d;

	private EgressPort srcPort;
	private EgressPort destPort;
	private List<EgressPort> path;
	private double delay = 0.0d;

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

	public double getMaxLatency() {
		return maxLatency;
	}

	public void setMaxLatency(double maxLatency) {
		this.maxLatency = maxLatency;
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

	public List<EgressPort> getPath() {
		return path;
	}

	public void setPath(List<EgressPort> path) {
		this.path = path;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public boolean checkDelay() {
		return maxLatency == -1 || maxLatency >= delay;
	}

	@Override
	public String toString() {
		return Arrays.toString(path.toArray());
	}

	public void setInitialMaxLatencyRequirement() {
		this.maxLatency = this.delay;
	}
}
