package Kartendarstellung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent.COMMAND;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.tilesources.*;

import fahrzeugverwaltung.Fahrzeug;
import fahrzeugverwaltung.FahrzeugVerwaltung;

import Graph.Graph;
import Graph.Kante;
import Graph.Knoten;
import Graph.Routenabschnitt;

@SuppressWarnings("serial")
public class SEPSlippyMap extends JMapViewer {
	
	/* DEBUGGING SCHALTER */
	@SuppressWarnings("unused")
	private static boolean DebugStrassenKanten = false;
	boolean offline = false;	/* Offline Modus für Jonathans USB-Stick */
	
	
	Graph graph; /* Der Graph, den die Karte immer erneut bekommt */
	public static float mousePointerX; /* Zur Übergabe vom MouseListener an das GUI Knotenanklick-Kontextmenü */
	public static float mousePointerY;
	
	private static Layer invislayer = new Layer("unqualifizierter_name");
	
	private boolean routeGezeichnet = false;
	
	private int anzeigeFilter_fahrzeugnr = 0; /* Fahrzeug-Id */
	private int anzeigeFilter_muellart = -1; /* 0: Alle, 1-4: Bio Papier Plastik Rest */
	private int anzeigeFilter_wochentag = 0; /* 0: Alle, 1-5 Mo-Fr */
	
	private int MapMakerMinZoom = 15; /*
									 * Mapmarker sind unter diesem Zoomwert
									 * unsichtbar
									 */

	//Baustellen Indikator für die GUI
	public static boolean baustelleGefunden = false;
	
	
	/* Stile für die Routen */
	Style stil_bio = new Style(new Color(0,255,0,150), new BasicStroke(4));
	Style stil_papier = new Style(new Color(0,0,255,150), new BasicStroke(4));
	Style stil_plastik = new Style(new Color(240,202,0,150), new BasicStroke(4));
	Style stil_rest = new Style(new Color(51,0,51,150), new BasicStroke(4));
	
