package Graph;

import java.awt.geom.Point2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import OSMparser.Way;

public class Graph implements Serializable {

	private static final long serialVersionUID = -263467792751810946L;
	// statische Felder & Methoden

	private static Graph kartenAuschnitt;

	public static Graph getGraph() {
		return Graph.kartenAuschnitt;
	}

	public static void setGraph(Graph g) {
		Graph.kartenAuschnitt = g;
	}

	// Ende statische Felder & Methoden

	// Bereich in dem sich alle Knoten befinden
	private Point2D.Float min;
	private Point2D.Float max;

	// Liste aller Knoten die im Graphen sind
	private List<Knoten> knoten = new ArrayList<Knoten>();

	// Liste aller Muellentleerungsorte für Plastik die im Graphen sind
	private Knoten plastik;

	// Liste aller Muellentleerungsorte für Papier die im Graphen sind
	private Knoten papier;

	// Liste aller Muellentleerungsorte für Restmuell die im Graphen sind
	private Knoten rest;

	// Liste aller Muellentleerungsorte für Biomuell die im Graphen sind
	private Knoten bio;

	// Depot als Start-/Endpunkt fuer Berechnungen
	private Knoten depot;

	// Getter&Setter
	public List<Knoten> getKnoten() {
		return knoten;
	}

	public void setKnoten(List<Knoten> knoten) {
		this.knoten = knoten;
	}

	public Knoten getPlastik() {
		return plastik;
	}

	public void setPlastik(Knoten plastik) {
		this.plastik = plastik;
	}

	public Knoten getPapier() {
		return papier;
	}

	public void setPapier(Knoten papier) {
		this.papier = papier;
	}

	public Knoten getRest() {
		return rest;
	}

	public void setRest(Knoten rest) {
		this.rest = rest;
	}

	public Knoten getBio() {
		return bio;
	}

	public void setBio(Knoten bio) {
		this.bio = bio;
	}

	public Knoten getDepot() {
		return depot;
	}

	public void setDepot(Knoten depot) {
		this.depot = depot;
	}

	public Point2D.Float getMin() {
		return min;
	}

	public Point2D.Float getMax() {
		return max;
	}

	// Ende Getter&Setter

	private Graph() {
	}

	/**
	 * Erzeugt einen neuen Graph aus den gegeben Ways
	 * 
	 * @param min
	 *            - Punkt oben links des Kartenauschnittes
	 * 
	 * @param max
	 *            - Punkt unten rechts des Kartenauschnittes
	 * 
	 * @param way
	 *            - Eine Liste von ways, aus einer .osm Datei eingelesen
	 * 
	 */

	public Graph(Point2D.Float min, Point2D.Float max, List<Way> ways) {
		this.min = min;
		this.max = max;
		waysToGraph(ways);
	}

	/**
	 * Wandelt die übergebenen Ways in die Datenstruktur des Graphen um.
	 * 
	 * @param way
	 *            - Eine Liste von ways, aus einer .osm Datei eingelesen
	 * 
	 */
	private void waysToGraph(List<Way> ways) {

		List<Kante> kList = new ArrayList<Kante>();
		Map<Long, Knoten> knotenMap = new HashMap<Long, Knoten>();

		for (Way way : ways) {
			ArrayList<Knoten> nodes = way.getKnoten();
			for (int i = 0; i < nodes.size(); i++) {
				Knoten tmp = nodes.get(i);
				if (knotenMap.get(tmp.getId()) == null) {
					this.knoten.add(tmp);
					knotenMap.put(tmp.getId(), tmp);
				}
			}
		}

		for (Way way : ways) {
			ArrayList<Knoten> knoten = way.getKnoten();
			for (int i = 0; i < knoten.size() - 1; i++) {
				long id1 = knoten.get(i).getId();
				long id2 = knoten.get(i + 1).getId();

				Knoten k1 = knotenMap.get(id1);
				Knoten k2 = knotenMap.get(id2);

				Kante tmp = new Kante(k1, k2, way.isGesperrt(), way.getV());

				if (tmp.getGesperrt())
					kList.add(tmp);
				k1.addKante(tmp);
				k2.addKante(tmp);

			}
		}
		if (kList.size() > 0)
			fixGesperrteStraßen(kList);
	}

