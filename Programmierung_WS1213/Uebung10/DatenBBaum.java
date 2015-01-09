public class DatenBBaum {
	private BBaumElement begin = null;

	public void einfuegen(int eintrag) {
		begin = einfuegen(begin, new BBaumElement(eintrag));
	}

	private BBaumElement einfuegen(BBaumElement knoten, BBaumElement eintrag) {
		if (knoten == null || eintrag == null) {
			if (knoten == null)
				return eintrag;
			else
				return knoten;
		} else {
			if (knoten.getEintrag() > eintrag.getEintrag()) {
				knoten.setKleiner(einfuegen(knoten.getKleiner(), eintrag));
			} else {
				knoten.setGroesserGleich(einfuegen(knoten.getGroesserGleich(),
						eintrag));
			}
			return knoten;
		}
	}

	public int anzahlEintraege() {
		return anzahlEintraege(begin);
	}

	private int anzahlEintraege(BBaumElement eintrag) {
		if (eintrag == null)
			return 0;
		else
			return 1 + anzahlEintraege(eintrag.getKleiner())
					+ anzahlEintraege(eintrag.getGroesserGleich());
	}

	public void loeschen(int i) {
		if (this.begin == null)
			return;
		int pre, post;
		do {
			pre = this.anzahlEintraege();
			this.begin = loeschen(i, this.begin);
			post = this.anzahlEintraege();

		} while (pre != post);
	}

	private BBaumElement loeschen(int wert, BBaumElement knoten) {
		if (knoten == null)
			return null;
		if (knoten.getEintrag() == wert) {
			if (knoten.getKleiner() == null)
				return knoten.getGroesserGleich();
			if (knoten.getGroesserGleich() == null)
				return knoten.getKleiner();
			BBaumElement ersatz = knoten.getGroesserGleich();
			while (ersatz.getKleiner() != null)
				ersatz = ersatz.getKleiner();
			ersatz.setKleiner(knoten.getKleiner());
			return knoten.getGroesserGleich();

		} else {
			if (wert < knoten.getEintrag())
				knoten.setKleiner(loeschen(wert, knoten.getKleiner()));
			if (wert > knoten.getEintrag())
				knoten.setGroesserGleich(loeschen(wert,
						knoten.getGroesserGleich()));
		}
		return knoten;
	}

	public void allesLoeschen() {
		begin = null;
	}

	public int tiefe() {
		return tiefe(begin);
	}

	private int tiefe(BBaumElement knoten) {
		if (knoten == null)
			return 0;
		int k = tiefe(knoten.getKleiner());
		int g = tiefe(knoten.getGroesserGleich());
		if (k > g) {
			return k + 1;
		} else {
			return g + 1;
		}
	}
}