    float strokeThickness = 3.0f;
    float miterLimit = 15f;
    float[] dashPattern = {3f, 15f};
    float dashPhase = 0f;
    BasicStroke stroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            miterLimit, dashPattern, dashPhase);
	
	Style stil_bio_inaktiv = new Style(new Color(255,0,0,150), stroke);
	Style stil_papier_inaktiv = new Style(new Color(255,0,0,150), stroke);
	Style stil_plastik_inaktiv = new Style(new Color(255,0,0,150), stroke);
	Style stil_rest_inaktiv = new Style(new Color(255,0,0,150), stroke);
	
	
	/* Mögliche Werte:
	 * 
	 * Fahrzeuge 	FahrzeugId
	 * Wochentage	Alle, 1-5 Mo-Fr
	 * Muellart		Alle, 1-4 Bio Papier Plastik Rest
	 */
	
	
	public SEPSlippyMap(){
		System.out.println("Karte initialisiert.");
		
		
		this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();

				mousePointerX = (float) getPosition(p).getLat();
        		mousePointerY = (float) getPosition(p).getLon();
        		
            }
        });
	}
	
	
	public synchronized void setGraph(Graph graph) { /* Dient quasi als Konstruktor */
		if(offline)
			this.setTileSource(new OfflineOsmTileSource("file:D:/Tiles",1,17));
		
//		this.setTileSource(new BingAerialTileSource());
//		this.setTileSource(new MapQuestOsmTileSource());
		
		
		/* Aufräumen */
		this.graph=null;
		this.removeAllMapMarkers();
		this.removeAllMapPolygons();
		this.removeAllMapRectangles();
		invislayer.setVisible(false);
		
		
		/* Neu erstellen */
		this.graph = graph;
		this.drawKartenbegrenzung();
		this.drawStrassenKnoten();
		this.drawSpezialKnoten();
		this.drawSelectedFahrzeuge();
		this.drawRoadClosed();
		this.manuAenderungAusfuehren(anzeigeFilter_fahrzeugnr, anzeigeFilter_wochentag);
	}
	
	/**
	 * Zum Aktualisieren der Darstellung. Setzt alle Kartenelemente neu.
	 */
	public synchronized void aktualisiereDarstellung(){
		this.setGraph(Graph.getGraph());
		System.err.println("!! KARTENDARSTELLUNG AKTUALISIERT (alle Kartenelemente wurden neu gezeichnet) !!");
	}
	
	/**
	 * Zeichnet den blauen Kartenrahmen
	 */
	private synchronized void drawKartenbegrenzung() {
		/* Zeichnet den Umriss des geladenen Kartenausschnitts auf die Karte */

		Point2D.Float min = graph.getMin();
		Point2D.Float max = graph.getMax();

		this.addMapRectangle(new MapRectangleImpl(new Coordinate(max.y, min.x),
				new Coordinate(min.y, max.x)));
	}

	/**
	 * Zeichnet nur die Strassenknoten
	 */
	private synchronized void drawStrassenKnoten() {
		for (Knoten node : graph.getKnoten()) { /* KNOTENERSTELLUNG */
			for (int i = 0; i < node.getKanten().size(); i++) {
				this.addMapMarker(new MapMarkerDot(Color.red, node.getLat(),
						node.getLon()));
			}
		}

		if (zoom < MapMakerMinZoom)
			this.setMapMarkerVisible(false);
	}
	
	/**
	 * Selektiert entsprechende Fahrzeuge je nach gesetzten Filtern.
	 */
	private synchronized void  drawSelectedFahrzeuge(){
		Vector<Fahrzeug> verfuegbareFahrzeuge = FahrzeugVerwaltung.getInstance().getListeVerf(); 
		verfuegbareFahrzeuge.trimToSize();
		
		LinkedList<Routenabschnitt> ausgabe = new LinkedList<Routenabschnitt>();
		
		LinkedList<Fahrzeug> loeschListe = new LinkedList<Fahrzeug>();
		
		for (int i = 0; i < verfuegbareFahrzeuge.size(); i++){
			if(anzeigeFilter_fahrzeugnr != 0 && verfuegbareFahrzeuge.get(i).getNr() != anzeigeFilter_fahrzeugnr){
				loeschListe.add(verfuegbareFahrzeuge.get(i));
			}
		}
		if (loeschListe.size()>0){
			verfuegbareFahrzeuge.removeAll(loeschListe);
			verfuegbareFahrzeuge.trimToSize();
			loeschListe.clear();
		}
		
		
		
		for (int i = 0; i < verfuegbareFahrzeuge.size(); i++){
			if(anzeigeFilter_muellart != 0 && verfuegbareFahrzeuge.get(i).getMuellArtNr() != anzeigeFilter_muellart)
				loeschListe.add(verfuegbareFahrzeuge.get(i));
		}
		if (loeschListe.size()>0){
			verfuegbareFahrzeuge.removeAll(loeschListe);
			verfuegbareFahrzeuge.trimToSize();
			loeschListe.clear();
		}
		
		
		
		
		for (int fahrzeugindex = 0; fahrzeugindex < verfuegbareFahrzeuge.size(); fahrzeugindex++){
			if(anzeigeFilter_wochentag == 0){
				/* Alles adden */
				for (int tagindex = 1; tagindex <= 5; tagindex++){
					if(verfuegbareFahrzeuge.get(fahrzeugindex).getRouteTag(tagindex-1) != null){
						LinkedList<Routenabschnitt> routenProTag = verfuegbareFahrzeuge.get(fahrzeugindex).getRouteTag(tagindex-1);
						
						for (Routenabschnitt r : routenProTag){
							Style stil = null;
							String stilname = "";
							
							switch(verfuegbareFahrzeuge.get(fahrzeugindex).getMuellArtNr()){
							case 0:
								System.err.println("Fahrzeug "+verfuegbareFahrzeuge.get(fahrzeugindex).getNr()+" ist 0 im Switch");
								break;
							case 1: /* Plastik */
								stil = stil_plastik;
								stilname = "plastik";
								break;
							case 2: /* Rest */
								stil = stil_rest;
								stilname = "rest";
								break;
							case 3: /* Papier */
								stil = stil_papier;
								stilname = "papier";
								break;
							case 4: /* Bio */
								stil = stil_bio;
								stilname = "bio";
								break;
							default:
								System.err.println("Fahrzeug "+verfuegbareFahrzeuge.get(fahrzeugindex).getNr()+" ist default im Switch");
								break;
							}
							
							r.setStil(stil);
							r.setStilname(stilname);
						}
					
						ausgabe.addAll(routenProTag);
					}
				}
			} else {
				/* Nur einen Wochentag zeichnen */
				for (int tagindex = 1; tagindex < 6; tagindex++){
					if(anzeigeFilter_wochentag == tagindex){
						if(verfuegbareFahrzeuge.get(fahrzeugindex).getRouteTag(tagindex-1) != null){
							LinkedList<Routenabschnitt> routenProTag = verfuegbareFahrzeuge.get(fahrzeugindex).getRouteTag(tagindex-1);
							
							for (Routenabschnitt r : routenProTag){
								Style stil = null;
								String stilname = "";
								
								switch(verfuegbareFahrzeuge.get(fahrzeugindex).getMuellArtNr()){
								case 0:
									System.err.println("Fahrzeug "+verfuegbareFahrzeuge.get(fahrzeugindex).getNr()+" ist 0 im Switch");
									break;
								case 1: /* Plastik */
									stil = stil_plastik;
									stilname = "plastik";
									break;
								case 2: /* Rest */
									stil = stil_rest;
									stilname = "rest";
									break;
								case 3: /* Papier */
									stil = stil_papier;
									stilname = "papier";
									break;
								case 4: /* Bio */
									stil = stil_bio;
									stilname = "bio";
									break;
								default:
									System.err.println("Fahrzeug "+verfuegbareFahrzeuge.get(fahrzeugindex).getNr()+" ist default im Switch");
									break;
								}
								
								r.setStil(stil);
								r.setStilname(stilname);
							}
						
							ausgabe.addAll(routenProTag);
					}
				
				}
				}
			}
			}
		
		
		/* INAKTIV SETZEN */
		for (Routenabschnitt r : ausgabe){
			if(r.isAktiv() == false){
				if (r.getStilname().equals("plastik")){
					r.setStil(stil_plastik_inaktiv);
				} else if (r.getStilname().equals("rest")){
					r.setStil(stil_rest_inaktiv);
				} else if (r.getStilname().equals("papier")){
					r.setStil(stil_papier_inaktiv);
				} else if (r.getStilname().equals("bio")){
					r.setStil(stil_bio_inaktiv);
				}
				
				
				
			}
		}
		
		
		if (ausgabe.size() > 25)
			this.setRouteGezeichnet(true);
		else 
			this.setRouteGezeichnet(false);
		
		/* OUTPUT */
		this.drawRoute(ausgabe);
		this.manuAenderungAusfuehren(anzeigeFilter_fahrzeugnr, anzeigeFilter_wochentag);
		}
	
	
	
	/**
	 * Zeichnet pauschal alle Routenabschnitte auf die Map.
	 */
	private synchronized void drawRoute(LinkedList<Routenabschnitt> routenabschnitte) {
		if(routenabschnitte != null){
		for (Routenabschnitt r : routenabschnitte){
			Coordinate coordFrom = new Coordinate(r.getFrom().getLat(),r.getFrom().getLon());
			Coordinate coordTo = new Coordinate(r.getTo().getLat(),r.getTo().getLon());
			List<Coordinate> aktroute_coordlist = new ArrayList<Coordinate>(Arrays.asList(coordFrom, coordTo, coordTo));
			Style routenstil = r.getStil();
			
			
	
			MapPolygon plastikPoly = new MapPolygonImpl(null, "", aktroute_coordlist, routenstil);
			this.addMapPolygon(plastikPoly);
		}
		}
	}
	
	private synchronized void drawRoadClosed() {
		Style stil_roadclosed = new Style(new Color(255,0,0,255), new BasicStroke(4));
		
		List<Kante> kantenliste = graph.getAlleKanten();
		baustelleGefunden = false;
		
		for(int i = 0; i < kantenliste.size(); i++){
			if (kantenliste.get(i).getGesperrt()){
				Coordinate coordFrom = new Coordinate(kantenliste.get(i).getFrom().getLat(),kantenliste.get(i).getFrom().getLon());
				Coordinate coordTo = new Coordinate(kantenliste.get(i).getTo().getLat(),kantenliste.get(i).getTo().getLon());
				
				List<Coordinate> aktstrassenzug_coordlist = new ArrayList<Coordinate>(Arrays.asList(coordFrom, coordTo, coordTo));
				MapMarkerCircle sperreFrom = new MapMarkerCircle(null, "ROAD_CLOSED", coordFrom, .00002);
				sperreFrom.setColor(Color.red);
				sperreFrom.setBackColor(Color.red);
				MapMarkerCircle sperreTo = new MapMarkerCircle(null, "ROAD_CLOSED", coordTo, .00002);
				sperreTo.setBackColor(Color.red);
				sperreTo.setColor(Color.red);
				this.addMapMarker(sperreFrom);
				this.addMapMarker(sperreTo);
				
				MapPolygon aktPoly = new MapPolygonImpl(null, "", aktstrassenzug_coordlist, stil_roadclosed);
				this.addMapPolygon(aktPoly);
				
				baustelleGefunden = true;
				
				System.err.println("[KARTENDARSTELLUNG] ROAD_CLOSED gefunden! FROM: "+aktstrassenzug_coordlist.get(0)+" TO: "+aktstrassenzug_coordlist.get(1));
			}
		}
	}
	
	/**
	 * Zeichnet alle StrassenKanten!
	 */
	@SuppressWarnings("unused")
	private synchronized void drawStrassenKanten() {
		List<Kante> kantenliste = graph.getAlleKanten();
		
		for (int i = 0; i < kantenliste.size(); i++){
			Coordinate coordFrom = new Coordinate(kantenliste.get(i).getFrom().getLat(),kantenliste.get(i).getFrom().getLon());
			Coordinate coordTo = new Coordinate(kantenliste.get(i).getTo().getLat(),kantenliste.get(i).getTo().getLon());
			
			List<Coordinate> aktstrassenzug_coordlist = new ArrayList<Coordinate>(Arrays.asList(coordFrom, coordTo, coordTo));
			
			
			// public Style(Color color, Color backColor, Stroke stroke, Font font)
			Style stil = new Style(new Color(255,0,128,150), new BasicStroke(4));
			
			
			
			// public MapPolygonImpl(Layer layer, String name, List<? extends ICoordinate> points, Style style)
			MapPolygon aktPoly = new MapPolygonImpl(null, "", aktstrassenzug_coordlist, stil);
			
			this.addMapPolygon(aktPoly);
			System.out.println("[KARTENDARSTELLUNG]: Strasse eingefaerbt   FROM: "+coordFrom.getLat()+","+coordFrom.getLon()+"  TO: "+coordTo.getLat()+","+coordTo.getLon()+"    ID="+kantenliste.get(i).id);
			
			
			if (zoom < MapMakerMinZoom)
				this.setMapPolygonsVisible(false);
			else
				this.setMapPolygonsVisible(true);
		}
		
		System.err.println("[KARTENDARSTELLUNG]: Es wurden alle Strassen in pink eingefaerbt. Zum deaktivieren: SEPSlippyMap.DebugStrassenKanten = FALSE (Zeile 43)!  *jl");
		
	}
	
	
	/** 
	 * Fuegt Markierungen fuer z.B. das Depot, Entleerungsorte etc. hinzu.
	 */
	private synchronized void drawSpezialKnoten() {
		if(graph.getDepot() != null){
			MapMarkerCircle fahrzeugdepot = new MapMarkerCircle(null, "Fahrzeugdepot", new Coordinate(graph.getDepot().getLat(), graph.getDepot().getLon()), .00005);
			
			this.addMapMarker(fahrzeugdepot);
			System.out.println("Wegmarke (Depot) hinzugefuegt.");
		}
		
		if(graph.getPlastik() != null){
			MapMarkerCircle entleerung_plastik = new MapMarkerCircle(null, "Entleerungsort: Plastik", new Coordinate(graph.getPlastik().getLat(), graph.getPlastik().getLon()), .00005);
			entleerung_plastik.setBackColor(Color.yellow);
			
			this.addMapMarker(entleerung_plastik);
			System.out.println("Wegmarke (Plastik) hinzugefuegt.");
		}
		
		if(graph.getPapier() != null){
			MapMarkerCircle entleerung_papier = new MapMarkerCircle(null, "Entleerungsort: Papier", new Coordinate(graph.getPapier().getLat(), graph.getPapier().getLon()), .00005);
			entleerung_papier.setBackColor(Color.blue);
			
			this.addMapMarker(entleerung_papier);
			System.out.println("Wegmarke (Papier) hinzugefuegt.");
		}
		
		if(graph.getRest() != null){
			MapMarkerCircle entleerung_rest = new MapMarkerCircle(null, "Entleerungsort: Rest", new Coordinate(graph.getRest().getLat(), graph.getRest().getLon()), .00005);
			entleerung_rest.setBackColor(Color.black);
			
			this.addMapMarker(entleerung_rest);
			System.out.println("Wegmarke (Restmuell) hinzugefuegt.");
		}
		
		if(graph.getBio() != null){
			MapMarkerCircle entleerung_bio = new MapMarkerCircle(null, "Entleerungsort: Bio", new Coordinate(graph.getBio().getLat(), graph.getBio().getLon()), .00005);
			entleerung_bio.setBackColor(Color.green);
			
			this.addMapMarker(entleerung_bio);
			System.out.println("Wegmarke (Bio) hinzugefuegt.");
		}		
		
	}
		
		
	public synchronized void setZoom(int zoom, Point mapPoint) { /*
													 * Macht gleichzeitig einen
													 * Redraw
													 */
		if (zoom > tileController.getTileSource().getMaxZoom()
				|| zoom < tileController.getTileSource().getMinZoom()
				|| zoom == this.zoom)
			return;

		if (zoom < MapMakerMinZoom){
			this.setMapMarkerVisible(false);
			this.setMapPolygonsVisible(false);
		} else {
			this.setMapMarkerVisible(true);
			this.setMapPolygonsVisible(true);
		}

		Coordinate zoomPos = getPosition(mapPoint);
		tileController.cancelOutstandingJobs(); // Clearing outstanding load
		// requests
		setDisplayPositionByLatLon(mapPoint, zoomPos.getLat(),
				zoomPos.getLon(), zoom);

		this.fireJMVEvent(new JMVCommandEvent(COMMAND.ZOOM, this));
	}
	
	


	public int getAnzeigeFilter_muellart() {
		return anzeigeFilter_muellart;
	}


	public synchronized void setAnzeigeFilter_muellart(int anzeigeFilter_muellart) {
		this.anzeigeFilter_muellart = anzeigeFilter_muellart;
		this.aktualisiereDarstellung();
	}


	public int getAnzeigeFilter_fahrzeugnr() {
		return anzeigeFilter_fahrzeugnr;
	}


	public synchronized void setAnzeigeFilter_fahrzeugnr(int anzeigeFilter_fahrzeugnr) {
		this.anzeigeFilter_fahrzeugnr = anzeigeFilter_fahrzeugnr;
		this.aktualisiereDarstellung();
	}


	public int getAnzeigeFilter_wochentag() {
		return anzeigeFilter_wochentag;
	}


	public synchronized void setAnzeigeFilter_wochentag(int anzeigeFilter_wochentag) {
		this.anzeigeFilter_wochentag = anzeigeFilter_wochentag;
		this.aktualisiereDarstellung();
	}


	/**
	 * @return the routeGezeichnet
	 */
	public boolean isRouteGezeichnet() {
		return routeGezeichnet;
	}


	/**
	 * @param routeGezeichnet the routeGezeichnet to set
	 */
	public void setRouteGezeichnet(boolean routeGezeichnet) {
		this.routeGezeichnet = routeGezeichnet;
	}
	
	
	public void setBetriebsfahrt(int fahrzeugnummer){
		Kante k = graph.getClosestKante(mousePointerX, mousePointerY);
		
		Coordinate coordFrom = new Coordinate(k.getFrom().getLat(),k.getFrom().getLon());
		Coordinate coordTo = new Coordinate(k.getTo().getLat(),k.getTo().getLon());
		List<Coordinate> aktroute_coordlist = new ArrayList<Coordinate>(Arrays.asList(coordFrom, coordTo, coordTo));
		
		Style routenstil = null;
		
		switch(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).getMuellArtNr()){
		case 1:
			routenstil = stil_plastik_inaktiv;
			break;
		case 2:
			routenstil = stil_rest_inaktiv;
			break;
		case 3:
			routenstil = stil_papier_inaktiv;
			break;
		case 4:
			routenstil = stil_bio_inaktiv;
			break;
		}
		MapPolygon poly = (new MapPolygonImpl(null, "", aktroute_coordlist, routenstil));
		FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAdded.add(poly);
		FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAddedWochentagMapping.add((Integer) anzeigeFilter_wochentag);
		this.manuAenderungAusfuehren(fahrzeugnummer, anzeigeFilter_wochentag);
	}
	
	public void setFahrt(int fahrzeugnummer){
		Kante k = graph.getClosestKante(mousePointerX, mousePointerY);
		
		Coordinate coordFrom = new Coordinate(k.getFrom().getLat(),k.getFrom().getLon());
		Coordinate coordTo = new Coordinate(k.getTo().getLat(),k.getTo().getLon());
		List<Coordinate> aktroute_coordlist = new ArrayList<Coordinate>(Arrays.asList(coordFrom, coordTo, coordTo));
		
		Style routenstil = null;
		
		switch(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).getMuellArtNr()){
		case 1:
			routenstil = stil_plastik;
			break;
		case 2:
			routenstil = stil_rest;
			break;
		case 3:
			routenstil = stil_papier;
			break;
		case 4:
			routenstil = stil_bio;
			break;
		}
		MapPolygon poly = (new MapPolygonImpl(null, "", aktroute_coordlist, routenstil));
		FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAdded.add(poly);
		FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAddedWochentagMapping.add((Integer) anzeigeFilter_wochentag);
		this.manuAenderungAusfuehren(fahrzeugnummer, anzeigeFilter_wochentag);		
	}
	
	
	public void entfaerbeKante(int fahrzeugnummer){
		Kante k = graph.getClosestKante(mousePointerX, mousePointerY);
		
		Coordinate k1 = new Coordinate(k.getFrom().getLat(),k.getFrom().getLon());
		Coordinate k2 = new Coordinate(k.getTo().getLat(),k.getTo().getLon());
		
		for (int i = this.mapPolygonList.size()-1; i >= 0; i--){ /* Längstes IF von die Welt */
			if(mapPolygonList.get(i).getPoints().get(0).getLat() == k1.getLat() 
			&& mapPolygonList.get(i).getPoints().get(0).getLon() == k1.getLon() &&
			   mapPolygonList.get(i).getPoints().get(1).getLat() == k2.getLat() 
			&& mapPolygonList.get(i).getPoints().get(1).getLon() == k2.getLon() ||
			   mapPolygonList.get(i).getPoints().get(1).getLat() == k1.getLat() 
			&& mapPolygonList.get(i).getPoints().get(1).getLon() == k1.getLon() &&
			   mapPolygonList.get(i).getPoints().get(0).getLat() == k2.getLat() 
			&& mapPolygonList.get(i).getPoints().get(0).getLon() == k2.getLon()){
				FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeleted.add(k);
				FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeletedWochentagMapping.add(anzeigeFilter_wochentag);

				this.manuAenderungAusfuehren(fahrzeugnummer, anzeigeFilter_wochentag);
				break;
			}
		}
	}
	

	
	public void manuAenderungAusfuehren(int fahrzeugnummer, int wochentag){
		this.repaint();
		if(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer) == null)
			return;
		
		
		if(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).getMuellArtNr() != anzeigeFilter_muellart)
			return;
		
		for (MapPolygon pl : mapPolygonList){
			invislayer.setVisible(true);
			pl.setLayer(null);
			invislayer.setVisible(false);
		}
		
		this.repaint();
		
