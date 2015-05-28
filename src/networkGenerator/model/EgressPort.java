package networkGenerator.model;

import java.util.Set;

import networkGenerator.util.DeterministicHashSet;

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
}
