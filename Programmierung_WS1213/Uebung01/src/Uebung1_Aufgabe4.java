import java.util.Scanner;

public class Uebung1_Aufgabe4 {
	public static void main(String[] argv) {
		int a, b, c;
		Scanner s = new Scanner(System.in);
		System.out.print("Bitte geben Sie einen Wert fuer a ein:");
		a = s.nextInt();
		System.out.print("Bitte geben Sie einen Wert fuer b ein:");
		b = s.nextInt();
		System.out.print("Bitte geben Sie einen Wert fuer c ein:");
		c = s.nextInt();
		s.close();
		// Ergaenzen Sie den Quellcode ab hier!

		System.out.println(a - b + c);
		System.out.println((a + b) * c);
		System.out.println(a * Math.pow(b, 2));
		System.out.println(Math.min(a, Math.min(b, c)));
		System.out.println(Math.max(a, Math.max(b, c)));
	}
}