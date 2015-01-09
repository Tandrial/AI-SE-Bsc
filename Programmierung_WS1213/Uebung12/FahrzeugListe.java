public class FahrzeugListe {
	Fahrzeug kopf;
	Fahrzeug fuss;

	public FahrzeugListe() {
		this.kopf = null;
		this.fuss = null;
	}

	public void fuegeEin(Fahrzeug neu) {
		if (kopf == null) {
			kopf = neu;
			fuss = kopf;
		} else {
			fuss.nf = neu;
			fuss = fuss.nf;
		}
	}

	public void ausgabe() {
		Fahrzeug tmp = this.kopf;
		while (tmp != null) {
			System.out.println("---");
			tmp.printType();
			tmp = tmp.nf;
		}
	}
}
