import java.io.PrintStream;
import java.util.ArrayList;

/**
 * SepCity - Testatversion.
 */
public class SepCity extends EinAusRahmen
// ==========
{
	/** Zufällig generierte serialVersionUID. */
	private static final long serialVersionUID = -7796674075784270495L;

	/** Kopf der Liste aller Städte in Sepanien */
	Stadt Staedte = null;

	/** Enthält eine Fehlermeldung zur späteren Ausgabe. */
	String Fehlermeldung = null;

	/**
	 * Führt SepCity aus (Hauptmethode).
	 * 
	 * @param Aufrufparameter
	 *            - Parameter von der Kommandozeile
	 */
	public static void main(String[] Aufrufparameter)
	// --------------------------------
	{
		SepCity sepCity = new SepCity();
		System.setOut(new PrintStream(new EinAusStream(sepCity)));
		Anfang(sepCity, "SepCity Musterlösung Einstiegsaufgabe", 25, 2);
	}

	/**
	 * Druckt eine Hilfe zu SepCity auf der Kommandozeile. Hinweis: Die Hilfe
	 * muss von Studenten während des Testats nicht erweitert werden.
	 */
	public void Hilfe() {
		System.out
				.println("--------------- Eingaben für SepCityOracle: -------------------");
		System.out
				.println("Stadt   <Name>                                           ");
		System.out
				.println("Weg     <Name>  <Tor> ,  <Zahl> ,  <Name>  <Tor>         ");
		System.out
				.println("Ausgabe                                                  ");
		System.out
				.println("direkt  <Name> ,  <Name>                                 ");
		System.out
				.println("Dreieck <Name> ,  <Name> ,  <Name>                       ");
		System.out
				.println("---------------------------------------------------------");
	}

	/**
	 * Diese Methode reagiert auf eine Eingabe, die aus der Eingabezeile (oder
	 * aus einer Zeile der Eingabe-Datei) gelesen wurde. Die aufge- rufene
	 * Methode eingegeben (...) liefert genau dann true, wenn die Eingabe dem
	 * Muster in dem String-Parameter genau entspricht. Darin bedeuten: N
	 * Eingabe eines neuen Städtenamens, der noch nicht existiert. S Eingabe
	 * eines bereits zuvor eingegebenen Städtenamens. T Himmelsrichtung eines
	 * Tors - möglich sind: n w s o. L Eingabe einer natürlichen Zahl, um eine
	 * Länge in km anzugeben. Wenn auf ein Kommandowort, z.B. "Stadt", eine
	 * syntaktisch abwein- chende Eingabe folgt, wird in die String-Variable
	 * Fehlermeldung ein entsprechender Text eingetragen und false
	 * zurückgegeben.
	 * 
	 * @param Kommando
	 *            - Das Kommando, auf das SepCity reagieren soll.
	 */
	public void Antwort(String Kommando) {
		Fehlermeldung = null;
		if (eingegeben(Kommando, "Stadt N"))

			neueStadt(Wort(Kommando, 1, 2));

		else if (eingegeben(Kommando, "Weg S T, L, S T"))

			neuerWeg(Wort(Kommando, 1, 2), Wort(Kommando, 1, 3).charAt(0),
					liesInt(Kommando, 2, 1), Wort(Kommando, 3, 1),
					Wort(Kommando, 3, 2).charAt(0));

		else if (eingegeben(Kommando, "Ausgabe"))

			ausgabe();

		else if (eingegeben(Kommando, "direkt S, S"))

			direkterWeg(Wort(Kommando, 1, 2), Wort(Kommando, 2, 1));

		else if (eingegeben(Kommando, "Dreieck S, S, S"))

			dreieck(Wort(Kommando, 1, 2), Wort(Kommando, 2, 1),
					Wort(Kommando, 3, 1));

		else if (eingegeben(Kommando, "abgelegen"))

			System.out.println(abgelegen());

		else if (eingegeben(Kommando, "Hilfe"))
			Hilfe();

		else if (eingegeben(Kommando, "Königsreise S, S"))
			Konigsreise(Wort(Kommando, 1, 2), Wort(Kommando, 2, 1));

		else if (eingegeben(Kommando, "Insel S"))
			Insel(Wort(Kommando, 1, 2));

		else if (eingegeben(Kommando, "Neustart"))
			neustart();

		else if (Fehlermeldung == null)

			System.out.println("*** Unzulässiges Kommando: "
					+ Wort(Kommando, 1, 1) + " ***");

		else
			System.out.println("*** " + Fehlermeldung + " ***");
	}

	/**
	 * Prüft, ob die Eingabe dem Muster entspricht, das als String-Para- meter
	 * übergeben wurde (wie in der Methode Antwort erläutert).
	 * 
	 * @param Kommando
	 *            - Das eingegebene Kommando.
	 * @param SollEingabe
	 *            - Das Muster, dem die Eingabe entsprechen soll.
	 */
	boolean eingegeben(String Kommando, String SollEingabe) {
		Stadt eingegebeneStaedte = null, s = null;
		// Prüfe Kommandowort:
		if (!Wort(Kommando, 1, 1).equals(Wort(SollEingabe, 1, 1)))
			return false;
		// Prüfe, ob die Anzahl der durch Kommata getrennten Sätze korrekt
		// ist:
		if (SatzAnz(Kommando) != SatzAnz(SollEingabe)) {
			Fehlermeldung = "Unzulässige Anzahl von Eingabe-Sätzen";
			return false;
		}
		// Prüfe im folgenden jeden Satz einzeln:
		for (int i = 1; i <= SatzAnz(Kommando); i++) { // Prüfe, ob im Satz i
														// die Anzahl
			// der Wörter korrekt ist:
			if (WortAnz(Kommando, i) != WortAnz(SollEingabe, i)) {
				Fehlermeldung = "Unzulässige Anzahl von Eingabe-Wörtern "
						+ "in Satz " + i;
				return false;
			}
			// Prüfe im Satz i jedes Wort einzeln:
			for (int j = 1; j <= WortAnz(Kommando, i); j++) { // Soll ist das
																// laut
				// SollEingabe geforderte
				// Wort.
				// Ist ist das an der
				// betreffenden Stelle
				// eingegebene Wort.
				String Soll = Wort(SollEingabe, i, j), Ist = Wort(Kommando, i,
						j);
				//
				// Falls gefordert wird, den Namen einer neuen Stadt einzugeben,
				// prüfe, ob die Eingabe einen solchen enthält. Namen müssen
				// mit
				// einem Buchstaben beginnen:
				if (Soll.equals("N")) {
					s = eingegebeneStaedte;
					while (s != null && !s.Name.equals(Ist))
						s = s.Nf;
					eingegebeneStaedte = new Stadt(Ist, eingegebeneStaedte);
					if (!Character.isLetter(Ist.charAt(0))) {
						Fehlermeldung = "Name einer Stadt beginnt nicht "
								+ "mit einem Buchstaben";
						return false;
					} else if (wo(Ist) != null) {
						Fehlermeldung = "Stadt " + Ist + " existiert bereits";
						return false;
					} else if (s != null) {
						Fehlermeldung = "Stadt " + Ist
								+ " kommt in der Eingabe " + "mehrfach vor";
						return false;
					}
				}
				// Falls gefordert wird, den Namen einer bereits existierenden
				// Stadt einzugeben, prüfe, ob die Eingabe einen solchen
				// enthält.
				// Innerhalb einer Eingabezeile darf ein Städtenamen nicht
				// mehr-
				// fach vorkommen:
				else if (Soll.equals("S")) {
					s = eingegebeneStaedte;
					while (s != null && !s.Name.equals(Ist))
						s = s.Nf;
					eingegebeneStaedte = new Stadt(Ist, eingegebeneStaedte);
					if (!Character.isLetter(Ist.charAt(0))) {
						Fehlermeldung = "Name einer Stadt beginnt nicht "
								+ "mit einem Buchstaben";
						return false;
					} else if (wo(Ist) == null) {
						Fehlermeldung = "Stadt " + Ist + " existiert nicht";
						return false;
					} else if (s != null) {
						Fehlermeldung = "Stadt " + Ist
								+ " kommt in der Eingabe " + "mehrfach vor";
						return false;
					}
				}
				// Falls gefordert wird, die Himmelsrichtung eines Tors
				// einzugeben,
				// prüfe, ob die Eingabe eine solche enthält. Zulässige
				// Bezeich-
				// nungen dafür sind: "n", "w", "s" und "o"
				else if (Soll.equals("T")) {
					if (Ist.length() > 1
							|| "nwso".indexOf(Ist.substring(0, 1)) < 0) {
						Fehlermeldung = Ist
								+ " ist keine zulässige Himmelsrichtung "
								+ "eines Tors";
						return false;
					}
				}
				// Falls gefordert wird, eine Länge in km einzugeben, prüfe,
				// ob
				// die
				// Eingabe eine solche enthält. Es sind nur positive ganze
				// Zahlen
				// zulässig. Null ist unzulässig:
				else if (Soll.equals("L")) {
					int Zahl = liesInt(Ist, 1, 1);
					if (ungueltig() || Zahl < 1) {
						Fehlermeldung = Ist
								+ " ist keine zulässige Längenangabe "
								+ "in km";
						return false;
					}
				}
				// Falls gefordert wird, ein bestimmtes Kommandowort einzugeben,
				// prüfe, ob die Eingabe dieses enthält:
				else {
					if (!Soll.equals(Ist)) {
						Fehlermeldung = "Anstatt " + Ist + " hätte " + Soll
								+ " geschrieben werden müssen";
						return false;
					}
				}
			}
		}
		// Keine Fehlermeldung und Rückgabe des Booleschen Werts true,
		// wenn kein Fehler aufgetreten ist:
		Fehlermeldung = null;
		return true;
	}

	// ============ Hilfsmethoden ========================
	/**
	 * Sucht das Stadt-Objekt der Stadt mit dem angegebenen Namen.
	 * 
	 * @param St
	 *            - der Name einer Stadt.
	 * @return Das Stadt-Objekt in Sepanien, das den Namen trägt, der durch den
	 *         Parameter übergeben wird.
	 */
	Stadt wo(String St) {
		Stadt s = Staedte;
		while (s != null && !s.Name.equals(St))
			s = s.Nf;
		return s;
	}

	/**
	 * Sucht den Index eines Tores im Weg-Array der Stadt.
	 * 
	 * @param TorZeichen
	 *            - Der erste Buchstabe der Himmelsichtung des gesuchten Tores.
	 * @return Der Index des gesuchten Tores (bezogen auf das Weg-Array der
	 *         Stadt.
	 */
	int TorIndex(char TorZeichen) {
		switch (TorZeichen) {
		case 'n':
			return 0;
		case 'w':
			return 1;
		case 's':
			return 2;
		default:
			return 3;
		}
	}

	/**
	 * Sucht den Index eines Tores im Weg-Array der Stadt.
	 * 
	 * @param s
	 *            - Die Stadt zu der das gesuchte Tor gehört.
	 * @param w
	 *            - Der Weg, der von dem gesuchten Tor ausgeht.
	 * @return Der Index des gesuchten Tores (bezogen auf das Weg-Array der
	 *         Stadt.
	 */
	int TorIndex(Stadt s, Weg w) {
		for (int i = 0; i < 4; i++)
			if (s.Tor[i] == w)
				return i;
		return -1;
	}

	/**
	 * Gibt den Namen zu einem Tor auf Basis seines Indizes zurück. Kann z.B.
	 * zur Ausgabe von Toren in einer lesbaren Form genutzt werden.
	 * 
	 * @param TorIndex
	 *            - Der Index des Tores bezüglich des Stadt-Arrays der Stadt zu
	 *            der das Tor gehört.
	 * @return Den Namen des Tores (z.B. "Nordtor").
	 */
	String TorName(int TorIndex) {
		switch (TorIndex) {
		case 0:
			return "Nordtor";
		case 1:
			return "Westtor";
		case 2:
			return "Südtor";
		default:
			return "Osttor";
		}
	}

	/**
	 * Sucht die Nachbarstadt, die mit einer Stadt über einen Weg an einem Tor
	 * verbunden ist.
	 * 
	 * @param Ausgangsstadt
	 *            - Die Stadt, von der aus gesucht werden soll.
	 * @param Ausganggstor
	 *            - Das Tor, von dem aus gesucht werden soll.
	 * @return null, wenn entweder die Ausgangsstadt null ist, oder wenn kein
	 *         Weg vom Ausgangstor ausgeht. Sonst die Stadt, die über den Weg
	 *         am Ausgangstor mit der Ausgangsstadt verbunden ist.
	 */
	Stadt Nachbar(Stadt Ausgangsstadt, int Ausgangstor) {
		if (Ausgangsstadt == null)
			return null;
		else {
			Weg w = Ausgangsstadt.Tor[Ausgangstor];
			if (w == null)
				return null;
			else if (w.Ziel[0] != Ausgangsstadt)
				return w.Ziel[0];
			else
				return w.Ziel[1];
		}
	}

	int Mindest(Stadt s)
	// berechne die Mindestfahrstrecke
	{
		int m = -1;
		for (int i = 0; i < 4; i++)
			// Durchlaufe alle
			// Nachbarn der Stadt s
			if (Nachbar(s, i) != null && (m == -1 || s.Tor[i].Laenge < m))
				m = s.Tor[i].Laenge;
		return m;
	}

	void loescheWeg(Weg w) {
		if (w != null) {
			for (Stadt s = this.Staedte; s != null; s = s.Nf) {
				for (int i = 0; i < 4; i++) {
					if (s.Tor[i] == w) {
						s.Tor[i] = null;
					}
				}
			}
		}
	}

	// ================ Musterlösung zu neue Stadt ==================

	/**
	 * Legt eine neue Stadt in Sepanien an und fügt diese zur Liste hinzu.
	 * 
	 * @param St
	 *            - Der Name der neu anzulegenden Stadt.
	 */
	void neueStadt(String St) {
		Staedte = new Stadt(St, Staedte);
	}

	// ================ Musterlösung zu neuer Weg ==================

	/**
	 * Legt einen neuen Weg in Sepanien an und fügt diesen zu seinen Ausgangs-
	 * und Zielstädten hinzu. Hinweis: Da Wege keine Richtung haben, hat ein
	 * Vertauschen von Ausgangs- und Zielstadt keine Auswirkungen.
	 * 
	 * @param St1
	 *            - Der Name der Ausgangsstadt des Weges.
	 * @param Tor1
	 *            - Der Name des Tores der Ausgangsstadt des Weges, an dem der
	 *            Weg beginnt.
	 * @param L
	 *            - die Länge des Weges.
	 * @param St1
	 *            - Der Name der Zielstadt des Weges.
	 * @param Tor1
	 *            - Der Name des Tores der Zielstadt des Weges, an dem der Weg
	 *            endet.
	 */
	void neuerWeg(String St1, char Tor1, int L, String St2, char Tor2) {
		Stadt Anfang = wo(St1), Ende = wo(St2);
		int t1 = TorIndex(Tor1), t2 = TorIndex(Tor2);
		if (Anfang.Tor[t1] != null) {

			System.out.println(" In " + St1 + " ist das " + TorName(t1)
					+ " bereits belegt");

		} else if (Ende.Tor[t2] != null) {

			System.out.println(" In " + St2 + " ist das " + TorName(t2)
					+ " bereits belegt");

		} else {
			Weg w = new Weg(Anfang, L, Ende);
			Anfang.Tor[t1] = w;
			Ende.Tor[t2] = w;
		}
	}

	// ================ Musterlösung zu Ausgabe ==================

	/**
	 * Gibt die Städte und Wege in Sepanien auf der Kommandozeile aus.
	 */
	void ausgabe() {
		Stadt s = Staedte;
		while (s != null) {
			System.out.println(s.Name);
			for (int i = 0; i < 4; i++) {
				if (s.Tor[i] != null) {
					System.out.println(TorName(i) + ": " + Nachbar(s, i).Name
							+ ", " + s.Tor[i].Laenge);
				} else {
					System.out.println(TorName(i) + ": Kein Weg vorhanden");
				}
			}
			System.out.println("-----------");
			s = s.Nf;
		}
	}

	// ================ Musterlösung zu direkt ==================

	// Hauptmethode, wird vom EinAusRahmen aufgerufen

	/**
	 * Sucht einen direkten Weg zwischen zwei Städten. Falls ein Weg gefunden
	 * wird, wird dieser ausgegeben. Andernfalls wird eine Meldung ausgegeben,
	 * dass ein solcher Weg nicht existiert.
	 * 
	 * @param St1
	 *            - Der Name der ersten Stadt.
	 * @param St2
	 *            - Der Name der zweiten Stadt.
	 */
	void direkterWeg(String St1, String St2) {
		Weg w = direkt(St1, St2);
		if (w != null) {
			System.out.println("Direkter Weg: " + St1 + " "
					+ TorName(TorIndex(wo(St1), w)) + ", " + w.Laenge + ", "
					+ St2 + " " + TorName(TorIndex(wo(St2), w)));
		} else {
			System.out.println("Kein direkter Weg zwischen " + St1 + " und "
					+ St2);
		}
	}

	/**
	 * Prüft, ob ein direkter Weg zwischen zwei Städten vorliegt und gibt
	 * einen solchen zurück.
	 * 
	 * @param St1
	 *            - Name der ersten Stadt
	 * @param St2
	 *            - Name der zweiten Stadt
	 * @return Falls ein direkter Weg vorliegt, wird dieser zurückgegeben.
	 *         Sonst gibt die Methode null zurück, um anzuzeigen, dass keine
	 *         direkte Verbindung existiert.
	 */
	Weg direkt(String St1, String St2) {
		Stadt s1 = wo(St1), s2 = wo(St2);
		int tor = -1, laenge = 0;
		for (int i = 0; i < 4; i++) {
			if ((Nachbar(s1, i) == s2)
					&& ((tor == -1) || (s1.Tor[i].Laenge < laenge))) {
				tor = i;
				laenge = s1.Tor[i].Laenge;
			}
		}
		if (tor >= 0) {
			return s1.Tor[tor];
		} else {
			return null;
		}
	}

	// ================ Musterlösung zu dreieck ==================

	/**
	 * Löst die Aufgabe "Dreieck" aus der Übung. Prüft also, ob zwischen
	 * jedem Paar aus einer Menge von drei Städten ein direkter Weg existiert.
	 * Falls ein solches Dreieck existiert, wird dieses ausgegeben, andernfalls
	 * wird ausgegeben, dass kein Dreieck existiert.
	 * 
	 * @param St1
	 *            - Der Name der ersten Stadt aus der zu prüfenden Menge.
	 * @param St2
	 *            - Der Name der zweiten Stadt aus der zu prüfenden Menge.
	 * @param St3
	 *            - Der Name der dritten Stadt aus der zu prüfenden Menge.
	 */
	void dreieck(String St1, String St2, String St3) {

		Weg w12 = direkt(St1, St2);
		Weg w13 = direkt(St1, St3);
		Weg w23 = direkt(St2, St3);

		if ((w12 != null) && (w13 != null) && (w23 != null)) {

			System.out.println("Dreieck gefunden!");
			System.out
					.println("Weg von " + St1 + ", "
							+ TorName(TorIndex(wo(St1), w12)) + " nach " + St2
							+ ", " + TorName(TorIndex(wo(St2), w12))
							+ " Länge " + w12.Laenge);
			System.out
					.println("Weg von " + St2 + ", "
							+ TorName(TorIndex(wo(St2), w23)) + " nach " + St3
							+ ", " + TorName(TorIndex(wo(St3), w23))
							+ " Länge " + w23.Laenge);
			System.out
					.println("Weg von " + St3 + ", "
							+ TorName(TorIndex(wo(St3), w13)) + " nach " + St1
							+ ", " + TorName(TorIndex(wo(St1), w13))
							+ " Länge " + w13.Laenge);

		} else {
			System.out
					.println("Keine direkte Verbindung zwischen allen drei Städten!");
		}

	}

	// ================ Beispieltestaufgabe "abgelegen" ==================
	/**
	 * Beispieltestaufgabe "abgelegen"
	 * 
	 * @return Das Ergebnis der Aufgabe "abgelegen" wie in der Übung
	 *         besprochen.
	 */
	String abgelegen() {
		Stadt s = Staedte;
		int m = 0;
		Stadt jwd = null;
		while (s != null) // Durchlaufe alle Städte
		{
			if (Mindest(s) == -1 || Mindest(s) > m) {
				jwd = s;
				m = Mindest(s); // Hilfsmethoden
				if (m == -1) {
					return jwd.Name + " ohne Weg zur Außenwelt";
				}

			}
			s = s.Nf;
		}
		if (jwd == null)
			return "Es gibt keine Stadt in Sepanien."; // Ausgabe
		else
			return jwd.Name + " ist " + m + " km abgelegen.";
	}

	/* VT Testataufgaben */

	private void Konigsreise(String start, String ende) {
		Stadt s = wo(start);
		Stadt e = wo(ende);

		ArrayList<Stadt> erreichbar = new ArrayList<Stadt>();
		erreichbar.add(s);

		for (int i = 0; i < erreichbar.size(); i++) {
			Stadt aktuell = erreichbar.get(i);
			for (int j = 0; j < 4; j++) {
				Stadt neu = Nachbar(aktuell, j);

				if (neu != null && !erreichbar.contains(neu))
					erreichbar.add(neu);

				if (erreichbar.contains(e)) {
					System.out.println("Kein Umbau nätig!");
					return;
				}
			}
		}

		Weg w = s.Tor[TorIndex('n')];
		loescheWeg(w);
		w = e.Tor[TorIndex('w')];
		loescheWeg(w);

		neuerWeg(start, 'n', 7, ende, 'w');
	}

	void Insel(String st1) {
		System.out.println("Die kleinste Insel von " + st1 + " ist ");
		ArrayList<Stadt> l = new ArrayList<Stadt>();
		l.add(wo(st1));

		for (int i = 0; i < l.size(); i++) {
			Stadt aktuell = l.get(i);
			for (int j = 0; j < 4; j++) {
				Stadt neu = Nachbar(aktuell, j);
				if (neu != null && !l.contains(neu))
					l.add(neu);
			}
		}
		for (int i = 0; i < l.size(); i++)
			System.out.print(l.get(i).Name + " ");
		System.out.println();
	}

	// ================ Testautomatisierung ==================
	/**
	 * Erschafft Sepanien neu.
	 * */
	void neustart() {
		Staedte = null;
	}
}
