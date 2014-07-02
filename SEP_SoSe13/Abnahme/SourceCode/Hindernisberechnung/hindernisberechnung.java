package Hindernisberechnung;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import allgemein.*;
import Graph.*;

public class hindernisberechnung {

	/**
	 * Prueft welche Knoten und Kanten im Graphen erreichbar sind und verlagert den Muell entsprechend.
	 */
	public static void erreichbarkeitpruefen() {
		Graph graph = Graph.getGraph();
		
		// Start der rekursiven Pruefung, ausgehend vom Depot
		erreichbarkeitpruefen(graph.getDepot());
		
		// Wenn Kante nicht erreichbar ist und noch Muell hat
		// - Aufruf der rekursiven Muellverlagerung
		List<Kante> alleKanten = graph.getAlleKanten();
		for (int i = 0; i < alleKanten.size(); i++) 
			if (!alleKanten.get(i).getErreichbar())
				muellverlagern(alleKanten.get(i));
	}

	/**
	 * Prueft rekusriv welche Knoten und Kanten im Graphen erreichbar sind.
	 * @param knoten - aktuell erreichte Position.
	 */
	public static void erreichbarkeitpruefen(Knoten knoten) {

		// Falls Knoten bereits erreichbar ist mache nichts 
		if (!knoten.getErreichbar()) {
			
			// Wenn Knoten aufgerufen wird Erreichbarkeit auf true setzen
		    knoten.setErreichbar(true);
		
		    // Ueberpruefe alle Kanten dieses Knotens
		    for (int i = 0; i < knoten.getKanten().size(); i++) {

		        // Wenn Kante nicht gesperrt ist, Erreichbarkeit auf true setzen
		    	// und Methode fuer anderen Knoten der Kante aufrufen
		        if (!knoten.getKanten().get(i).getGesperrt()) {
			        knoten.getKanten().get(i).setErreichbar(true);
		            erreichbarkeitpruefen(knoten.getKanten().get(i).getOther(knoten));
		        }  
		    }
		}
	}

	/**
	 * Verlagert den Muell einer (nicht erreichbaren) Kante auf die naechste erreichbare Kante.
	 * @param kante - Kante deren Muell verlagert werden soll.
	 */
	public static void muellverlagern(Kante kante) {
		Graph graph = Graph.getGraph();
		
		Knoten closestNode = null; // naechstgelegener erreichbarer Knoten
		Kante closestEdge = null; // naechstgelegene erreichbare Kante
		double closestDistance = Double.MAX_VALUE; // Abstand zu naechstgelegenem erreichbaren Knoten

		// Vergleiche mit allen Knoten des Graphens
		for (int i = 0; i < graph.getKnoten().size(); i++) {
			
			// Wenn Knoten erreichbar ist
			if (graph.getKnoten().get(i).getErreichbar()) {
				
			    // Berechne die Abweichung jewils zu "From" und zu "To"
			    Point2D.Float loc = graph.getKnoten().get(i).getLocation();
			    double abwFrom = Math.sqrt(Math.pow(loc.getX() - kante.getFrom().getLocation().getX(), 2) 
					    + Math.pow(loc.getY() - kante.getFrom().getLocation().getY(), 2));
			    double abwTo = Math.sqrt(Math.pow(loc.getX() - kante.getTo().getLocation().getX(), 2) 
				    	+ Math.pow(loc.getY() - kante.getTo().getLocation().getY(), 2));
			
			    // Wenn Abweichung geringer als bisher, setzte closest neu
			    if (abwFrom < closestDistance || abwTo < closestDistance) {
				    closestNode = graph.getKnoten().get(i);
				    closestDistance = Math.min(abwFrom, abwTo);
			    }		
			}
		}
		
		// Suche die erste erreichbare Kante des Knotens
		for (int i = 0; i < closestNode.getKanten().size(); i++)
			if (closestNode.getKanten().get(i).getErreichbar()) {
				closestEdge = closestNode.getKanten().get(i);
				break;
			}
		
		// Lege Muell an closestEdge
		closestEdge.addMuell(kante.getMuellMengeArt(""));
		kante.setMuellMengeAll(0);
	}

}
