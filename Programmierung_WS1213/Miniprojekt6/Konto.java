public abstract class Konto {

	private int kontoNr;
	protected double kontoStand;

	// Aufgabe 1.1
	public Konto(int kontoNr, double kontoStand) {
		this.kontoNr = kontoNr;
		this.kontoStand = kontoStand;
	}

	abstract public boolean abheben(double x);

	// Aufgabe 2.1
	public void einzahlen(double x) {
		this.kontoStand += x;
	}

	// Aufgabe 2.2
	public double getKontoStand() {
		return this.kontoStand;
	}

	// Aufgabe 2.3
	public int getKontoNr() {
		return this.kontoNr;
	}
}