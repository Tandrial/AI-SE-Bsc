public class Kursliste {
	Belegung kopf, fuss;

	public Kursliste() {
		kopf = fuss = null;
	}

	public void neuerKursEinfuegen(int kursId) {
		Belegung neuerKurs = new Belegung();
		neuerKurs.setKursId(kursId);

		if (kopf == null) {
			kopf = fuss = neuerKurs;
		} else {

			fuss.nf = neuerKurs;
			fuss = fuss.nf;
		}
	}

	// Aufgabe 8
	public boolean loescheKurs(int kursId) {
		if (this.kopf == null)
			return false;

		if (this.kopf.getkursId() == kursId) {
			if (this.kopf == this.fuss) {
				this.kopf = this.fuss = null;
			} else {
				this.kopf = this.kopf.naechsteKursBelegungsElement();
			}
			return true;
		} else {
			Belegung tmpKurs = this.kopf;
			while (tmpKurs != null
					&& tmpKurs.naechsteKursBelegungsElement() != null) {
				if (tmpKurs.naechsteKursBelegungsElement().getkursId() == kursId) {
					if (tmpKurs.nf == fuss)
						fuss = tmpKurs;
					tmpKurs.setzeNeueBelegung(tmpKurs.nf.nf);
					return true;
				}
				tmpKurs = tmpKurs.naechsteKursBelegungsElement();
			}
			return false;
		}
	}

	public Belegung getErsteKursbelegung() {
		return this.kopf;
	}
}