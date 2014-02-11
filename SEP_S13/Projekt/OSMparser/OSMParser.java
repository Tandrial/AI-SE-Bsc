package OSMparser;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import Graph.*;

public class OSMParser {

	private ArrayList<Knoten> knoten = new ArrayList<Knoten>();
	private ArrayList<Way> ways = new ArrayList<Way>();

	private Point2D.Float min;
	private Point2D.Float max;

	public List<Knoten> getKnoten() {
		return knoten;
	}

	public List<Way> getWays() {
		return ways;
	}

	public Point2D.Float getMin() {
		return min;
	}

	public Point2D.Float getMax() {
		return max;
	}

	/**
	 * Erzeugt einen neuen Parser mit einer gegebenen Kartengröße.
	 * 
	 * @param file
	 *            - Die .osm Datei die eingelesen werden soll.
	 * 
	 * @param mapSpize
	 *            - Die Kartengröße in Pixeln
	 * 
	 */
	public OSMParser(File file) {
		try {
			FileReader reader = new FileReader(file);
			InputSource inputSource = new InputSource(reader);
			OSMHandler parser = new OSMHandler(knoten, ways);
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			xmlReader.setContentHandler(parser);
			xmlReader.parse(inputSource);

			this.min = parser.getMin();
			this.max = parser.getMax();

			Map<Long, Knoten> knotenMap = new HashMap<Long, Knoten>();
			for (Knoten node : knoten) {
				knotenMap.put(node.getId(), node);
			}

			// Füllt die fehlenden Infos über die Knoten in den Ways aus
			for (Way way : ways) {
				way.findNodes(knotenMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Erstellt aus der geparsten .osm Datei einen Graphen, speichert diesen
	 * under Graph.getGraph()
	 * 
	 * @return - liefert den Erstellen Graphen
	 * 
	 */
	public Graph createGraphFromKarte() {
		Graph g = new Graph(this.min, this.max, this.ways);

		Graph.setGraph(g);
		return g;
	}
}