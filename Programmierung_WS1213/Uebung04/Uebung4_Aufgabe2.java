public class Uebung4_Aufgabe2 {
	public static void main(String[] args) {

		int[] array1 = { 1, 2, 6, 1, -3, 2, 6 };
		int[] array2 = { -10, 12, 21 };
		int[] result = produkt(array1, array2);
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + " ");
		}
	}

	static int[] produkt(int[] array1, int[] array2) {

		int count = Math.max(array1.length, array2.length);
		int[] ergebnis = new int[count];

		for (int i = 0; i < count; i++) {
			if (i >= array1.length || i >= array2.length)
				ergebnis[i] = 0;
			else
				ergebnis[i] = array1[i] * array2[i];
		}
		return ergebnis;
	}
}
