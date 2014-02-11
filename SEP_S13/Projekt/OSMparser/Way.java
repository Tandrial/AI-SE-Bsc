package OSMparser;

import java.util.*;

import Graph.Knoten;

public class Way {

	private long id;
	private String k;
	private String v;
	private String strassenName;

	private boolean gesperrt;

	private ArrayList<Knoten> knoten = new ArrayList<Knoten>();

	public Way(String id) {
		this.id = Long.parseLong(id);
		this.k = "";
		this.v = "";
		this.strassenName = "";
		this.gesperrt = false;
	}

	// Getter&Setter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getStrassenName() {
		return strassenName;
	}

	public void setStrassenName(String strassenName) {
		this.strassenName = strassenName;
	}

	public boolean isGesperrt() {
		return gesperrt;
	}

	public void setGesperrt(boolean gesperrt) {
		this.gesperrt = gesperrt;
	}

	public ArrayList<Knoten> getKnoten() {
		return knoten;
	}

	public void setKnoten(ArrayList<Knoten> knoten) {
		this.knoten = knoten;
	}

	public void add(Knoten node) {
		this.knoten.add(node);
	}

	public void add(String id) {
		this.knoten.add(new Knoten(id, v));
	}

	// Ende Getter&Setter

	public void findNodes(Map<Long, Knoten> nodeMap) {
		for (Knoten node : knoten) {
			nodeMap.get(node.getId()).copyCords(node);
			node.setStrassenTyp(v);
			node.setStrassenName(strassenName);
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[[id=" + this.id + "] [k=" + this.k + "] [v=" + this.v
				+ "] [zu=" + this.gesperrt + "]]\n");

		for (Knoten node : knoten) {
			result.append("  " + node + "\n");
		}
		return result.toString();
	}
}