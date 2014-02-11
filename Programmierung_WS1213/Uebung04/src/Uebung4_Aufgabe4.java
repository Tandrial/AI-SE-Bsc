public class Uebung4_Aufgabe4 {
	static public int[] prime;

	public static void main(String[] args) {
		int start = 2;
		int ende = 100000;

		// Methode 1
		int count = 0;
		long startZeit = System.nanoTime();
		for (int i = start; i < ende; i++) {
		//	if (primecheck(i))
				count++;
		}
		long stop = System.nanoTime();

		System.out.println(count + " Primzahlen gefunden. Dauer: "
				+ (stop - startZeit) / Math.pow(10, 9) + " s");

		// Methode 2
		count = 0;
		startZeit = System.nanoTime();
		for (int i = start; i < ende; i++) {
			if (primecheckA3(i))
				count++;
		}
		stop = System.nanoTime();

		System.out.println(count + " Primzahlen gefunden. Dauer: "
				+ (stop - startZeit) / Math.pow(10, 9) + " s");

		// Method 3
		prime = new int[count];
		count = 0;
		startZeit = System.nanoTime();

		for (int i = start; i < ende; i++) {
			if (primecheckA4(i)) {
				prime[count++] = i;
			}
		}
		stop = System.nanoTime();

		System.out.println(count + " Primzahlen gefunden. Dauer: "
				+ (stop - startZeit) / Math.pow(10, 9) + " s");

		// Methode 4
		count = 0;
		startZeit = System.nanoTime();
		for (int i = start; i < ende; i++) {
			if (isPrime(i))
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

	static public boolean isPrime(int n) {
		// check if n is a multiple of 2
		if (n % 2 == 0 && n > 2)
			return false;
		// if not, then just check the odds		
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	static public boolean primecheckA3(int n) {
		if ((n % 2 == 0) && (n > 2))
			return false;
		if ((n % 3 == 0) && (n > 3))
			return false;

		double sq = Math.sqrt(n);
		
		for (int i = 2; i <= sq; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	static public boolean primecheckA4(int n) {
		if ((n % 2 == 0) && (n > 2))
			return false;
		if ((n % 3 == 0) && (n > 3))
			return false;

		for (int i = 2; i <= prime.length && prime[i] != 0; i++) {
			if (n % prime[i] == 0)
				return false;
		}
		return true;
	}

}
