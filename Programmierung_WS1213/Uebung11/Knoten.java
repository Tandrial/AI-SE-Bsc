class Knoten {
	String bez;
	Knoten nf;
	Kante kopf, fuss;
	boolean marker;

	Knoten(String bez) {
		this.bez = bez;
		nf = null;
		kopf = fuss = null;
	}

	public void fuegeEin(Kante k) {
		if (kopf == null) {
			this.kopf = this.fuss = k;
		} else {
			this.fuss.nf = k;
			this.fuss = this.fuss.nf;
		}
	}

	public void gibAus() {
		System.out.print(bez + " ->");
		Kante tmpK = this.kopf;
		while (tmpK != null) {
			System.out.print(" " + tmpK.ziel.bez + "(" + tmpK.bez + ")");
			tmpK = tmpK.nf;
		}
		System.out.println();
	}
}