public class Miniprojekt3_Pokalspiel {

	public int getMannschaftAnzahl(Spiele spiele) {
		int n = 0;
		Spiel temp = spiele.spiel;
		while (temp != null) {
			if (temp.getHeim() > n)
				n = temp.getHeim();
			if (temp.getGast() > n)
				n = temp.getGast();
			temp = temp.naechstesSpielElement();
		}
		return ++n;
	}

	// Aufgabe 1
	public int[][] listeNachArray(Spiele spiele) {
		int n = getMannschaftAnzahl(spiele);
		int[][] result = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = -1;
			}
		}

		Spiel temp = spiele.spiel;
		while (temp != null) {
			result[temp.getHeim()][temp.getGast()] = temp.getToreH();
			result[temp.getGast()][temp.getHeim()] = temp.getToreG();
			temp = temp.naechstesSpielElement();
		}
		return result;
	}

	// Aufgabe 2
	public int tordifferenzEins(Spiele spiele) {
		int counter = 0;
		Spiel temp = spiele.spiel;
		while (temp != null) {
			if (Math.abs(temp.getToreG() - temp.getToreH()) == 1)
				counter++;
			temp = temp.naechstesSpielElement();
		}
		return counter;
	}

	// Aufgabe 3
	public int[] anzahlSpiele(Spiele spiele) {
		int n = getMannschaftAnzahl(spiele);
		int[] result = new int[n];

		Spiel temp = spiele.spiel;
		while (temp != null) {
			if (temp.getToreG() != -1 && temp.getToreH() != -1) {
				result[temp.getHeim()]++;
				result[temp.getGast()]++;
			}
			temp = temp.naechstesSpielElement();
		}
		return result;
	}

	// Aufgabe 4
	double[] torMittelwert(Spiele spiele) {
		int n = getMannschaftAnzahl(spiele);

		double[] mittelwert = new double[n];
		int[] spielAnzahl = new int[n];

		Spiel temp = spiele.spiel;
		while (temp != null) {
			if (temp.getToreG() != -1 && temp.getToreH() != -1) {
				spielAnzahl[temp.getHeim()]++;
				spielAnzahl[temp.getGast()]++;

				mittelwert[temp.getHeim()] += temp.getToreH();
				mittelwert[temp.getGast()] += temp.getToreG();
			}
			temp = temp.naechstesSpielElement();
		}

		for (int i = 0; i < n; i++) {
			if (spielAnzahl[i] != 0)
				mittelwert[i] /= spielAnzahl[i]; 
		}
		return mittelwert;
	}	

	// Aufgabe 5
	int sieger(Spiele spiele) {
		int n = getMannschaftAnzahl(spiele);
		int gewinner = -1;

		int[] siegTore = new int[n];
		int[] siegCounter = new int[n];
		int[] torCounter = new int[n];

		Spiel temp = spiele.spiel;
		while (temp != null) {

			if (temp.getToreG() != -1 && temp.getToreH() != -1) {

				torCounter[temp.getHeim()] += temp.getToreH();
				torCounter[temp.getGast()] += temp.getToreG();

				if (temp.getToreH() > temp.getToreG()) {
					siegCounter[temp.getHeim()]++;
					siegTore[temp.getHeim()] += temp.getToreH();
				}

				if (temp.getToreG() > temp.getToreH()) {
					siegCounter[temp.getGast()]++;
					siegTore[temp.getGast()] += temp.getToreG();
				}
			}
			temp = temp.naechstesSpielElement();
		}

		// CHECK 1: Die Manschaft mit den meisten Toren in gewonnenen Spielen
		// gewinnt

		int maxSiegTore = -1;

		for (int i = 0; i < n; i++) {
			if (siegTore[i] == maxSiegTore) {
				gewinner = -1;
			}

			if (siegTore[i] > maxSiegTore) {
				maxSiegTore = siegTore[i];
				gewinner = i;
			}
		}

		if (gewinner != -1)
			return gewinner;

		// CHECK 2: Mehr als eine Mannschaft hat gleich viele SiegTore, es
		// gewinnt die mit den meisten Siegen

		int maxSiege = -1;
		for (int i = 0; i < n; i++) {
			if (siegTore[i] == maxSiegTore) {
				if (siegCounter[i] == maxSiege) {
					gewinner = -1;
				}
				if (siegCounter[i] > maxSiege) {
					maxSiege = siegCounter[i];
					gewinner = i;
				}
			}
		}

		if (gewinner != -1)
			return gewinner;

		// CHECK 3 Mehr als eine Mannschaft hat gleich viele Siege, es gewinnt
		// die mit den meisten Toren

		int maxTore = -1;
		for (int i = 0; i < n; i++) {
			if (siegCounter[i] == maxSiege) {
				if (torCounter[i] == maxTore) {
					gewinner = -1;
				}

				if (torCounter[i] > maxTore) {
					maxTore = torCounter[i];
					gewinner = i;
				}
			}
		}
		return gewinner;
	}

	// Zum Testen
	public void ausgabe(Spiele spiele) {

		Spiel temp = spiele != null ? spiele.spiel : null;
		int cnt = 0;

		while (temp != null) {
			cnt++;
			System.out.println(" ----------------");
			System.out.println("|Spiel " + cnt);
			System.out.println("|heim: " + temp.getHeim() + " toreH: "
					+ temp.getToreH());
			System.out.println("|Gast: " + temp.getGast() + " toreG: "
					+ temp.getToreG());
			System.out.println(" ----------------");
			temp = temp.nf;
		}
		System.out.println(" ----------------");
		System.out.println("|      Null      ");
		System.out.println(" ----------------");
	}

	public void inhalt_des_arrays(int[][] temp) {
		System.out.println("Spiel");
		int temp_parm = 0;
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				temp_parm = temp[i][j];
				if (temp_parm == -1) {
					System.out.print(temp[i][j] + " ");
				} else {
					System.out.print(" " + temp[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	public static void main(String... strings) {

		Miniprojekt3_Pokalspiel m3p = new Miniprojekt3_Pokalspiel();

		Spiele spiele = new Spiele(0, 1, 3, 4);
		spiele.neuesSpiel(0, 2, 4, 4);
		spiele.neuesSpiel(1, 2, 2, 1);

		System.out.println(" Miniprojekt Test:");
		m3p.ausgabe(spiele);

		System.out.println("\nAufgabe 1");
		m3p.inhalt_des_arrays(m3p.listeNachArray(spiele));

		System.out.println("\nAufgabe 2");
		System.out.println("Spiele mit Tordiffernz 1: insgesamt "
				+ m3p.tordifferenzEins(spiele));

		System.out.println("\nAufgabe 3");
		int[] test_aufgabe3a = m3p.anzahlSpiele(spiele);
		for (int i = 0; test_aufgabe3a != null && i < test_aufgabe3a.length; i++)
			System.out.println("anzahl_spiele_array[" + i + "]:="
					+ test_aufgabe3a[i]);

		System.out.println("\nAufgabe 4");
		double[] tor_mittelwert = m3p.torMittelwert(spiele);
		System.out.print("Mitellwert: ");
		for (int i = 0; i < tor_mittelwert.length; i++)
			System.out.print(tor_mittelwert[i] + " ; ");
		System.out.println();

		System.out.println("\nAufgabe 5");
		int pokal_sieger = m3p.sieger(spiele);
		System.out.println("Sieger des Turniers:");
		if (pokal_sieger < 0) {
			System.out.println("Es gibt kein Sieger");
		} else {
			System.out.println("Mannschaft " + pokal_sieger
					+ " hat das Pokalspiel gewonnen!");
		}
	}
}
