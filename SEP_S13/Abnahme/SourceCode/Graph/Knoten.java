package Graph;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Knoten implements Serializable {

	private static final long serialVersionUID = -8283798446079156683L;
	// id, lat, lon sind vom Parser aus der .osm ausgelesen
	private long id;
	private float lat;
	private float lon;

	private boolean erreichbar = false;

	private float muellmenge;
	private String strassenTyp;
	private String strassenName;

	// x,y Kordinate zum Zeichnen. Nur noetig solange es noch keine besondere
	// Kartendarstellung gibt
	private Point2D.Float location;

	// Liste mit allen verbundenen Knoten
	private ArrayList<Kante> kanten = new ArrayList<Kante>();

	// Check für BFS-Search
	private boolean visited = false;

	// Knoten id für den CPP-Algo
	private int cppId;

	// Getter&Setter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public void setLat(String lat) {
		this.lat = Float.parseFloat(lat);
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public void setLon(String lon) {
		this.lon = Float.parseFloat(lon);
	}

	public boolean getErreichbar() {
		return erreichbar;
	}

	public void setErreichbar(boolean erreichbar) {
		this.erreichbar = erreichbar;
	}

	public float getMuellmenge() {
		return muellmenge;
	}

	public void setMuellmenge(float muellmenge) {
		this.muellmenge = muellmenge;
	}

	public String getStrassenTyp() {
		return strassenTyp;
	}

	public void setStrassenTyp(String strassenTyp) {
		this.strassenTyp = strassenTyp;
	}

	public String getStrassenName() {
		return strassenName;
	}

	public void setStrassenName(String strassenName) {
		this.strassenName = strassenName;
	}

	public Point2D.Float getLocation() {
		return location;
	}

	public void setLocation(Point2D.Float location) {
		this.location = location;
	}

	public void setLocation(float x, float y) {
		this.location = new Point2D.Float(x, y);
	}

	public ArrayList<Kante> getKanten() {
		return kanten;
	}

	public void setKanten(ArrayList<Kante> kanten) {
		this.kanten = kanten;
	}

	public void addKante(Kante kante) {
		this.kanten.add(kante);
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getCppId() {
		return cppId;
	}

	public void setCppId(int cppId) {
		this.cppId = cppId;
	}

	// Ende Getter&Setter

	public Knoten(String id, String tag) {
		this.id = Long.parseLong(id);
		this.strassenTyp = tag;
	}

	// Kopiert Koordinaten VON DIESEM knoten in den Parameter
	public void copyCords(Knoten node) {
		node.setLocation(this.location);
		node.setLat(this.lat);
		node.setLon(this.lon);
	}

	public Kante getKanteTo(Knoten k) {
		for (Kante kante : this.getKanten()) {
			if (kante.getOther(k) != null)
				return kante;
		}
		return null;
	}

	public String toString() {
		// return "[[id=" + this.id + "] [lat=" + this.lat + "] [lon=" +
		// this.lon + "] [" + location.toString() + "]]";
		return "[[id=" + this.id + "]]";

	}

}