public class Spiele {

	Spiel spiel;

	public Spiele(int heim, int gast, int heimTore, int gastTore) {
		spiel = new Spiel();
		spiel.setzeSpielParameter(heimTore, gastTore, heim, gast);
	}

	void neuesSpiel(int heim, int gast, int heimTore, int gastTore) {
		Spiel neuesSpiel = new Spiel();
		neuesSpiel.setzeSpielParameter(heimTore, gastTore, heim, gast);
		Spiel letztesSpiel = letztesSpiel();
		letztesSpiel.setzeSpiel(neuesSpiel);
	}

	/* Die Methode liefert das letzte Spiel in der Liste (das letzte Element) */
	public Spiel letztesSpiel() {
		Spiel startSpiel = spiel;
		while (startSpiel.naechstesSpielElement() != null) {
			startSpiel = startSpiel.naechstesSpielElement();
		}
		return startSpiel;
	}
}
