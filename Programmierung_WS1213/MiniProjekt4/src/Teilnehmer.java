public class Teilnehmer {
	int id; // eindeutige Teilnehmer Id.
	char teilnehmerTyp; // den Teilnehmertyp {Kind, Student, Berufstätige,
						// sonstiges} festlegen.
	float gesamtkosten; // Gesamtkosten des Teilnehmers
	Kursliste belegteKurse; // Die Liste mit den angemeldeten Kursen.

	// Aufgabe 2.
	public Teilnehmer(int teilnehmerId, int[] id_der_kurse) {
		this.id = teilnehmerId;
		this.teilnehmerTyp = 'X';
		this.belegteKurse = new Kursliste();
		this.gesamtkosten = 0.0f;

		for (int i = 0; i < id_der_kurse.length; i++) {
			this.belegteKurse.neuerKursEinfuegen(id_der_kurse[i]);
		}
	}

	// Aufgabe 3
	public void beitragskostenBerechnen(float kosten) {
		switch (this.teilnehmerTyp) {
		case 'S':
			this.gesamtkosten = 0.8f * kosten;
			break;
		case 'K':
			this.gesamtkosten = 0.5f * kosten;
			break;
		case 'B':
			this.gesamtkosten = 0.85f * kosten;
			break;
		default:
			this.gesamtkosten = 0.90f * kosten;
			break;
		}
	}

	// Aufgabe 4
	public int liesId() {
		return this.id;
	}

	// Aufgabe 5
	public char liesTeilnehemerTyp() {
		return this.teilnehmerTyp;
	}

	// Aufgabe 6
	public float liesGesamtkosten() {
		return this.gesamtkosten;
	}

	// Aufgabe 7
	public int zaehleKurse() {
		int anzahl = 0;
		Belegung tmpKurs = this.belegteKurse.getErsteKursbelegung();
		while (tmpKurs != null) {
			anzahl++;
			tmpKurs = tmpKurs.naechsteKursBelegungsElement();
		}
		return anzahl;
	}

	// Aufgabe 9
	public int[] alleAngemeldentenKurse() {
		int[] kurse = new int[this.zaehleKurse()];
		int count = 0;
		Belegung tmpKurs = this.belegteKurse.getErsteKursbelegung();
		while (tmpKurs != null) {
			kurse[count++] = tmpKurs.getkursId();
			tmpKurs = tmpKurs.naechsteKursBelegungsElement();
		}
		return kurse;
	}

	public void setzeTeilnehmerTyp(char teilnehmerTyp) {
		this.teilnehmerTyp = teilnehmerTyp;
	}

	public void kursAufloesen(int kursId) {
		belegteKurse.loescheKurs(kursId);
	}

	public Kursliste lieferKursliste() {
		return this.belegteKurse;
	}

	// gibt aus welche Kurse der Teilnehmer belegt hat.
	public void ausgabe() {
		System.out.println("Gesmatkosten:" + this.liesGesamtkosten());
	}
}