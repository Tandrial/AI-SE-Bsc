package uni.dc.model;

import java.io.Serializable;

public class PortFlowPriority implements Cloneable, Serializable {
	private EgressPort port;
	private Flow flow;
	private Integer priority;

	public PortFlowPriority() {
		super();
	};

	public PortFlowPriority(EgressPort port, Flow flow, Integer priority) {
		super();
		this.port = port;
		this.flow = flow;
		this.priority = priority;
	}

	public EgressPort getPort() {
		return port;
	}

	public void setPort(EgressPort port) {
		this.port = port;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
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
		rv.priority = new Integer(this.priority);
		return rv;
	}
}