public class Uebung6_Aufgabe2 {

	public static void findMonoReihe(int[] array) {
		int start = 0;
		int laenge = 0;

		for (int i = 0; i < array.length - 1; i++) {
			int count = 0;

			while (array[i + count] <= array[i + count + 1]) {
				count++;
				if (i + count + 1 >= array.length)
					break;
			}
			if (count > laenge) {
				start = i;
				laenge = count;
			}
		}

		String result = "";
		for (int i = start; i <= start + laenge && i < array.length; i++) {
			result += String.valueOf(array[i]) + " ";
		}
		System.out.println(result);
	}

	public static void main(String[] args) {
		int[] zahlen = { 5, 3, 2, 4, 6, 1, 2, 8, 9, 1 };
		findMonoReihe(zahlen);

	}
}
