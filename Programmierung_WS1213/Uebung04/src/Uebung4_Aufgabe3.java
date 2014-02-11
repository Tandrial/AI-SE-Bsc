public class Uebung4_Aufgabe3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int start = 2;
		int ende = 100000;

		int count = 0;
		long startZeit = System.nanoTime();

		for (int i = start; i < ende; i++) {
			// if (primecheck(i))
			count++;
		}
		long stop = System.nanoTime();

		System.out.println(count + " Primzahlen gefunden. Dauer: "
				+ (stop - startZeit) / Math.pow(10, 9) + " s");

		count = 0;
		startZeit = System.nanoTime();

		for (int i = start; i < ende; i++) {
			if (primecheckA3(i))
				count++;
		}
		stop = System.nanoTime();

		System.out.println(count + " Primzahlen gefunden. Dauer: "
				+ (stop - startZeit) / Math.pow(10, 9) + " s");

	}

	static public boolean primecheck(int n) {
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	static public boolean primecheckA3(int n) {
		if ((n % 2 == 0) && (n > 2))
			return false;

		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

}
