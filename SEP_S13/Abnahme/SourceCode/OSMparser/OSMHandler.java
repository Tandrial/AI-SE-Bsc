package OSMparser;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Graph.Knoten;

class OSMHandler extends DefaultHandler {

	// Liste mit Straßentypen die geparst werden
	private final static List<String> streetTypToParse = Arrays
			.asList(new String[] { "primary", "primary_link", "secondary",
					"secondary_link", "tertiary", "tertiary_link",
					"living_street", "residential", "road" });

	private Knoten node;
	private Way way;

	private List<Knoten> knoten = new ArrayList<Knoten>();
	private List<Way> ways = new ArrayList<Way>();

	private Point2D.Float min;
	private Point2D.Float max;

	public OSMHandler(List<Knoten> knoten, List<Way> ways) {
		this.knoten = knoten;
		this.ways = ways;
	}

	public Point2D.Float getMin() {
		return min;
	}

	public Point2D.Float getMax() {
		return max;
	}

	// Was soll passieren wenn er einen <TEXT> - Tag findet
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		if (localName.equals("bounds")) {
			setBounds(atts.getValue("minlat"), atts.getValue("maxlat"),
					atts.getValue("minlon"), atts.getValue("maxlon"));
		}
		if (localName.equals("node")) {
			node = new Knoten(atts.getValue("id"), "");
			node.setLat(atts.getValue("lat"));
			node.setLon(atts.getValue("lon"));
			node.setLocation(node.getLat(), node.getLon());

			knoten.add(node);
		}
		if (localName.equals("way")) {
			way = new Way(atts.getValue("id"));
		}
		if (localName.equals("nd")) {
			way.add(atts.getValue("ref"));
		}
		if (localName.equals("tag")) {
			if (way != null) {
				if (atts.getValue("k").equals("highway")) {
					way.setK(atts.getValue("k"));
					way.setV(atts.getValue("v"));
				}
				if (atts.getValue("k").equals("name")) {
					way.setStrassenName(atts.getValue("v"));
				}
				if (atts.getValue("k").equals("road_closed")) {
					way.setGesperrt(true);
					if (way.getV().equals("")) {
						way.setV("gesperrt");
						way.setK("highway");
					}
				}
			}
		}
	}

	// Was soll passieren wenn er einen </TEXT> - Tag findet
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("way") && way != null) {
			String v = way.getV();

			if (way.getK().equals("highway") && v != null) {

				if (allgemein.Konstanten.HAUPTSTRASSENTYPEN.contains(v)
						|| allgemein.Konstanten.NEBENSTRASSENTYPEN.contains(v)
						|| allgemein.Konstanten.NULLMUELLSTRASSE.contains(v)) {
					ways.add(way);
				}
				way = null;
			}
		}
	}

	private void setBounds(String minLat, String maxLat, String minLon,
			String maxLon) {
		this.min = new Point2D.Float(Float.parseFloat(minLon),
				Float.parseFloat(minLat));
		this.max = new Point2D.Float(Float.parseFloat(maxLon),
				Float.parseFloat(maxLat));
	}
}