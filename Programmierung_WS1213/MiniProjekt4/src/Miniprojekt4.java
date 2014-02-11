public class Miniprojekt4 {
	Teilnehmer[] kunden;
	Kurs[] kurse;
	int teilnehmerId;

	// Aufgabe 1
	public Miniprojekt4(int max_t, int max_k) {
		kunden = new Teilnehmer[max_t];
		kurse = new Kurs[max_k];
	}

	// Aufgabe 10
	public void berechneKosten(Teilnehmer teilnehmer) {
		float kosten = 0.0f;

		Belegung tmpKurs = teilnehmer.belegteKurse.getErsteKursbelegung();
		while (tmpKurs != null) {
			kosten += kurse[tmpKurs.kursId].grundkosten;
			tmpKurs = tmpKurs.naechsteKursBelegungsElement();
		}
		teilnehmer.beitragskostenBerechnen(kosten);
	}

	// Aufgabe 11
	public void neuerTeilnehmer(int[] kurse_ids, char teilnehemerTyp) {
		teilnehmerId = 7;
		for (int i = 0; i < kunden.length; i++) {
			if (kunden[i] == null) {
				teilnehmerId += i * 5;
				kunden[i] = new Teilnehmer(teilnehmerId, kurse_ids);
				kunden[i].setzeTeilnehmerTyp(teilnehemerTyp);
				berechneKosten(kunden[i]);
				break;
			}
		}
	}

	// Aufgabe 12
	public void kursAufloesen(int kurs_index) {
		for (int i = 0; i < kunden.length; i++) {
			if (kunden[i] != null) {
				kunden[i].kursAufloesen(kurs_index);
				berechneKosten(kunden[i]);
			}
		}
		kurse[kurs_index] = null;
	}

	public void neuenKursAnbieten(Kurs neuerKurs, int kursId) {
		int index_kurse = kurse.length;
		for (int i = 0; i < index_kurse; i++) {
			if (kurse[i] == null) {
				kurse[i] = new Kurs();
				kurse[i].setGrundkosten(neuerKurs.getGrundkosten());
				kurse[i].setSprache(neuerKurs.getSprache());
				kurse[i].setWochentage(neuerKurs.getWochentage());
				break;
			}
		}
	}

	public void ausgabe() {
		int index = kunden.length;
		System.out
				.println("---------------------------------------------------------");
		for (int i = 0; i < index; i++) {
			if (kunden[i] != null) {
				System.out.println(kunden[i].liesId() + ": "
						+ kunden[i].liesGesamtkosten() + " Anzahlkurse:"
						+ kunden[i].zaehleKurse());
				ausgabeTeilnehmerKurswahl(kunden[i]);
			}
		}
	}

	private void ausgabeTeilnehmerKurswahl(Teilnehmer teilnehmer) {
		teilnehmer.ausgabe();
	}

	public static void main(String[] args) {
		int max_t = 10;
		int max_k = 5;

		Miniprojekt4 m4 = new Miniprojekt4(max_t, max_k);

		Kurs spanisch_A1 = new Kurs(), altgriechisch_I = new Kurs(), arabisch_C1 = new Kurs(), persisch_C1 = new Kurs(), chinesisch_A2 = new Kurs();

		spanisch_A1.setSprache("Spanisch");
		spanisch_A1.setWochentage(new boolean[] { true, true, false, false,
				true, false, false });
		spanisch_A1.setGrundkosten(75.0f);

		altgriechisch_I.setSprache("Altgriechisch");
		altgriechisch_I.setWochentage(new boolean[] { true, false, true, true,
				false, false, false });
		altgriechisch_I.setGrundkosten(150.0f);

		arabisch_C1.setSprache("ARabisch");
		arabisch_C1.setWochentage(new boolean[] { false, false, true, true,
				false, false, false });
		arabisch_C1.setGrundkosten(120.0f);

		persisch_C1.setSprache("Persisch(Iranisch)");
		persisch_C1.setWochentage(new boolean[] { true, false, false, false,
				true, false, false });
		persisch_C1.setGrundkosten(80.0f);

		chinesisch_A2.setSprache("Chinesisch");
		chinesisch_A2.setWochentage(new boolean[] { true, false, false, false,
				true, false, false });
		chinesisch_A2.setGrundkosten(100.0f);

		Kurs[] kurse = { spanisch_A1, altgriechisch_I, arabisch_C1,
				persisch_C1, chinesisch_A2 };

		for (int i = 0; i < kurse.length; i++) {
			m4.neuenKursAnbieten(kurse[i], i);
		}

		m4.neuerTeilnehmer(new int[] { 0, 2, 3 }, 'S');
		m4.neuerTeilnehmer(new int[] { 1 }, 'S');
		m4.neuerTeilnehmer(new int[] { 1, 2 }, 'B');
		m4.neuerTeilnehmer(new int[] { 0 }, 'B');
		m4.neuerTeilnehmer(new int[] { 2 }, 'K');
		m4.neuerTeilnehmer(new int[] { 1, 2 }, 'B');
		m4.neuerTeilnehmer(new int[] { 3 }, 'B');
		m4.neuerTeilnehmer(new int[] { 3 }, 'K');
		m4.neuerTeilnehmer(new int[] { 0, 1, 2, 3, 4 }, 'X');
		m4.neuerTeilnehmer(new int[] { 0, 3 }, 'X');

		System.out.println("-----------------");
		m4.ausgabe();
		m4.kursAufloesen(2);
		m4.ausgabe();
	}
}