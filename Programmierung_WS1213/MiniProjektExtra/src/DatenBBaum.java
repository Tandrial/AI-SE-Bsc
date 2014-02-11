public class DatenBBaum extends Datenverwaltung {
	private BBaumElement begin = null;

	public void einfuegen(Beschreibung eintrag) {
		begin = einfuegen(begin, new BBaumElement(eintrag));
	}

	private BBaumElement einfuegen(BBaumElement knoten, BBaumElement eintrag) {
		if (knoten == null || eintrag == null) {
			if (knoten == null)
				return eintrag;
			else
				return knoten;
		} else {
			if (knoten.getEintrag().getProduktId() > eintrag.getEintrag()
					.getProduktId()) {
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

	// Aufgabe 3
	public DatenListe getAll(int id) {
		DatenListe result = new DatenListe();
		if (this.begin != null)
			getAll(id, this.begin, result);
		return result;
	}

	private void getAll(int id, BBaumElement knoten, DatenListe liste) {
		if (knoten.getEintrag().getProduktId() == id)
			liste.einfuegen(knoten.getEintrag());
		if (knoten.getKleiner() != null
				&& knoten.getEintrag().getProduktId() > id)
			getAll(id, knoten.getKleiner(), liste);
		if (knoten.getGroesserGleich() != null
				&& knoten.getEintrag().getProduktId() <= id)
			getAll(id, knoten.getGroesserGleich(), liste);
	}

	// Aufgabe 4
	public void loeschen(int i) {
		int pre, post;
		do {
			if (this.begin == null)
				return;
			pre = this.anzahlEintraege();
			this.begin = loeschen(i, this.begin);
			post = this.anzahlEintraege();

		} while (pre != post);
	}

	private BBaumElement loeschen(int wert, BBaumElement knoten) {
		if (knoten == null)
			return null;
		if (knoten.getEintrag().getProduktId() == wert) {
			if (knoten.getKleiner() == null)
				return knoten.getGroesserGleich();
			if (knoten.getGroesserGleich() == null)
				return knoten.getKleiner();
			BBaumElement ersatz = knoten.getGroesserGleich();
			while (ersatz.getKleiner() != null)
				ersatz = ersatz.getKleiner();
			ersatz.setKleiner(knoten.getKleiner());
			return ersatz;// knoten.getGroesserGleich();

		} else {
			if (wert < knoten.getEintrag().getProduktId())
				knoten.setKleiner(loeschen(wert, knoten.getKleiner()));
			if (wert > knoten.getEintrag().getProduktId())
				knoten.setGroesserGleich(loeschen(wert,
						knoten.getGroesserGleich()));
		}
		return knoten;
	}

	/*
	 * private BBaumElement loeschen2(int i, BBaumElement knoten, boolean
	 * ersatzL) { if (knoten == null) return null;
	 * 
	 * if (knoten.getEintrag().getProduktId() == i && (!ersatzL ||
	 * (knoten.getKleiner() == null && knoten .getGroesserGleich() == null))) {
	 * 
	 * if (knoten.getKleiner() == null) { return knoten.getGroesserGleich(); }
	 * else if (knoten.getGroesserGleich() == null) { return
	 * knoten.getKleiner(); } else {
	 * 
	 * BBaumElement ersatz = knoten.getKleiner(); while
	 * (ersatz.getGroesserGleich() != null) { ersatz =
	 * ersatz.getGroesserGleich(); }
	 * 
	 * ersatz.setKleiner(loeschen2(ersatz.getEintrag().getProduktId(),
	 * knoten.getKleiner(), true));
	 * ersatz.setGroesserGleich(knoten.getGroesserGleich()); knoten = null;
	 * return ersatz; } }
	 * 
	 * if (i < knoten.getEintrag().getProduktId()) {
	 * knoten.setKleiner(loeschen2(i, knoten.getKleiner(), ersatzL)); return
	 * knoten; } else { knoten.setGroesserGleich(loeschen2(i,
	 * knoten.getGroesserGleich(), ersatzL)); return knoten; } }
	 */

	public void allesLoeschen() {
		begin = null;
	}

	// Aufgabe 5
	public Beschreibung[] baum2array() {
		Beschreibung[] result = new Beschreibung[anzahlEintraege()];
		if (this.begin != null)
			baum2array(result, this.begin, 0);
		return result;
	}

	private int baum2array(Beschreibung[] array, BBaumElement startNode,
			int index) {
		int rc = index;
		if (startNode.getKleiner() != null) {
			rc = baum2array(array, startNode.getKleiner(), index);
		}
		array[rc++] = startNode.getEintrag();
		if (startNode.getGroesserGleich() != null) {
			return baum2array(array, startNode.getGroesserGleich(), rc);
		} else {
			return rc;
		}
	}

	// Aufgabe 6
	public void array2baum(Beschreibung[] eingabeArray) {
		if (eingabeArray.length > 0) {
			array2baum(eingabeArray, 0, eingabeArray.length - 1);
		}
	}

	private void array2baum(Beschreibung[] array, int start, int end) {
		if (start > end)
			return;
		int mid = start + (end - start) / 2;

		this.einfuegen(array[mid]);
		array2baum(array, start, mid - 1);
		array2baum(array, mid + 1, end);
	}

	// Aufgabe 7
	public void baumBalancieren() {
		Beschreibung[] baum = baum2array();
		this.allesLoeschen();
		quicksort(baum, 0, baum.length - 1);
		array2baum(baum);
	}

	private void quicksort(Beschreibung[] baum, int pLow, int pHigh) {
		int hLinks = pLow;
		int hRechts = pHigh;
		int pivot = baum[(pLow + pHigh) / 2].getProduktId();

		while (hLinks <= hRechts) {
			while (baum[hLinks].getProduktId() < pivot)
				hLinks++;
			while (baum[hRechts].getProduktId() > pivot)
				hRechts--;

			if (hLinks <= hRechts) {
				Beschreibung tmp = baum[hLinks];
				baum[hLinks] = baum[hRechts];
				baum[hRechts] = tmp;
				hLinks++;
				hRechts--;
			}
		}

		if (pLow < hRechts)
			quicksort(baum, pLow, hRechts);
		if (hLinks < pHigh)
			quicksort(baum, hLinks, pHigh);
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