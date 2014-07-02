class eratosthenes {

	public void sieb(int max) {
		boolean[] notPrime = new boolean[max + 1];

		for (int i = 2; i < notPrime.length; i++) {
			if (notPrime[i])
				continue;
			for (int j = i + 1; j < notPrime.length; j++) {
				if (notPrime[j])
					continue;
				if (j % i == 0)
					notPrime[j] = true;
			}
		}
		for (int i = 1; i <= max; i++) {
			if (!notPrime[i])
				System.out.print(i + " ");
		}
	}

	public static void main(String args[]) {

		eratosthenes s = new eratosthenes();

		System.out.println();
		System.out.println("Sieb der ersten 50 Zahlen");
		System.out.println("Erwartetes Ergebnis:");
		System.out.println("1 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47");
		System.out.println("Ihre Loesung:");
		s.sieb(50);

		System.out.println();
		System.out.println();
		System.out.println("Sieb der ersten 100 Zahlen");
		System.out.println("Erwartetes Ergebnis:");
		System.out
				.println("1 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97");
		System.out.println("Ihre Loesung:");
		s.sieb(100);
	}
}