//		if (anzeigeFilter_fahrzeugnr == 0) {
//			
//			// ---------------------------------------------------------------------------------------------------------
//		for (Fahrzeug f : FahrzeugVerwaltung.getInstance().getListeArtVerf(GUI.mainWindow.aktMuellart))
//			if (wochentag == 0){
//				for (MapPolygon p : f.manuellAdded){
//					if(this.mapPolygonList.contains(p) != true)
//						this.addMapPolygon(p);
//				}
//				
//				
//				/* HIERNACH LÖSCHEN */
//				
//				for (int i = 0; i < f.manuellDeleted.size(); i++){
//					this.entfaerbeByKante(f.manuellDeleted.get(i));
//					
//				/* LÖSCHEN ENDE */
//				}
//																	} else { // ELSE ELSE ELSE WOCHENTAG != ZERO
//					for (int i = 0; i < f.manuellAdded.size(); i++){
//						if(this.mapPolygonList.contains(f.manuellAdded.get(i)) != true)
//							if(f.manuellAddedWochentagMapping.get(i) == anzeigeFilter_wochentag)
//								this.addMapPolygon(f.manuellAdded.get(i));
//					}
//					
//					
//					/* Hiernach: LÖSCHEN */
//					
//					for (int i = 0; i < f.manuellDeleted.size(); i++){
//						if(f.manuellDeletedWochentagMapping.get(i) == anzeigeFilter_wochentag){
//							this.entfaerbeByKante(f.manuellDeleted.get(i));
//						}
//								// ACTION
//					}
//				
//					/* LÖSCHEN ENDE */
//				}
//			
//			
//			
//			
//			
//			// ------------------------------------------------------
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//		} else {
		if (wochentag == 0){
		for (MapPolygon p : FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAdded){
			if(this.mapPolygonList.contains(p) != true)
				this.addMapPolygon(p);
		}
		
		
		/* HIERNACH LÖSCHEN */
		
		for (int i = 0; i < FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeleted.size(); i++){
			this.entfaerbeByKante(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeleted.get(i));
			
		/* LÖSCHEN ENDE */
		}
															} else { // ELSE ELSE ELSE WOCHENTAG != ZERO
			for (int i = 0; i < FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAdded.size(); i++){
				if(this.mapPolygonList.contains(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAdded.get(i)) != true)
					if(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAddedWochentagMapping.get(i) == anzeigeFilter_wochentag)
						this.addMapPolygon(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellAdded.get(i));
			}
			
			
			/* Hiernach: LÖSCHEN */
			
			for (int i = 0; i < FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeleted.size(); i++){
				if(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeletedWochentagMapping.get(i) == anzeigeFilter_wochentag){
					this.entfaerbeByKante(FahrzeugVerwaltung.getInstance().getFahrzeugByNr(fahrzeugnummer).manuellDeleted.get(i));
				}
						// ACTION
			}
		
			/* LÖSCHEN ENDE */
		}
