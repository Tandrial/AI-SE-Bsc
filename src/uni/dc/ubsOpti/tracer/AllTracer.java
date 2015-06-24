package uni.dc.ubsOpti.tracer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.goataa.impl.utils.Individual;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

public class AllTracer extends Tracer {
	private static final long serialVersionUID = 1L;

	private Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs = new HashMap<String, DelegateForest<Individual<int[], int[]>, String>>();
	private Map<String, Individual<int[], int[]>> endPoints = new HashMap<String, Individual<int[], int[]>>();
	private Map<String, Individual<int[], int[]>> startPoints = new HashMap<String, Individual<int[], int[]>>();

	@Override
	public void update(TracerStat stat) {
		if (stat.getPrio().length == 0)
			return;

		String algoName = stat.getName();
		if (!graphs.containsKey(algoName)) {
			graphs.put(algoName, new DelegateForest<Individual<int[], int[]>, String>());
			startPoints.put(algoName, stat.getData());
		}

		DelegateForest<Individual<int[], int[]>, String> graph = graphs.get(algoName);

//		if (algoName.equals("BT"))
			handleBT(stat, graph);
//		else
//			handleOther(stat, graph);
		endPoints.put(algoName, stat.getData());
	}

	private void handleBT(TracerStat stat, Forest<Individual<int[], int[]>, String> graph) {
		if (!graph.containsVertex(stat.getData())) {
			graph.addVertex(stat.getData());
		}

		for (Individual<int[], int[]> parent : stat.getParents()) {
			if (parent != null) {
				String edgeName = String.format("%s->%s", parent, stat.getData());
				String newEdgeName = edgeName;
				int cnt = 0;
				while (graph.containsEdge(newEdgeName)) {
					newEdgeName = edgeName + "_" + cnt++;
				}
				graph.addEdge(newEdgeName, parent, stat.getData());
			}
		}
	}

	private void handleOther(TracerStat stat, Forest<Individual<int[], int[]>, String> graph) {
		stat.getData().x = Arrays.copyOf(stat.getData().x, stat.getData().x.length);
		stat.getData().g = Arrays.copyOf(stat.getData().g, stat.getData().g.length);
		if (!graph.containsVertex(stat.getData())) {
			graph.addVertex(stat.getData());
		}
		if (stat.getParents().size() > 0) {
			Individual<int[], int[]> parent = endPoints.get(stat.getName());
			if (parent == stat.getData())
				return;
			String edgeName = String.format("%s->%s", parent, stat.getData());
			String newEdgeName = edgeName;
			int cnt = 0;
			while (graph.containsEdge(newEdgeName)) {
				newEdgeName = edgeName + "_" + cnt++;
			}
			graph.addEdge(newEdgeName, parent, stat.getData());
		}
	}

	public Map<String, DelegateForest<Individual<int[], int[]>, String>> getGraphs() {
		return graphs;
	}

	public Forest<Individual<int[], int[]>, String> getGraph(String algoName) {
		return graphs.get(algoName);
	}

	public Individual<int[], int[]> getStartPoint(String algoName) {
		return startPoints.get(algoName);
	}

	public Individual<int[], int[]> getEndPoint(String algoName) {
		return endPoints.get(algoName);
	}
}
