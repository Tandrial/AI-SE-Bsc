package routenberechnung;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Graph.Graph;
import Graph.Kante;
import Graph.Knoten;

public class eulerkreis {

	/**
	 * Dies ist die Kantenliste der Eulertour
	 */
	private static ArrayList<Kante> eulerKanten = new ArrayList<Kante>();

	public static void makeEuler(Graph g) {
		System.out.println("vorher " + isEuler(g));
		if (!isEuler(g)) {
			for (Kante k : g.getAlleKanten()) {
				k.getTo().addKante(k);
				k.getFrom().addKante(k);
			}
			System.out.println("nachher: " + isEuler(g));
		}
	}

	private static boolean isEuler(Graph g) {
		int count = 0;
		for (Knoten k : g.getKnoten()) {
			if (k.getKanten().size() % 2 == 1) {
				count++;
			}
		}
		return count == 0;
	}

	/**
	 * Durchsucht einen Graphen nach einem geschlossenen Zyklus, der keine Kante in g zweimal durchlaeuft<br>
	 * Anmerkung: Die Methode setzt das Attribut "besucht" der besuchten Knoten und Kanten
	 * @param g - Graph, in dem gesucht werden soll
	 * @param start - Knoten, an dem der Zyklus beginnen und enden soll
	 * @return Knotenfolge, die den Zyklus repraesentiert
	 */
	public static List<Knoten> findeZyklus(Graph g, Knoten start) {
		List<Knoten> way = new ArrayList<Knoten>(); // enthaelt alle Knoten, die im spaeteren Unterkreis enthalten sind
		Knoten current = start;
		do {
			System.out.println("current is " + current);
			current.setBesucht(true); // Markiere den aktuellen Knoten als besucht...
			way.add(current); // ...und fuege ihn der Wegliste hinzu
			Set<Knoten> neighbors = current.getNachbarn();
			Knoten next = neighbors.iterator().next();
			// graph.removeEdge(current, next);
			Kante k = g.getKante(current, next); // Kante, die die beiden Knoten verbindet
			k.setBesucht(true); // Markiere die Kante als besucht

			eulerKanten.add(k);

			current = next;
		} while (!(current == start)); // solange wir keinen geschlossenen Zyklus gefunden haben
		way.add(current);
		return way;
	}

	/**
	 * Dem Der Hierholzer sein Algorithmus zum Finden eines Eulerkreises
	 * @param g - Der Graph
	 * @param start - Der Startknoten
	 * @return Die Eulertour
	 */
	public static List<Knoten> machDenHolzmichl(Graph g, Knoten start) {
		// TODO Ist der Knoten in g?
		// Cleanup
		for (Knoten k : g.getKnoten()) {
			k.setBesucht(false);
		}
		for (Kante k : g.getAlleKanten()) {
			k.setBesucht(false);
		}

		// Hier faengt der eigentliche Algo an
		List<Knoten> tour = findeZyklus(g, start); // Bilde den ersten Unterkreis
		for (int i = 0; i < tour.size() && !g.alleKantenBesucht(); i++) { // Solange nicht alle Kanten besucht wurden
			Knoten current = tour.get(i);
			if (current.getNachbarn().size() > 0) {
				System.out.println("Der Knoten " + current + " hat einen Knotengrad > 0");
				List<Knoten> unterKreis = findeZyklus(g, current);
				System.out.print("Der Unterkreis umfasst: ");
				for (Knoten k : unterKreis) {
					System.out.print(k + " ");
				}
				System.out.println("Tour vor Einfügen: " + printListe(tour));
				// Aktuellen Knoten durch Knoten des Unterkreises in der Tour ersetzen
				tour.remove(i);
				for (Knoten k : unterKreis) {
					tour.add(i, k); //
				}
				System.out.println("Tour nach Einfügen: " + printListe(tour));
			}
		}
		return tour;
	}

	/**
	 * Liefert die Kantenliste vom Eulerzyklus zurueck
	 * @return Kantenliste vom Eulerzyklus
	 */
	public static ArrayList<Kante> getEulerKanten() {
		return eulerKanten;
	}

	/**
	 * Gibt eine komplette Liste aus
	 * @param liste
	 * @return Konsolenausgabe als String
	 */
	private static String printListe(List liste) {
		String s = "";
		for (Object o : liste) {
			s += o + " ";
		}
		return s;
	}

}