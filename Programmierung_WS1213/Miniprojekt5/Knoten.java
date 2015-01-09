class Knoten {
	String bez;
	Knoten nf;
	Kante kopf, fuss;

	// Aufgabe 1
	public Knoten(String bez) {
		this.bez = bez;
		this.nf = null;
		this.kopf = this.fuss = null;
	}

	public void fuegeEin(Kante k) {
		if (kopf == null)
			kopf = fuss = k;
		else {
			fuss.nf = k;
			fuss = k;
		}
	}
}