public class Uebung5_Aufgabe1 {

	public static void main(String[] args) {
		int[] x = new int[] { 1, 2, 4, 7, 8 };
		int[] y = new int[] { 2, 3, 7, 9 };
		int[] z = merge(x, y);

		for (int i = 0; i < z.length; i++) {
			System.out.print(z[i] + " ");
		}
	}

	static int[] merge(int[] x, int[] y) {
		int[] result = new int[x.length + y.length];
		int i = 0, j = 0, k = 0;

		while (i < x.length && j < y.length) {
			if (x[i] < y[j]) {
				result[k++] = x[i++];
			} else {
				result[k++] = y[j++];
			}
		}

		while (i < x.length) {
			result[k++] = x[i++];
		}

		while (j < y.length) {
			result[k++] = y[j++];
		}

		return result;
	}
}
