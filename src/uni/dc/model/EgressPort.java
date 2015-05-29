package uni.dc.model;

import java.util.Set;

import uni.dc.util.DeterministicHashSet;

public class EgressPort implements Cloneable {

	private Set<Flow> flowList = new DeterministicHashSet<Flow>();

	private String name;

	public EgressPort() {
		super();
	}

	public EgressPort(String name) {
		this();
		this.name = name;
	}

	public Set<Flow> getFlowList() {
		return flowList;
	}

	public void setFlowList(Set<Flow> flowList) {
		this.flowList = flowList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
