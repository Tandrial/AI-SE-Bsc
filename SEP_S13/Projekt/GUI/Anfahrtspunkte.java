package GUI;

import javax.swing.JOptionPane;

import Graph.*;

public class Anfahrtspunkte {

	/**
	 * Traegt das Depot oder eine Deponie bei dem, dem Mausklick am naechsten gelegenen Knoten ein.
	 * @param x - X-Koordinate des Mausklicks.
	 * @param y - Y-Koordinate des Mausklicks.
	 * @param Art - Gibt an, was eingetragen werden soll.
	 */
	public static void punkt_eintragen(double x, double y, String Art) {
        Graph graph = Graph.getGraph();
		Knoten closest = graph.getClosestKnoten(x, y);

		System.out.println(closest.toString());

		if (closest != null) {

			if (Art.equals("Fahrzeugdepot")) {
				graph.setDepot(closest);	
			    graph.checkGraphZusammenhang();
		    } else
				
			if (Art.equals("Muellentleerungsort (Plastik)") && graph.getDepot() != null)
				graph.setPlastik(closest);			
			else

			if (Art.equals("Muellentleerungsort (Papier)") && graph.getDepot() != null)
				graph.setPapier(closest);		
			else

			if (Art.equals("Muellentleerungsort (Restmuell)") && graph.getDepot() != null)
				graph.setRest(closest);
			else

			if (Art.equals("Muellentleerungsort (Biomuell)") && graph.getDepot() != null)
				graph.setBio(closest);	
			else
				
				JOptionPane.showMessageDialog(null, "Bitte tragen Sie zuerst ein Fahrzeugdepot ein.", "Eintragung nicht moeglich", JOptionPane.WARNING_MESSAGE);
			
			
		}
	}
}
