public class Uebung5_Aufgabe2 {

	public static void main(String[] args) {
		int[][] A = new int[][] { { 1, 2, 3 }, { 4, 5, 6 } };
		int[][] B = new int[][] { { 6, -1 }, { 3, 2 }, { 0, -3 } };

		int[][] result = multiply(A, B);

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				System.out.print(result[i][j] + " ");
			}
			System.out.println();
		}

	}

	static int[][] multiply(int[][] matA, int[][] matB) {

		if (matA[0].length != matB.length)
			return null;
		int[][] ergebnis = new int[matA.length][matB[0].length];

		for (int i = 0; i < ergebnis.length; i++)
			for (int j = 0; j < ergebnis[i].length; j++)
				for (int k = 0; k < matA[0].length; k++)
					ergebnis[i][j] += matA[i][k] * matB[k][j];

		return ergebnis;
	}
}
