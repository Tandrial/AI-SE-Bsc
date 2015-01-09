public class Kante {
	int zahl;
	Kante nf;
	Knoten kante;

	// Aufgabe 1
	public Kante(int Zahl, Knoten Kante) {
		this.zahl = Zahl;
		this.kante = Kante;
		this.nf = null;
	}
}