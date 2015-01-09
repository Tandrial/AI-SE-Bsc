public class Belegung {
	int kursId;
	boolean bestanden;
	Belegung nf;

	public Belegung() {
		kursId = 0;
		bestanden = false;
		nf = null;
	}

	public int getkursId() {
		return this.kursId;
	}

	public boolean kursBestanden() {
		return this.bestanden;
	}

	public void setKursId(int kursId) {
		this.kursId = kursId;
	}

	public void setBestandenStatus(boolean bestanden) {
		this.bestanden = bestanden;
	}

	public Belegung naechsteKursBelegungsElement() {
		return this.nf;
	}

	public void setzeNeueBelegung(Belegung neueBelegung) {
		this.nf = neueBelegung;
	}
}