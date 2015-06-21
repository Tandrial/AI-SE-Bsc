package uni.dc.ubsOpti.tracer;

import java.util.HashMap;
import java.util.Map;

import org.goataa.impl.utils.Individual;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

public class AllTracer extends Tracer {
	private static final long serialVersionUID = 1L;
	private Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs = new HashMap<String, DelegateForest<Individual<int[], int[]>, String>>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0)
			return;

		if (!graphs.containsKey(stat.getName())) {
			graphs.put(stat.getName(), new DelegateForest<Individual<int[], int[]>, String>());
		}

		DelegateForest<Individual<int[], int[]>, String> graph = graphs.get(stat.getName());

		if (!graph.getVertices().contains(stat.getData())) {
			graph.addVertex(stat.getData());
		}
		for (Individual<int[], int[]> parent : stat.getParents()) {
			if (parent != null) {
				String edgeName = String.format("%s->%s", parent, stat.getData());
				if (!graph.getEdges().contains(edgeName))
					graph.addEdge(edgeName, parent, stat.getData());
			}
		}
	}

	public Forest<Individual<int[], int[]>, String> getGraph(String algoName) {
		return graphs.get(algoName);
	}
}
