import java.util.Scanner;

public class Uebung2_Aufgabe1 {
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

		System.out.println("Uni-Code a: " + (char) a);
		System.out.println("Uni-Code b: " + (char) b);
		System.out.println("Uni-Code c: " + (char) c);

		boolean check1 = (a == b && a == c);
		boolean check2 = (a != b && a != c && b != c);
		boolean check3 = (a == b && c != a || a == c && b != a || b == c && a != b);
		

		System.out.println("a = b = c == " + check1);
		System.out.println("a != b UND a!= c == " + check2);
		System.out.println("a = b != c ODER a = c !=b ODER b = c != a == " + check3);
	}

}
