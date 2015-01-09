import java.util.Scanner;

public class Uebung3_Aufgabe2 {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int wert;
		int divisor;

		System.out.print("Bitte geben Sie einen Integer-Wert ein: ");
		wert = s.nextInt();
		System.out.print("Bitte geben Sie einen Integer-Divisor ein: ");
		divisor = s.nextInt();
		s.close();

		float rechnung;

		if (divisor % 2 == 0) {
			rechnung = wert / divisor;
		} else {
			rechnung = wert / (float) divisor;
		}

		System.out.print("Ergebnis der Divison ist: " + rechnung);

	}

}
