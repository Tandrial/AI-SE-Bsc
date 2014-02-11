public class Uebung10_Aufgabe1 {

	public static void main(String[] args) {
		System.out.println("Start allgemeiner Test Binärbaum");
		DatenBBaum testobjekt = new DatenBBaum();

		testobjekt.einfuegen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");
		testobjekt.loeschen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");

		testobjekt.einfuegen(4571);
		testobjekt.einfuegen(4571);
		testobjekt.einfuegen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");
		testobjekt.loeschen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");

		testobjekt.einfuegen(45);
		testobjekt.einfuegen(4571);
		testobjekt.einfuegen(23);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");
		testobjekt.loeschen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");

		testobjekt.einfuegen(12);
		testobjekt.einfuegen(23);
		testobjekt.einfuegen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");
		testobjekt.loeschen(4571);
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");

		testobjekt.allesLoeschen();
		System.out.println("Es sind " + testobjekt.anzahlEintraege()
				+ " Eintraege vorhanden.");
	}
}
