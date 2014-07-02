package routenberechnung;

//Arup Guha
//6/25/02
//Floyd Warshall's algorithm: an example of dynamic programming.

import java.io.File;
import java.util.*;

import Graph.Graph;
import Graph.Kante;
import Graph.Knoten;

public class floyd {

	public static float[][] shortestpath(float[][] adj, int[][] path) {

		int n = adj.length;
		float[][] ans = new float[n][n];

		copy(ans, adj);

		// Compute successively better paths through vertex k.
		for (int k = 0; k < n; k++) {

			// Do so between each possible pair of points.
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {

					if (ans[i][k] + ans[k][j] < ans[i][j]) {
						ans[i][j] = ans[i][k] + ans[k][j];
						path[i][j] = path[k][j];
					}
				}
			}
		}

		// Return the shortest path matrix.
		return ans;
	}

	// Copies the contents of array b into array a. Assumes that both a and
	// b are 2D arrays of identical dimensions.
	public static void copy(float[][] a, float[][] b) {

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++)
				a[i][j] = b[i][j];
	}

	private static void setKnotenNummern(Graph g) {
		int id = 0;
		for (Knoten k : g.getKnoten()) {
			k.setCppId(id++);
		}
		id = 0;
		for (Kante k : g.getAlleKanten()) {
			k.id = id++;
		}
	}

	public static void main(String[] args) {

		Graph g;
		g = Graph.loadFromFile(new File("Graph.xml"));

		setKnotenNummern(g);

		System.out.println("Euler? " + isEuler(g));

		int c = 0;
		for (Knoten k : g.getKnoten())
			c += k.getKanten().size();
		System.out.println(c + " Kanten im Graph");
		while (!isEuler(g)) {

			Map<Integer, Knoten> knoten = new HashMap<Integer, Knoten>();
			for (Knoten k : g.getKnoten()) {
				knoten.put(k.getCppId(), k);
			}

			int N = g.getKnoten().size();
			float[][] m = new float[N][N];

			for (Kante k : g.getAlleKanten()) {
				m[k.getTo().getCppId()][k.getFrom().getCppId()] = k
						.getAbstand();
				m[k.getFrom().getCppId()][k.getTo().getCppId()] = k
						.getAbstand();
			}

			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m.length; j++) {

					if (m[i][j] == 0)
						m[i][j] = 10000f;
				}
			}

			float[][] shortpath;
			int[][] path = new int[N][N];

			// Initialize with the previous vertex for each edge. -1 indicates
			// no such vertex.
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++)
					if (m[i][j] == 10000f)
						path[i][j] = -1;
					else
						path[i][j] = i;

			shortpath = shortestpath(m, path);

			List<Knoten> addEdge = new ArrayList<Knoten>();

			for (Knoten k : g.getKnoten()) {
				if (k.getKanten().size() % 2 == 1) {
					addEdge.add(k);
				}
			}

			double[][] H = new double[addEdge.size()][addEdge.size()];

			for (int i = 0; i < addEdge.size(); i++) {
				for (int j = 0; j < addEdge.size(); j++) {
					H[i][j] = shortpath[i][j];
					H[j][i] = H[i][j];
				}
			}

			HungarianAlgorithm test = new HungarianAlgorithm(H);

			int[] result = test.execute();

			for (int i = 0; i < result.length; i++) {
				if (result[result[i]] == -1)
					continue;
				// if (i != result[i]) System.out.println(i + " ->" +
				// result[i]);
				int start = addEdge.get(i).getCppId();
				int end = addEdge.get(result[i]).getCppId();

				String myPath = end + "";
				while (path[start][end] != start) {
					myPath = path[start][end] + "," + myPath;
					end = path[start][end];
				}
				myPath = start + "," + myPath;
				// System.out.println("Here's the path " + myPath);

				String[] nodesPath = myPath.split(",");

				for (int j = 0; j < nodesPath.length - 1; j++) {
					Knoten k1 = knoten.get(Integer.parseInt(nodesPath[j]));
					Knoten k2 = knoten.get(Integer.parseInt(nodesPath[j + 1]));
					Kante kant = k1.getKanteTo(k2);
					k1.addKante(kant);
					k2.addKante(kant);

					result[i] = -1;
				}

			}
		}
		System.out.println("Euler? " + isEuler(g));
		c = 0;
		for (Knoten k : g.getKnoten())
			c += k.getKanten().size();
		System.out.println(c + " Kanten im Graph");
	}

	private static boolean isEuler(Graph g) {
		int count = 0;
		for (Knoten k : g.getKnoten()) {
			if (k.getKanten().size() % 2 == 1) {
				count++;
			}
		}
		System.out.println("anzahl oddVertexes: " + count);
		return count == 0;
	}
}
