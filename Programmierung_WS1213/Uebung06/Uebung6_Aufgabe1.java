public class Uebung6_Aufgabe1 {

	public static double durchschnitttemp(int[] temp) {
		double durchschnitt = 0.0;
		for (int i = 0; i < temp.length; i++) {
			durchschnitt += temp[i];
		}
		return durchschnitt / temp.length;
	}

	public static void sortiere(double[] durchschnitt) {
		for (int i = 0; i < durchschnitt.length - 1; i++) {
			for (int j = i + 1; j < durchschnitt.length; j++) {
				if (durchschnitt[i] > durchschnitt[j]) {
					double temp = durchschnitt[j];
					durchschnitt[j] = durchschnitt[i];
					durchschnitt[i] = temp;
				}
			}
		}
	}

	public static void main(String[] args) {
		int[][] zahlen = { { 1, 2, 3, 4, 5, 6, 7 }, { 6, 5, 4, 3, 2, 1 },
				{ 6, 5, 2, 1 }, { 6, 5, 4, 3, 9797, 1 }, { 6, 5, 4, 11, 2, 1 } };
		double[] mittel = new double[zahlen.length];
		for (int i = 0; i < mittel.length; i++) {
			mittel[i] = durchschnitttemp(zahlen[i]);
			System.out.print(mittel[i] + " ");
		}
		System.out.println();
		sortiere(mittel);
		for (int i = 0; i < mittel.length; i++) {
			System.out.print(mittel[i] + " ");
		}
	}
}