	private void fixGesperrteStraßen(List<Kante> kList) {
		for (Kante k : kList) {
			String typ = "";
			boolean singleTyp = true;
			Knoten k1 = k.getFrom();
			Knoten k2 = k.getTo();

			for (Kante tmp : k1.getKanten()) {
				if (tmp.getTyp().equals("gesperrt"))
					continue;
				if (typ.equals(""))
					typ = tmp.getTyp();
				else if (!tmp.getTyp().equals(typ))
					singleTyp = false;
			}
			if (!singleTyp) {
				typ = "";
				singleTyp = true;
				for (Kante tmp : k2.getKanten()) {
					if (tmp.getTyp().equals("gesperrt"))
						continue;
					if (!tmp.getTyp().equals(typ))
						singleTyp = false;
				}
			}
			if (singleTyp) {
				k.setTyp(typ);
			} else {
				// TODO Wie Straßentyp bestimmen wenn beide verbundenen Knoten
				// Kanten mit mehreren Straßentypen haben
				k.setTyp(typ);
			}
		}
	}

	/**
	 * Wandelt die übergebenen Kanten in einen Graphen um
	 * 
	 * @param kanten
	 *            - Eine Liste von Kante aus der der Graph erstellt wird
	 * 
	 */
	public static Graph graphFromKanten(List<Kante> kanten) {
		Graph g = new Graph();

		for (Kante k : kanten) {
			if (!g.getKnoten().contains(k.getTo()))
				g.getKnoten().add(k.getTo());
			if (!g.getKnoten().contains(k.getFrom()))
				g.getKnoten().add(k.getFrom());
		}

		Map<Long, Knoten> knotenMap = new HashMap<Long, Knoten>();
		for (Knoten k : g.getKnoten()) {
			knotenMap.put(k.getId(), k);
		}
		for (Kante k : kanten) {

			Knoten k1 = knotenMap.get(k.getFrom().getId());
			Knoten k2 = knotenMap.get(k.getTo().getId());

			k1.addKante(k);
			k2.addKante(k);
		}
		return g;
	}

	/**
	 * Führt vom Depot aus eine Breitensuche durch, und LÖSCHT alle nicht
	 * erreichbaren Knoten aus dem Graph
	 * 
	 * @return - true wenn der Graph zusammenhängend ist
	 * 
	 */
	public boolean checkGraphZusammenhang() {
		List<Knoten> connected = new ArrayList<Knoten>();
		boolean result = true;
		connected = bfs(this.getDepot());

		for (int i = this.knoten.size() - 1; i >= 0; i--) {

			if (!connected.contains(this.knoten.get(i))) {
				this.knoten.remove(i);
				result = false;
			}
		}
		return result;
	}

	// Methode zur Breitensuche
	private List<Knoten> bfs(Knoten start) {

		List<Knoten> result = new ArrayList<Knoten>();

		java.util.Queue<Knoten> q = new LinkedList<Knoten>();
		q.add(start);
		result.add(start);
		start.setVisited(true);
		while (!q.isEmpty()) {
			Knoten n = (Knoten) q.remove();
			Knoten child = null;
			while ((child = getUnvisitedChildNode(n)) != null) {
				child.setVisited(true);
				result.add(child);
				q.add(child);
			}
		}

		for (Knoten k : this.knoten) {
			k.setVisited(false);
		}
		return result;
	}

	private static Knoten getUnvisitedChildNode(Knoten n) {
		for (Kante k : n.getKanten())
			if (!k.getOther(n).isVisited())
				return k.getOther(n);
		return null;
	}

