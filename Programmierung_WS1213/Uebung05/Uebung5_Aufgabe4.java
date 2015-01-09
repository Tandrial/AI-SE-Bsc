import java.util.*;
import java.io.*;

public class Uebung5_Aufgabe4 {

	public static void main(String[] args) {

		String dateiname = "Beispiel.txt"; // Bei Bedarf aendern!

		java.util.Scanner s = null;
		try {
			s = new Scanner(new File(dateiname));
		} catch (Exception e) {
			System.err.println("Fehler beim einlesen der Datei '" + dateiname
					+ "':");
			e.printStackTrace();
		}

		String text1D[] = null, text2D[][] = null;

		// Teil 1
		int counter = 0;
		text1D = new String[counter];
		while (s.hasNext()) {
			String[] helper = new String[++counter];
			System.arraycopy(text1D, 0, helper, 0, text1D.length);
			helper[counter - 1] = (String) s.next();
			text1D = helper;
		}

		// Teil 2
		int[] anzahl = new int[text1D.length];
		int highest = 0;
		for (int i = 0; i < text1D.length; i++) {
			anzahl[i] = 0;
			for (int j = 0; j < text1D.length; j++) {
				if (text1D[i].equals(text1D[j]))
					anzahl[i]++;
			}
			if (anzahl[i] > anzahl[highest])
				highest = i;
		}

		System.out.println("Das häufigste Wort ist: " + text1D[highest]);

		// Teil 3
		int countSatz = 1;
		int countWort = 0;
		text2D = new String[countSatz][countWort];

		for (int i = 0; i < text1D.length; i++) {
			countWort++;
			String[][] helper = new String[countSatz][];

			for (int j = 0; j < helper.length; j++) {

				if (j >= countSatz - 1) {
					if (text2D.length < countSatz) {
						helper[j] = new String[1];

					} else {
						helper[j] = new String[text2D[j].length + 1];

						System.arraycopy(text2D[j], 0, helper[j], 0,
								text2D[j].length);
					}
				} else
					helper[j] = text2D[j];
			}
			helper[countSatz - 1][countWort - 1] = text1D[i];
			text2D = helper;
			if (text1D[i].endsWith(".")) {
				countSatz++;
				countWort = 0;
			}
		}

		// Teil 4
		for (int i = 0; i < text2D.length; i++) {
			for (int j = 0; j < text2D[i].length; j++) {
				System.out.print(text2D[i][j] + " ");
			}
			System.out.println("");
		}

		// Teil 5

		System.out.println("Doppelte Sätze:");

		for (int i = 0; i < text2D.length; i++) {
			for (int j = 0; j < text2D.length; j++) {
				if (i != j && text2D[i].length == text2D[j].length) {
					boolean gleich = true;
					for (int i2 = 0; i2 < text2D[i].length; i2++) {
						if (!text2D[i][i2].equals(text2D[j][i2])) {
							gleich = false;
							break;
						}
					}
					if (gleich) {
						for (int k = 0; k < text2D[i].length; k++) {
							System.out.print(text2D[i][k] + " ");
						}
						System.out.println("");
					}

				}
			}
		}
	}
}
