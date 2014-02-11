public class GiroKonto extends Konto implements ueberziehbar {

	private double gebuehr;
	private double kreditLimit;
	private double sollZinssatz;

	public GiroKonto(int kontoNr, double anfangsbetrag, double gebuehr) {

		this(kontoNr, anfangsbetrag, gebuehr, 0, 100.0);
	}

	// Aufgabe 1.2
	public GiroKonto(int kontoNr, double anfangsbetrag, double gebuehr,
			double kreditlimit, double sollzinssatz) {
		super(kontoNr, anfangsbetrag);
		this.gebuehr = gebuehr;
		this.sollZinssatz = sollzinssatz;
		this.kreditLimit = kreditlimit;
	}

	// Aufgabe 3.1
	public boolean abheben(double betrag) {
		if (betrag <= (this.kontoStand + this.kreditLimit)) {
			this.kontoStand -= betrag;
			return true;
		} else
			return false;
	}

	// Aufgabe 3.2
	public boolean ueberweisenAuf(Konto ziel, double betrag) {
		if (ziel == null)
			return false;
		if (abheben(betrag + this.gebuehr)) {
			ziel.einzahlen(betrag);
			return true;
		} else
			return false;
	}

	// Aufgabe 6.1
	public boolean ueberzogen() {
		return (this.kontoStand + this.kreditLimit < 0);
	}

	// Aufgabe 6.2
	public double sollZinsAnrechnen() {
		if (ueberzogen())
			this.kontoStand -= Math.abs(this.sollZinssatz / 100
					* this.kontoStand);
		return this.kontoStand;
	}

	// Aufgabe 6.3
	public double verfuegbaresGeld() {
		return this.kontoStand + this.kreditLimit;
	}
}