	/**
	 * Speichert den Graph g in der Datei fileName
	 * 
	 * @param fileName
	 *            - Der Name der Datei
	 * 
	 * @param g
	 *            - Der zuspeicherne Graph
	 * 
	 */
	public static void saveToFile(File fileName, Graph g) {
		if (g != null && g.getKnoten().size() > 0) {
			long size = g.getKnoten().size();

			if (size > allgemein.Konstanten.MAX_GRAPH_SIZE) {
				JOptionPane.showMessageDialog(null,
						"Der Kartenauschnitt ist zu groß um gespeichert zuwerden. ("
								+ size + " Knoten Das Maximum ist "
								+ allgemein.Konstanten.MAX_GRAPH_SIZE + ")",
						"Graph zu groß!", JOptionPane.WARNING_MESSAGE);
				return;
			}

			try {
				FileOutputStream fileOut = new FileOutputStream(fileName);
				BufferedOutputStream bout = new BufferedOutputStream(fileOut);
				ObjectOutputStream out = new ObjectOutputStream(bout);
				out.writeObject(g);
				out.close();
				fileOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			return;
	}

	/**
	 * Lädt einen Graph aus der gegeben Datei aus, speichert diesen im
	 * statischen Graph
	 * 
	 * @param fileName
	 *            - Der Name der Datei
	 * 
	 * @return - Der geladenen Graph
	 * 
	 */
	public static Graph loadFromFile(File fileName) {
		Graph g = null;
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			g = (Graph) in.readObject();
			in.close();
			fileIn.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Graph.setGraph(g);
		return g;
	}

	/**
	 * Sucht einen Knoten der den Koordinaten am nächsten liegt
	 * 
	 * @param x
	 *            - Die X-Koordinate eines Mausklicks
	 * 
	 * @param y
	 *            - Die Y-Koordinate eines Mausklicks
	 * 
	 * @return - Der dem Mausklick am nächsten gelegenen Knoten
	 */
	public Knoten getClosestKnoten(double input_x, double input_y) {
		Knoten closest = null;
		double minabstand = Double.MAX_VALUE;

		for (Knoten k : this.getKnoten()) {

			double a = (input_x - k.getLat());
			double b = (input_y - k.getLon());

			double c_quadrat = Math.pow(a, 2) + Math.pow(b, 2);

			double c = Math.sqrt(c_quadrat);

			if (c < minabstand) {
				minabstand = c;
				closest = k;
			}
		}
		return closest;
	}

	public Kante getClosestKante(double input_x, double input_y) {
		Kante closest = null;
		double minabstand = Double.MAX_VALUE;

		for (Kante k : this.getAlleKanten()) {

			double a = (input_x - k.getMidPoint().getX());
			double b = (input_y - k.getMidPoint().getY());

			double c_quadrat = Math.pow(a, 2) + Math.pow(b, 2);

			double c = Math.sqrt(c_quadrat);

			if (c < minabstand) {
				minabstand = c;
				closest = k;
			}
		}
		return closest;
	}

	/**
	 * Liefert die gesamte Muellmenge des Graphen zurueck
	 * 
	 * @return gesamte Muellmenge des Graphen zurueck
	 */
	public float gettotmuell() {
		float totmuell = 0f;
		List<Kante> alleKanten = getAlleKanten();
		for (int i = 0; i < alleKanten.size(); i++) {
			totmuell += alleKanten.get(i).getMuellMengeArt("");
		}
		return totmuell;
	}

	/**
	 * Liefert eine Liste mit allen Kanten die sich im Graph befinden
	 * 
	 * @return alle Kanten des Graphen als Liste
	 */
	public List<Kante> getAlleKanten() {
		ArrayList<Kante> result = new ArrayList<Kante>();

		for (Knoten knoten : this.getKnoten()) {
			for (Kante kante : knoten.getKanten()) {
				if (!result.contains(kante))
					result.add(kante);
			}
		}
		return result;
	}

	/**
	 * Setzt die Muellmenge, und die Erreichbarkeit im Graphen zurück
	 */
	public void resetGraph() {
		for (Knoten kn : this.getKnoten())
			kn.setErreichbar(false);
		for (Kante ka : this.getAlleKanten()) {
			ka.calcMuellmenge();
			ka.setErreichbar(false);
		}
	}

	/**
	 * Liefert eine Liste mit allen erreichbaren Kanten die sich im Graph
	 * befinden
	 * 
	 * @return alle erreichbaren Kanten des Graphen als Liste
	 */
	public List<Kante> getErreichbareKanten() {
		ArrayList<Kante> result = new ArrayList<Kante>();

		for (Knoten knoten : this.getKnoten()) {
			for (Kante kante : knoten.getKanten()) {
				if (!result.contains(kante) && kante.getErreichbar())
					result.add(kante);
			}
		}
		return result;
	}
}