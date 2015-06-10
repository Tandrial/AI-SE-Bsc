package uni.dc.model;

import java.io.Serializable;

public class PortFlowPriority implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private EgressPort port;
	private Flow flow;
	private int priority;

	public PortFlowPriority() {	}

	public PortFlowPriority(EgressPort port, Flow flow, int priority) {
		this.port = port;
		this.flow = flow;
		this.priority = priority;
	}

	public EgressPort getPort() {
		return port;
	}

	public Flow getFlow() {
		return flow;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	protected Object clone() {
		PortFlowPriority rv = new PortFlowPriority();
		rv.port = this.port;
		rv.flow = this.flow;
		rv.priority = this.priority;
		return rv;
	}
}