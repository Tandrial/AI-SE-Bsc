public class SparKonto extends Konto implements verzinst {

	// Aufgabe 1.3
	public SparKonto(int kontoNr, double kontoStand) {
		super(kontoNr, kontoStand);
	}

	private static double zinssatz = 3;

	// Aufgabe 4.1
	public boolean abheben(double x) {
		if (x <= kontoStand) {
			kontoStand -= x;
			return true;
		}
		return false;
	}

	// Aufgabe 4.2
	public boolean uebertragenAuf(GiroKonto ziel, double x) {
		if (ziel == null)
			return false;
		if (abheben(x)) {
			ziel.einzahlen(x);
			return true;
		} else
			return false;
	}

	// Aufgabe 5.1
	public void zinsenAnrechnen() {
		this.kontoStand += zinssatz / 100 * this.kontoStand;
	}
}