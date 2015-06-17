package uni.dc.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class EgressPort implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Node node;
	private double linkSpeed = 1e9;
	private Set<Flow> flowList = new LinkedHashSet<Flow>();

	public EgressPort() {
		super();
	}

	public EgressPort(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public double getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(double linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public Set<Flow> getFlowList() {
		return flowList;
	}

	public void setFlowList(Set<Flow> flowList) {
		this.flowList = flowList;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EgressPort other = (EgressPort) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
