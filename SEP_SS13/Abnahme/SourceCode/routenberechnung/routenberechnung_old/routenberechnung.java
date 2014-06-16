package routenberechnung;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import Graph.Graph;
import Graph.Kante;
import Graph.Knoten;
import Graph.Routenabschnitt;
import Hindernisberechnung.hindernisberechnung;
import allgemein.Konstanten;
import fahrzeugverwaltung.Fahrzeug;
import fahrzeugverwaltung.FahrzeugVerwaltung;

public class routenberechnung {

	/**
	 * Teilt allen verfuegbaren Fahrzeugen aller Muellarten ihre Routen zu.
	 */
	public static void calculateroute() {
		Graph graph = Graph.getGraph();
		
		// Ist die Berechnung ueberhaupt moeglich ?
		if (checkpossibility()) { 
			
			// Muellemnege auf Standard setzen
			Graph.getGraph().resetGraph();
			
			// erreichbare Kanten / Knoten ermitteln
			hindernisberechnung.erreichbarkeitpruefen();
			System.err.println("GRAPH.GETERREICHBARKEITSPRUEFUNG() AUSGEFUEHRT");
			
			// "optimaler" Weg, der alle Kanten mitnimmt	 
			ArrayList<Kante> cppRoute = CPPv2.getPath(graph.getErreichbareKanten(),graph.getDepot());
			
			
			//Aufruf der Routenberechnung fuer alle Muellarten
			calculateroute(FahrzeugVerwaltung.getInstance().getListeArtVerf(allgemein.Konstanten.MUELLART_BIO), cppRoute);
			calculateroute(FahrzeugVerwaltung.getInstance().getListeArtVerf(allgemein.Konstanten.MUELLART_PAPIER), cppRoute);
			calculateroute(FahrzeugVerwaltung.getInstance().getListeArtVerf(allgemein.Konstanten.MUELLART_PLASTIK), cppRoute);
			calculateroute(FahrzeugVerwaltung.getInstance().getListeArtVerf(allgemein.Konstanten.MUELLART_REST), cppRoute);
			
		} else
			JOptionPane.showMessageDialog(null, "Berechnung wegen kritischem Fehler abgebrochen!", "Berechnung abgebrochen", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Weist allen verfuegbaren Fahrzeugen (einer Muellart) die zu fahrenden Routen zu.
	 * @param fahrzeuge - Die Fahrzeuge, die auf die Route aufgeteilt werden sollen.
	 * @param cppRoute - Die Route durch den Graphen, auf die die fahrzuege aufgeteilt werden.
	 */
	public static void calculateroute(ArrayList<Fahrzeug> fahrzeuge, ArrayList<Kante> cppRoute) {
		int count = 0;  // Position in der Liste, bis zu welcher geleert wurde
		
		// Plane fuer alle 5 Arbeitstage
		for (int tag = 0; tag < 5; tag++) {
			
			// Plane fuer jedes Fahrzeug
			for (int i = 0; i < fahrzeuge.size(); i++) {
				Fahrzeug aktFahrzeug = fahrzeuge.get(i);
				LinkedList<Routenabschnitt> route = new LinkedList<Routenabschnitt>(); // Jedes Fahrzeug bekommt seine eigene Route
				
				goToWork(cppRoute.get(count).getTo(), route, aktFahrzeug); // Fahre zur Arbeit
				
				// Solange das taegliche Max. nicht erreicht ist
                while (aktFahrzeug.getTagLadung() < aktFahrzeug.getDailyCap() && count < cppRoute.size()) {
                	
					// Wenn die Kapazitaet noch nicht erreicht ist
					if (aktFahrzeug.getAktLadung() + cppRoute.get(count).getMuellMengeArt(aktFahrzeug.getMuellArt()) <= aktFahrzeug.getCap()) {
						sammleMuell(aktFahrzeug, cppRoute.get(count), route); // Sammle Muell von diesem Abschnitt ein
						count++; // naechster Abschnitt
					} else
						// Fahre zur Deponie
						besucheDeponie(cppRoute.get(count).getFrom(), route, aktFahrzeug); 
				}
                
                count--;
                
				goHome(cppRoute.get(count).getTo(), route, aktFahrzeug); // Fahre zum Depot
				
				aktFahrzeug.setRouteTag(route, tag); // Speichere die Route beim Fahrzeug			
			}	
		}		
	}
	
	/**
	 * Sammelt mit einem Fahrzeug an einer Kante Muell ein. D.h. erhoeht die Muellmenge des Fahrzeugs um die, 
	 * auf der Kante liegende Muellmenge und setzt diese, fuer die entsprechende Muellart, danach auf 0. 
	 * @param f - Das Fahrzeug, das den Muell aufnehmen soll.
	 * @param k - Die Kante / Straße, auf der der Muell augesammelt wird.
	 * @param r - Die bisherige Route des Fahrzeugs (f).
	 */
	public static void sammleMuell(Fahrzeug f, Kante k, LinkedList<Routenabschnitt> r) {
		
		// wenn noch Muell an dem Abschnitt liegt
		if (k.getMuellMengeArt(f.getMuellArt()) > 0) {
			
		    // Fahrzeug sammelt Muell ein (aktuelle Ladung erhoeht sich)
		    f.setAktLadung(f.getAktLadung() + k.getMuellMengeArt(f.getMuellArt())); 
		    
		    // Fahrzeug sammelt Muell ein (taegliche Ladung erhoeht sich)
		    f.setTagLadung(f.getTagLadung() + k.getMuellMengeArt(f.getMuellArt())); 
		    
		    // Kante hat keinen Muell mehr
		    k.setMuellMengeArt(0, f.getMuellArt()); 
		    
		    // Kante hinzufuegen als "gesammelt"
		    r.add(new Routenabschnitt(k.getFrom(), k.getTo(), true));
		    
		// wenn kein Muell mehr an dem Abschnitt liegt    
		} else
			
			// Kante hinzufuegen als "nicht gesammelt"
			r.add(new Routenabschnitt(k.getFrom(), k.getTo(), false));			
	}
	
	/**
	 * Fuegt der bisherigen Route eines Fahrzeugs den Weg zu der, fuer seine Muellart zustaendige, Deponie und zurueck hinzu.
	 * @param aktKnoten - Position des Fahrzeugs vor und nach der Fahrt zur Deponie.
	 * @param route - Die bisherige Route des Fahrzeugs (f).
	 * @param f - Das Fahrzeug, das zur Deponie fahren soll.
	 */
	public static void besucheDeponie(Knoten aktKnoten, LinkedList<Routenabschnitt> route, Fahrzeug f) {
		String aktMuellArt = f.getMuellArt();
		Graph graph = Graph.getGraph();
		Dijkstra pathfinder = new Dijkstra(graph); // Zur Berechnung des Weges zur Deponie

		f.setAktLadung(0); // Wagen "entleeren"
		
		// Wir suchen die entsprechende Deponie
		pathfinder.execute(aktKnoten);
		LinkedList<Kante> temp = new LinkedList<Kante>();
		if (aktMuellArt.equals(Konstanten.MUELLART_PLASTIK))
			temp = Dijkstra.getKantenPath(pathfinder.getPath(graph.getPlastik()));
		if (aktMuellArt.equals(Konstanten.MUELLART_REST))
			temp = Dijkstra.getKantenPath(pathfinder.getPath(graph.getRest()));
		if (aktMuellArt.equals(Konstanten.MUELLART_PAPIER))
			temp = Dijkstra.getKantenPath(pathfinder.getPath(graph.getPapier()));
		if (aktMuellArt.equals(Konstanten.MUELLART_BIO))
			temp = Dijkstra.getKantenPath(pathfinder.getPath(graph.getBio()));

		// Fahre zur entsprechenden Muelldeponie, sammle dabei keinen Muell ein
		for (Kante k : temp) 
			route.add(new Routenabschnitt(k.getFrom(), k.getTo(), false)); // Kante hinzufuegen als "nicht gesammelt"
	}
	
	/**
	 * Fuegt der bisherigen Route eines Fahrzeugs den Weg von seiner aktuellen Position zum Depot hinzu.
	 * @param aktKnoten - Position des Fahrzeugs vor der Fahrt zum Depot.
	 * @param route - Die bisherige Route des Fahrzeugs (f).
	 * @param f - Das Fahrzeug, das zum Depot fahren soll.
	 */
	public static void goHome(Knoten aktKnoten, LinkedList<Routenabschnitt> route, Fahrzeug f) {
		Graph graph = Graph.getGraph();
		Dijkstra pathfinder = new Dijkstra(graph); // Zur Berechnung des Weges zur Deponie

		pathfinder.execute(aktKnoten);
		LinkedList<Kante> temp = new LinkedList<Kante>();
		temp = Dijkstra.getKantenPath(pathfinder.getPath(graph.getDepot()));

		f.setAktLadung(0); // Wagen "entleeren"	
		f.setTagLadung(0); // Wagen "entleeren"
		
		// Fahre zum Depot, sammle keinen Muell ein
		for (Kante k : temp) 
			route.add(new Routenabschnitt(k.getFrom(), k.getTo(), false)); // Kante hinzufuegen als "nicht gesammelt"
	}
	
	/**
	 * Fuegt der bisherigen Route eines Fahrzeugs den Weg vom Depot zum Anfang seiner Route hinzu.
	 * @param first - Anfang der Route des Fahrzeugs
	 * @param route - Die bisherige Route des Fahrzeugs (f).
	 * @param f - Das Fahrzeug, das vom Depot zum Anfang seiner Route fahren soll.
	 */
	public static void goToWork(Knoten first, LinkedList<Routenabschnitt> route, Fahrzeug f) {
		Graph graph = Graph.getGraph();
		Dijkstra pathfinder = new Dijkstra(graph); // Zur Berechnung des Weges zur Deponie

		pathfinder.execute(graph.getDepot());
		LinkedList<Kante> temp = new LinkedList<Kante>();
			temp = Dijkstra.getKantenPath(pathfinder.getPath(first));
	
		// Fahrzeug faehrt zum Depot sammelt auf dem Hin- und Rueckweg keinen Muell ein 
		for (Kante k : temp) 
			route.add(new Routenabschnitt(k.getFrom(), k.getTo(), false)); // Kante hinzufuegen als "nicht gesammelt"
	}

	/**
	 * prueft, ob es ueberhaupt moeglich ist mit den vorhandenen Fahrzeugen den Muell im gesamten Kartenausschnitt (bei einer Fahrtzeit von max. 8 Std. pro Fahrzeug)
	 * einzusammeln
	 * 
	 * @param graph - 
	 *            Graph, der den aktuellen Kartenausschnitt repraesentiert, in dem der Muell eingesamelt werden soll
	 * @return true, falls das Einsammeln des Muells unter den aktuellen Bedingungen vorraussichtlich moeglich ist. Sonst false.
	 */
	public static boolean checkpossibility() {
		Graph graph = Graph.getGraph();
		
		String errormessage = "";
		int[][] kapaArtTyp = new int[4][3]; // entaehlt die Summe aller Kapazitaeten [Muellart][Fahrzeugtyp]
		kapaArtTyp[0][0] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_SMALL, Konstanten.MUELLART_PLASTIK);
		kapaArtTyp[0][1] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_MEDIUM, Konstanten.MUELLART_PLASTIK);
		kapaArtTyp[0][2] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_LARGE, Konstanten.MUELLART_PLASTIK);
		kapaArtTyp[1][0] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_SMALL, Konstanten.MUELLART_REST);
		kapaArtTyp[1][1] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_MEDIUM, Konstanten.MUELLART_REST);
		kapaArtTyp[1][2] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_LARGE, Konstanten.MUELLART_REST);
		kapaArtTyp[2][0] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_SMALL, Konstanten.MUELLART_PAPIER);
		kapaArtTyp[2][1] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_MEDIUM, Konstanten.MUELLART_PAPIER);
		kapaArtTyp[2][2] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_LARGE, Konstanten.MUELLART_PAPIER);
		kapaArtTyp[3][0] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_SMALL, Konstanten.MUELLART_BIO);
		kapaArtTyp[3][1] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_MEDIUM, Konstanten.MUELLART_BIO);
		kapaArtTyp[3][2] = FahrzeugVerwaltung.getInstance().gesamtMuellMenge(Konstanten.FAHRZEUG_ART_LARGE, Konstanten.MUELLART_BIO);

		double[] kapatyp = new double[4]; // enthaelt die gesamte verfuegbare Kapazitaet je Muelltyp
		boolean[] machbar = new boolean[4]; // gibt an, ob die einzelnen Muellmengen schaffbar sind
		boolean[] depVorh = new boolean[4]; // gibt an, ob die Deponien vorhanden sind
		boolean depotVorh = false; // gibt an, ob das Fahrzeugdepot vorhanden ist
		float totmuell = graph.gettotmuell(); // Muellmenge des gesamten Graphen

		for (int i = 0; i < 4; i++) {
			kapatyp[i] += 5 * (kapaArtTyp[i][0] + kapaArtTyp[i][1] + kapaArtTyp[i][2]); 
			if (kapatyp[i] >= totmuell)
				machbar[i] = true;
		}

		if (!machbar[0])
			errormessage += "- Die Anzahl der, fuer Plastik Zustaendigen Fahrzeuge reicht vorraussichtlich nicht aus. \n";
		if (!machbar[1])
			errormessage += "- Die Anzahl der, fuer Restmuell Zustaendigen Fahrzeuge reicht vorraussichtlich nicht aus. \n";
		if (!machbar[2])
			errormessage += "- Die Anzahl der, fuer Papier Zustaendigen Fahrzeuge reicht vorraussichtlich nicht aus. \n";
		if (!machbar[3])
			errormessage += "- Die Anzahl der, fuer Biomuell Zustaendigen Fahrzeuge reicht vorraussichtlich nicht aus. \n";
		
		// Ist das Fahrzeugdepot eingetragen ?
		if (graph.getDepot() != null)
			depotVorh = true;
		else 
			errormessage += "- Es ist kein Fahrzeugdepot eingetragen. \n";
		// Sind alle Deponien eingetragen ? 
		if (graph.getBio() != null)
			depVorh[0] = true;
		else
			errormessage += "- Es ist keine Biomuell Deponie eingetragen. \n";
		
		if (graph.getPapier() != null)
			depVorh[1] = true;
		else
			errormessage += "- Es ist keine Papiermuell Deponie eingetragen. \n";
		
		if (graph.getPlastik() != null)
			depVorh[2] = true;
		else
			errormessage += "- Es ist keine Plastikmuell Deponie eingetragen. \n";
		
		if (graph.getRest() != null)
			depVorh[3] = true;
		else
			errormessage += "- Es ist keine Restmuell Deponie eingetragen. \n";

		if (machbar[0] && machbar[1] && machbar[2] && machbar[3] && depotVorh && depVorh[0] && depVorh[1] && depVorh[2] && depVorh[3])
			return true;
		else {
			JOptionPane.showMessageDialog(null, errormessage, "Achtung!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

}
