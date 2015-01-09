public class Uebung8_Aufgabe1 {
	public static void main(String[] args) {
		char[] abc = { 'a', 'b', 'c', 'e', 'g', 'h', 'i', 'j', 'k', 'J', 'K',
				'L', 'M', 'P', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
				'B', 'C', 'D', 'F', 'H', 'I', 'l', 'm', 'n', 'o', 'r', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		Uebung8_Aufgabe1 s = new Uebung8_Aufgabe1();
		charListe l = new charListe();
		System.out.println("Unsortiertes Alphabet:");
		s.ausgabe(abc);
		System.out.println("Sortiertes Alphabet:");
		s.sortiere(abc);
		s.ausgabe(abc);
		System.out.println("Liste:");
		l.erzeugen(abc);
		l.ausgabe();
	}

	public void sortiere(char[] abc) {
		// Durchlauf des Arrays
		for (int i = 0; i < abc.length; i++) {
			// Durchlauf des restlichen Arrays. Man muss nicht das komplette
			// Array durchsuchen,
			// weil bis zur Stelle i schon alles sortiert ist
			for (int j = i + 1; j < abc.length; j++) {
				// Wenn ich an der Stelle j ein char mit einem niedrigeren Wert
				// steht wird getauscht
				if (abc[j] < abc[i]) {
					vertausche(i, j, abc);
				}
			}
		}
	}

	private void vertausche(int i, int k, char[] abc) {
		// Element an der Stelle i wird zwischen gespeichert
		char temp = abc[i];
		// Element an der STelle k wird nach i kopiert
		abc[i] = abc[k];
		// Das zwischgengespeicherte i wird nach k kopiert
		abc[k] = temp;
	}

	public void ausgabe(char[] abc) {
		// Durchlauf des Arrays
		for (int i = 0; i < abc.length; i++) {
			// Drucke das aktuelle Zeichen
			System.out.print(abc[i] + " ");
		}
		System.out.println();
	}
}

class charListe {
	private charElement kopf;

	public charListe() {
		kopf = new charElement(' ', ' ');
		kopf.nachfolger = null;
	}

	public void erzeugen(char[] abc) {
		// chars werden intern als Zahlen abgespeichert, z. B. hat das Zeichen
		// 'a' den wert 97 und 'A' den Wert 65
		// Dazu kommt das alle Großbuchstaben "zusammenhängen" (A = 65 bis Z =
		// 90) und alle Kleinbuchstaben "zusammenhängen" (a = 97 bis z = 122).

		// Hilfsvariable zum Erstellen der Liste
		charElement pos = kopf;
		// Durchlauf des Arrays
		for (int i = 0; i < abc.length; i++) {
			// Wenn ich einen Char mit einem Wert über 90 finde kann ich
			// abbrechen, weil nur noch Kleinbuchstaben kommen
			if (abc[i] > 90)
				return;
			// Ich suche im "Rest des Arrays" für den gefundenen Buchstaben den
			// passenden Kleinbuchstaben
			for (int j = i; j < abc.length; j++) {
				// Wenn man auf den Char-Wert von einem Großbuchstaben 32
				// addiert hat man den Char-Wert vom Kleinbuchstabe
				if (abc[i] + 32 == abc[j]) {
					// Das neue char Element wird erzeugt
					charElement neu = new charElement(abc[i], abc[j]);
					// an die Liste angehängt
					pos.nachfolger = neu;
					// und die aktuelle Position wird auf das neue Element
					// verschoben
					pos = neu;
				}
			}
		}
	}

	public void ausgabe() {
		// Die "Standart" Schleife um eine Liste zudurchlaufen
		charElement temp = this.kopf;
		while (temp != null) {
			// Für jedes Element wird ausgabe() aufgerufen
			temp.ausgabe();
			temp = temp.nachfolger;
		}
		// Liste durchlaufen, hauptsäclich damit es so aussieht wie auf dem
		// Blatt.
		System.out.println("Ende");
	}
}

class charElement {
	char UpperCase; // Der Großbuchstabe als Char-Variable
	char LowerCase; // Der Kleinbuchstabe als Char-Variable
	int pos = 0;
	charElement nachfolger;

	public charElement(char UpperCase, char LowerCase) {
		// Die angegebenen Parameter werden den jeweiligen Felder zugewiesen
		this.UpperCase = UpperCase;
		this.LowerCase = LowerCase;
		// Das neue Element hat noch keinen Nachfolger
		this.nachfolger = null;
	}

	public void ausgabe() {
		// Gibt das Buchstabenpaar aus, z. B. [Aa]
		System.out.print("[" + UpperCase + LowerCase + "] ");
	}
}