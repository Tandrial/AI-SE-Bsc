public class Bank {
	public static final int MAX_KONTEN = 10000;
	int anzahl = 0;
	Konto[] konten = new Konto[MAX_KONTEN];

	Konto kontoSuchen(int kontoNr) {
		for (int i = 0; i < konten.length; i++) {
			if (konten[i] != null && konten[i].getKontoNr() == kontoNr) {
				return konten[i];
			}
		}
		return null;
	}

	// Aufgabe 7.1
	public boolean kontoAnlegen(Konto neuesKonto) {
		if (kontoSuchen(neuesKonto.getKontoNr()) == null) {
			for (int i = 0; i < konten.length; i++) {
				if (konten[i] == null) {
					konten[i] = neuesKonto;
					anzahl++;
					return true;
				}
			}
			return false;
		} else
			return false;
	}

	// Aufgabe 7.2
	public boolean kontoLoeschen(int kontoNr) {
		if (kontoSuchen(kontoNr) != null) {
			for (int i = 0; i < konten.length; i++) {
				if (konten[i].getKontoNr() == kontoNr) {
					konten[i] = null;
					anzahl--;
					return true;
				}
			}
			return false;
		} else
			return false;
	}

	// Aufgabe 7.3
	public double berechneVermoegen(Konto[] konten) {
		double vermoegen = 0d;
		for (int i = 0; i < konten.length; i++) {
			if (konten[i] != null)
				vermoegen += konten[i].kontoStand;
		}
		return vermoegen;
	}

	private void ausgabeAllerKonten() {
		for (Konto i : konten)
			if (i != null)
				System.out.println("Konto " + i.getKontoNr()
						+ " hat einen Kontostand von: " + i.getKontoStand());
	}

	public static void main(String[] args) {
		Bank test = new Bank();
		GiroKonto dummy = new GiroKonto(1234, 875.451, 0.1, 412, 17.45);
		test.kontoAnlegen(dummy);
		test.ausgabeAllerKonten();
	}
}