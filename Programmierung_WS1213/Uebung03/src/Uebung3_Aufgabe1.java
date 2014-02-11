import java.util.Scanner;

public class Uebung3_Aufgabe1 {
	public static void main(String[] args) {

		String[] Monate = { "", "Januar", "Februar", "März", "April", "Mai",
				"Juni", "Juli", "August", "September", "Oktober", "November",
				"Dezember" };

		Scanner s = new Scanner(System.in);
		int monat;

		do {
			System.out
					.print("Bitte geben Sie eine Zahl zwischen 1 und 12 ein: ");
			monat = s.nextInt();
		} while (monat > 12 || monat < 1);
		s.close();

		System.out.println(String.format("Der %d. Monate ist der %s.", monat,
				Monate[monat]));
	}

}
