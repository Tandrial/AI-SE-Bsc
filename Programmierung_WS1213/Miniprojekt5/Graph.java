class Graph {
	Knoten kopf, fuss;

	public void fuegeEin(Knoten k) {
		if (kopf == null)
			kopf = fuss = k;
		else {
			fuss.nf = k;
			fuss = k;
		}
	}

	// Aufgabe 2
	public int anzahlKanten() {
		int count = 0;
		Knoten tmp = this.kopf;
		while (tmp != null) {
			count += anzahlAusKanten(tmp);
			tmp = tmp.nf;
		}
		return count;
	}

	// Aufgabe 3
	public int anzahlKnoten() {
		int count = 0;
		Knoten tmp = this.kopf;
		while (tmp != null) {
			count++;
			tmp = tmp.nf;
		}
		return count;
	}

	// Aufgabe 4
	public boolean suche(Knoten p) {
		Knoten tmp = this.kopf;
		while (tmp != null) {
			if (tmp == p)
				return true;
			tmp = tmp.nf;
		}
		return false;
	}

	// Aufgabe 5
	public int anzahlKanten(Knoten k) {
		return anzahlHinKanten(k) + anzahlAusKanten(k);
	}

	// Aufgabe 6
	public int anzahlHinKanten(Knoten k) {
		int count = 0;
		Knoten tmp = this.kopf;
		while (tmp != null) {
			Kante tmpK = tmp.kopf;
			while (tmpK != null) {
				if (tmpK.kante == k)
					count++;
				tmpK = tmpK.nf;
			}
			tmp = tmp.nf;
		}
		return count;
	}

	// Aufgabe 7
	public int anzahlAusKanten(Knoten k) {
		int count = 0;
		Kante tmpK = k.kopf;
		while (tmpK != null) {
			count++;
			tmpK = tmpK.nf;
		}
		return count;
	}

	// Aufgabe 8
	public int maxKantenZahl() {
		int max = 0;
		Knoten tmp = this.kopf;
		while (tmp != null) {
			Kante tmpK = tmp.kopf;
			while (tmpK != null) {
				if (tmpK.zahl > max)
					max = tmpK.zahl;
				tmpK = tmpK.nf;
			}
			tmp = tmp.nf;
		}
		return max;
	}

	// Aufgabe 9
	public int anzKnotenMaxKantenZahl() {
		int count = 0;
		int max = maxKantenZahl();
		Knoten tmp = this.kopf;
		while (tmp != null) {
			Kante tmpK = tmp.kopf;
			while (tmpK != null) {
				if (tmpK.zahl == max)
					count++;
				tmpK = tmpK.nf;
			}
			tmp = tmp.nf;
		}
		return count;
	}

	// Aufgabe 10
	public int anzSenke() {
		int count = 0;
		Knoten tmp = this.kopf;
		while (tmp != null) {
			if (tmp.kopf == null)
				count++;
			tmp = tmp.nf;
		}
		return count;
	}

	// Aufgabe 11
	public boolean quelle(Knoten k) {
		return (anzahlHinKanten(k) == 0 && anzahlAusKanten(k) > 0);
	}

	// Aufgabe 12
	public boolean exKante(Knoten a, Knoten b) {
		Kante tmpK = a.kopf;
		while (tmpK != null) {
			if (tmpK.kante == b)
				return true;
			tmpK = tmpK.nf;
		}
		return false;
	}

	// Aufgabe 13
	public boolean doppeltVerbunden(Knoten a, Knoten b) {
		return (exKante(a, b) && exKante(b, a));
	}

	// Aufgabe 14
	public boolean alone(Knoten k) {
		return (anzahlHinKanten(k) + anzahlAusKanten(k) == 0);
	}

	// Aufgabe 15
	public int diff(Knoten k) {
		return Math.abs(anzahlHinKanten(k) - anzahlAusKanten(k));
	}

	public int diffKantenZahl(Knoten k) {
		int summeHin = 0;
		int summeAus = 0;
		Kante tmpKante;
		Knoten tmpKnoten = kopf;
		while (tmpKnoten != null) {
			tmpKante = tmpKnoten.kopf;
			while (tmpKante != null) {
				if (tmpKante.kante.equals(k)) {
					summeHin += tmpKante.zahl;
				}
				tmpKante = tmpKante.nf;
			}
			tmpKnoten = tmpKnoten.nf;
		}
		tmpKante = k.kopf;
		while (tmpKante != null) {
			summeAus += tmpKante.zahl;
			tmpKante = tmpKante.nf;
		}
		return summeHin - summeAus;
	}

	public int diffKantenZahl2(Knoten k) {
		int hin = 0;
		int aus = 0;
		Knoten tmpKnoten = kopf;
		while (tmpKnoten != null) {
			Kante tmpKante = tmpKnoten.kopf;
			while (tmpKante != null) {
				if (tmpKante.kante == k && tmpKnoten != k)
					hin += tmpKante.zahl;
				if (tmpKnoten == k)
					aus += tmpKante.zahl;
				tmpKante = tmpKante.nf;
			}
			tmpKnoten = tmpKnoten.nf;
		}
		return Math.abs(hin - aus);
	}

}