//	}
		this.repaint();
	}
	
	public void entfaerbeByKante(Kante k){
		Coordinate k1 = new Coordinate(k.getFrom().getLat(),k.getFrom().getLon());
		Coordinate k2 = new Coordinate(k.getTo().getLat(),k.getTo().getLon());
		
		for (int i = 0; i < this.mapPolygonList.size(); i++){ /* Längstes IF von die Welt */
//		for (int i = this.mapPolygonList.size()-1; i >= 0; i--){
			if(mapPolygonList.get(i).getPoints().get(0).getLat() == k1.getLat() 
			&& mapPolygonList.get(i).getPoints().get(0).getLon() == k1.getLon() &&
			   mapPolygonList.get(i).getPoints().get(1).getLat() == k2.getLat() 
			&& mapPolygonList.get(i).getPoints().get(1).getLon() == k2.getLon() ||
			   mapPolygonList.get(i).getPoints().get(1).getLat() == k1.getLat() 
			&& mapPolygonList.get(i).getPoints().get(1).getLon() == k1.getLon() &&
			   mapPolygonList.get(i).getPoints().get(0).getLat() == k2.getLat() 
			&& mapPolygonList.get(i).getPoints().get(0).getLon() == k2.getLon()){

				
				if(mapPolygonList.get(i).getLayer() != null && mapPolygonList.get(i).getLayer().equals(invislayer))
					continue;
				
				mapPolygonList.get(i).setLayer(invislayer);
				this.repaint();
				break;
			}
		}
	}
}

