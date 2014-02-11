class Kante {
	String bez;
	Kante nf;
	Knoten ziel;

	Kante(Knoten ziel, String bez) {
		this.bez = bez;
		nf = null;
		this.ziel = ziel;
	}
}
