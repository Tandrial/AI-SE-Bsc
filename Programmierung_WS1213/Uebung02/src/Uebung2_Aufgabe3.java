import java.util.Scanner;

public class Uebung2_Aufgabe3 {

	public static void main(String[] args) {
		// 1.
		int laenge, breite;
		Scanner s = new Scanner(System.in);
		System.out.print("Bitte geben Sie die L�nge in cm ein:");
		laenge = s.nextInt();
		System.out.print("Bitte geben Sie die Breite in cm ein:");
		breite = s.nextInt();

		// 2.
		int flaeche = laenge * breite;
		int umfang = 2 * (laenge + breite);
		double dia = Math.sqrt(laenge * laenge + breite * breite);
		int diaint = (int) dia;
		double error = dia - diaint;

		System.out.println("Fl�che: " + flaeche + " cm^2");
		System.out.println("Umfang: " + umfang + " cm");
		System.out.println("Diagonale gerundet: " + diaint + " cm");
		System.out.println("Diagonale t: " + dia + " cm");
		System.out.println("Diagonale Rundungsfehler: " + error + " cm");
		// 3.
		int hoehe;
		System.out.print("Bitte geben Sie die H�he in cm ein:");
		hoehe = s.nextInt();

		// 4.
		int volumen = flaeche * hoehe;
		int raumdia = (int) Math.sqrt(laenge * laenge + breite * breite + hoehe
				* hoehe);
		
		System.out.println("Volumen: " + volumen + " cm^3");
		System.out.println("Raumdiagonale: " + raumdia + " cm");
		// 5.
		System.out
				.println("Der Quader ist h�her als die Diagonale der Grundfl�che: "
						+ (hoehe > dia));

		s.close();
	}

}
