public class graph1 {
	Knoten knoten;

	boolean vergleiche(int eins, int zwei) {
		boolean ergebnis = false;
		outer: for (Knoten k = this.knoten; k != null; k = k.Nf)
			if (k.Nr == eins)
				for (Kante kante = k.kanten; kante != null; kante = kante.Nf)
					if (kante.Ziel.Nr == zwei) {
						ergebnis = true;
						break outer;
					}

		return ergebnis;
	}

	public static void main(String args[]) {
		/* Graphen erstellen */
		graph1 graph = new graph1();
		graph.knoten = new Knoten(1, new Knoten(2, new Knoten(3, new Knoten(4,
				new Knoten(5, new Knoten(6, null))))));

		graph.knoten.kanten = new Kante(graph.knoten.Nf, null);
		graph.knoten.Nf.Nf.kanten = new Kante(graph.knoten, new Kante(
				graph.knoten.Nf, new Kante(graph.knoten.Nf.Nf.Nf, null)));
		graph.knoten.Nf.Nf.Nf.kanten = new Kante(graph.knoten, null);
		graph.knoten.Nf.Nf.Nf.Nf.kanten = new Kante(graph.knoten.Nf.Nf.Nf, null);

		/* vergleiche aufrufen */
		System.out.println("vergleiche(1,2) hat " + graph.vergleiche(1, 2)
				+ " zurueckegegeben, richtig ist true");
		System.out.println("vergleiche(1,3) hat " + graph.vergleiche(1, 3)
				+ " zurueckegegeben, richtig ist false");
		System.out.println("vergleiche(5,2) hat " + graph.vergleiche(5, 2)
				+ " zurueckegegeben, richtig ist false");
		System.out.println("vergleiche(3,4) hat " + graph.vergleiche(3, 4)
				+ " zurueckegegeben, richtig ist true");
		System.out.println("vergleiche(2,4) hat " + graph.vergleiche(2, 4)
				+ " zurueckegegeben, richtig ist false");
		System.out.println("vergleiche(4,1) hat " + graph.vergleiche(4, 1)
				+ " zurueckegegeben, richtig ist true");
	}
}

class Knoten {
	Knoten Nf;
	Kante kanten = null;
	int summe = 0;
	int Nr;

	Knoten(int Nummer, Knoten Nachfolger) {
		Nr = Nummer;
		Nf = Nachfolger;
	}
}

class Kante {
	Kante Nf;
	Knoten Ziel;
	int gewicht;

	Kante(Knoten Zielknoten, int Gewicht, Kante Nachfolger) {
		Ziel = Zielknoten;
		Nf = Nachfolger;
		gewicht = Gewicht;
	}

	Kante(Knoten Zielknoten, Kante Nachfolger) {
		this(Zielknoten, 0, Nachfolger);
	}
}