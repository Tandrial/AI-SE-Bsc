package uni.dc.ubsOpti.tracer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.goataa.impl.utils.Individual;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

public class AllTracer extends Tracer {
	private static final long serialVersionUID = 1L;
	// Map with prio Individual for each Algo

	private Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs = new HashMap<String, DelegateForest<Individual<int[], int[]>, String>>();
	private Map<String, HashMap<String, Individual<int[], int[]>>> exisitingVertices = new HashMap<String, HashMap<String, Individual<int[], int[]>>>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0)
			return;
		String algoName = stat.getName();

		if (!exisitingVertices.containsKey(algoName)) {
			graphs.put(algoName, new DelegateForest<Individual<int[], int[]>, String>());
			exisitingVertices.put(algoName, new HashMap<String, Individual<int[], int[]>>());
		}
		DelegateForest<Individual<int[], int[]>, String> graph = graphs.get(algoName);
		int[] arr = stat.getPrio();
		Individual<int[], int[]> curr = exisitingVertices.get(algoName).get(Arrays.toString(arr));
		if (curr == null) {
			curr = stat.getData();
			exisitingVertices.get(algoName).put(Arrays.toString(stat.getPrio()), curr);
			graph.addVertex(curr);
		}

		for (Individual<int[], int[]> parent : stat.getParents()) {
			if (parent != null) {
				Individual<int[], int[]> currP = exisitingVertices.get(algoName).get(Arrays.toString(parent.x));
				String edgeName = String.format("%s->%s", currP, stat.getData());
				if (!graph.containsEdge(edgeName))
					graph.addEdge(edgeName, currP, curr);
			}
		}
	}

	public Map<String, DelegateForest<Individual<int[], int[]>, String>> getGraphs() {
		return graphs;
	}

	public Forest<Individual<int[], int[]>, String> getGraph(String algoName) {
		return graphs.get(algoName);
	}
}
