package uni.dc.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Node implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private int cnt;
	private Set<EgressPort> ports = new LinkedHashSet<EgressPort>();

	public Node(String name) {
		this.name = name;
		cnt = 0;
	}

	public String getName() {
		return name;
	}

	public Set<EgressPort> getPorts() {
		return ports;
	}

	public void addPort(EgressPort port) {
		port.setName(name + ".P" + cnt++);
		ports.add(port);
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
