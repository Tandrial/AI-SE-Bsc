package uni.dc.model;

import java.util.Set;

import uni.dc.util.DeterministicHashSet;

public class Node implements Cloneable {

	private Set<EgressPort> ports = new DeterministicHashSet<EgressPort>();

	private String name;

	private int cnt;

	public Node() {
		super();
		cnt = 0;
	}

	public Node(String name) {
		this();
		this.name = name;
	}

	public Set<EgressPort> getPorts() {
		return ports;
	}

	public void setPorts(Set<EgressPort> ports) {
		this.ports = ports;
	}

	public void addPort(EgressPort port) {
		port.setName(getName() + ".P" + cnt++);
		ports.add(port);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
