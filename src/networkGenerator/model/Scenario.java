package networkGenerator.model;

import java.util.HashSet;
import java.util.Set;

public class Scenario implements Cloneable {

	private Set<Flow> flowSet;

	public Scenario() {
		flowSet = new HashSet<Flow>();
	}
}
