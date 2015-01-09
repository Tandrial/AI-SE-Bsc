public class Uebung4_Aufgabe1 {
	public static void main(String[] args) {
		int start = 2;
		int ende = 100000;

		for (int i = start; i < ende; i++) {
			if (primecheck(i))
				System.out.println(i);
		}
	}

	static public boolean primecheck(int n) {
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;

	}

}
