import java.util.Scanner;

public class Uebung2_Aufgabe5 {
	public static void main(String[] args) {
		String[] wochentage = { "Donnerstag", "Freitag", "Samstag", "Sonntag",
				"Montag", "Dienstag", "Mittwoch" };
		String[] wochen = { "ersten", "zweiten", "dritten", "vierten",
				"fünften" };

		Scanner s = new Scanner(System.in);
		int tag;

		do {
			System.out
					.print("Bitte geben Sie eine Zahl zwischen 1 und 30 ein: ");
			tag = s.nextInt();
		} while (tag > 30 || tag < 1);
		s.close();

		int tagName = tag % 7 - 1;
		int wochenName = tag / 7;

		System.out.println(String.format(
				"Der %d.11.2012 fällt auf den %s der %s Woche.", tag,
				wochentage[tagName], wochen[wochenName]));

		String tName = "";
		String wName = "";

		switch (tagName) {
		case 0:
			tName = "Donnerstag";
			break;
		case 1:
			tName = "Freitag";
			break;
		case 2:
			tName = "Samstag";
			break;
		case 3:
			tName = "Sonntag";
			break;
		case 4:
			tName = "Montag";
			break;
		case 5:
			tName = "Dienstag";
			break;
		case 6:
			tName = "Mittwoch";
			break;
		}
		switch (wochenName) {
		case 0:
			wName = "ersten";
			break;
		case 1:
			wName = "zweiten";
			break;
		case 2:
			wName = "dritten";
			break;
		case 3:
			wName = "vierten";
			break;
		case 4:
			wName = "fünften";
			break;
		}
		System.out.println("Der " + tag + ".11.2012 fällt auf den " + tName
				+ " der " + wName + " Woche.");
	}
}
