public class fibonacci {

	// Fibonacci rekursiv
	public int fibonaccirek(int f) {
		int ergebnis = 0;
		if (f <= 2)
			ergebnis = 1;
		else
			ergebnis = fibonaccirek(f - 2) + fibonaccirek(f - 1);
		return ergebnis;
	}

	// Fiboncacci iterativ
	public int fibonacciiter(int f) {
		int ergebnis = 0;
		int f_2 = 1;
		int f_1 = 1;

		for (int i = 3; i <= f; i++) {
			ergebnis = f_2 + f_1;
			f_1 = f_2;
			f_2 = ergebnis;
		}

		return ergebnis;
	}

	public static void main(String args[]) {

		fibonacci f = new fibonacci();

		System.out.println("fib(5) = " + f.fibonacciiter(5) + " (iterativ)");
		System.out.println("fib(5) = " + f.fibonaccirek(5) + " (rekursiv)");
		System.out.println("Richtig ist fib(5) = 5");

		System.out.println("fib(15) = " + f.fibonacciiter(15) + " (iterativ)");
		System.out.println("fib(15) = " + f.fibonaccirek(15) + " (rekursiv)");
		System.out.println("Richtig ist fib(15) = 610");

		System.out.println("fib(40) = " + f.fibonacciiter(40) + " (iterativ)");
		System.out.println("fib(40) = " + f.fibonaccirek(40) + " (rekursiv)");
		System.out.println("Richtig ist fib(40) = 102334155");
		System.out.println("Warum ist der rekurisve Algo nur so langsam...?");
	}
}