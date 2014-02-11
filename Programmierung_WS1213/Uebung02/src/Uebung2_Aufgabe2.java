import java.util.Scanner;

public class Uebung2_Aufgabe2 {

	/*
	 * Erstellen Sie Java-Programm, das ausgibt, ob zwei vom Benutzer
	 * eingegebene ganze Zahlen gerade oder ungerade sind. Weiterhin soll das
	 * Programm überprüfen, ob die erste Zahl ohne Rest teilbar durch die zweite
	 * Zahl ist.
	 */

	public static void main(String[] args) {
		int a, b;
		Scanner s = new Scanner(System.in);
		System.out.print("Bitte geben Sie einen Wert fuer a ein:");
		a = s.nextInt();
		System.out.print("Bitte geben Sie einen Wert fuer b ein:");
		b = s.nextInt();
		s.close();

		System.out.println("a ist gerade: " + (a % 2 == 0));
		System.out.println("b ist gerade: " + (b % 2 == 0));
		System.out.println("a ist ohne Rest durch b teilbar: " + (a % b == 0));

	}
}
