import java.util.Scanner;

public class Uebung2_Aufgabe4 {
	public static void main(String[] args) {
		int a, b, c;
		Scanner s = new Scanner(System.in);
		System.out.print("Bitte geben Sie einen Wert fuer a ein:");
		a = s.nextInt();
		System.out.print("Bitte geben Sie einen Wert fuer b ein:");
		b = s.nextInt();
		System.out.print("Bitte geben Sie einen Wert fuer c ein:");
		c = s.nextInt();
		s.close();

		// Zahlen 48 bis 57
		// groß 65 bis 90
		// klein 97 bis 122

		boolean check_a = (a >= 48 && a <= 57 || a >= 65 && a <= 90 || a >= 97
				&& a <= 122);
		boolean check_b = (b >= 48 && b <= 57 || b >= 65 && b <= 90 || b >= 97
				&& a <= 122);
		boolean check_c = (c >= 48 && c <= 57 || c >= 65 && c <= 90 || c >= 97
				&& c <= 122);

		if (check_a && check_b && check_c) {
			System.out.println("Uni-Code a: " + (char) a);
			System.out.println("Uni-Code b: " + (char) b);
			System.out.println("Uni-Code c: " + (char) c);

			boolean check1 = (a == b && a == c);
			boolean check2 = (a != b && a != c && b != c);
			boolean check3 = (a == b && c != a || a == c && b != a || b == c
					&& a != b);

			System.out.println("a = b = c == " + check1);
			System.out.println("a != b UND a!= c == " + check2);
			System.out.println("a = b != c ODER a = c !=b ODER b = c != a == "
					+ check3);
		} else {
			System.out
					.println("Eine oder mehrere Eingaben sind keien gültigen UNICODE-Chars!");
		}

	}
}
