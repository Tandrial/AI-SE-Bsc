public class DatenTBaum extends Datenverwaltung {
	private TBaumElement start = null;

	public DatenListe getAll(int id) {
		DatenListe tmp = new DatenListe();
		TBaumElement laeufer = start;
		while (laeufer != null && laeufer.getEintrag().getProduktId() != id) {
			if (laeufer.getEintrag().getProduktId() > id)
				laeufer = laeufer.getKleiner();
			else
				laeufer = laeufer.getGroesser();
		}
		while (laeufer != null) {
			tmp.einfuegen(laeufer.getEintrag());
			laeufer = laeufer.getGleich();
		}
		return tmp;
	}

	// Aufgabe 8
	public int anzahlEintraege() {
		return anzahlEintraege(this.start);
	}

	private int anzahlEintraege(TBaumElement eintrag) {
		if (eintrag == null)
			return 0;
		else
			return 1 + anzahlEintraege(eintrag.getKleiner())
					+ anzahlEintraege(eintrag.getGleich())
					+ anzahlEintraege(eintrag.getGroesser());
	}

	// Aufgabe 9
	public void einfuegen(Beschreibung eintrag) {
		start = einfuegen(start, new TBaumElement(eintrag));
	}
	
	private TBaumElement einfuegen(TBaumElement knoten, TBaumElement eintrag) {
		if (knoten == null || eintrag == null) {
			if (knoten == null)
				return eintrag;
			else
				return knoten;
		} else {
			if (knoten.getEintrag().getProduktId() > eintrag.getEintrag()
					.getProduktId()) {
				knoten.setKleiner(einfuegen(knoten.getKleiner(), eintrag));
			} else if (knoten.getEintrag().getProduktId() < eintrag
					.getEintrag().getProduktId()) {
				knoten.setGroesser(einfuegen(knoten.getGroesser(), eintrag));
			} else {
				knoten.setGleich(einfuegen(knoten.getGleich(), eintrag));
			}
			return knoten;
		}
	}

	// Aufgabe 10
	public void loeschen(int i) {
		if (this.start == null)
			return;
		this.start = loeschen(i, this.start);
	}

	private TBaumElement loeschen(int wert, TBaumElement knoten) {
		if (knoten == null)
			return null;
		if (knoten.getEintrag().getProduktId() == wert) {
			if (knoten.getKleiner() == null)
				return knoten.getGroesser();
			if (knoten.getGroesser() == null)
				return knoten.getKleiner();
			TBaumElement ersatz = knoten.getGroesser();
			while (ersatz.getKleiner() != null)
				ersatz = ersatz.getKleiner();
			ersatz.setKleiner(knoten.getKleiner());
			return knoten.getGroesser();

		} else {
			if (wert < knoten.getEintrag().getProduktId())
				knoten.setKleiner(loeschen(wert, knoten.getKleiner()));
			if (wert > knoten.getEintrag().getProduktId())
				knoten.setGroesser(loeschen(wert, knoten.getGroesser()));
		}
		return knoten;
	}

	// Aufgabe 11
	public void allesLoeschen() {
		this.start = null;
	}

	public int tiefelr() {
		return tiefelr(start);
	}

	private int tiefelr(TBaumElement knoten) {
		if (knoten == null)
			return 0;
		int k = tiefelr(knoten.getKleiner());
		int g = tiefelr(knoten.getGroesser());
		if (k > g) {
			return k + 1;
		} else {
			return g + 1;
		}
	}
}