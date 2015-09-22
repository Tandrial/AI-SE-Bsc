package uni.dc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Flow implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private double rate;
	private int maxFrameLength;
	private double maxAllowedDelay = 0.0d;

	private EgressPort srcPort;
	private EgressPort destPort;
	private List<EgressPort> path;
	private double delay = 0.0d;

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

	public double getMaxAllowedDelay() {
		return maxAllowedDelay;
	}

	public void setMaxAllowedDelay(double maxAllowedLatency) {
		this.maxAllowedDelay = maxAllowedLatency;
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

	public double getDiffDelayAllowedDelay() {
		return delay - maxAllowedDelay;
	}

	public boolean checkDelay() {
		return maxAllowedDelay == Double.MAX_VALUE || maxAllowedDelay >= delay;
	}

	@Override
	public String toString() {
		return Arrays.toString(path.toArray());
	}

	public void setInitialMaxAllowedDelay(double modifier) {
		this.maxAllowedDelay = this.delay * modifier;
		this.delay = 0;
	}
}
