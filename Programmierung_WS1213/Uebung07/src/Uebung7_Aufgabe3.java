public class Uebung7_Aufgabe3 {

	static int c(int n) {
		if (n == 1)
			return 0;

		if (n % 2 == 0)
			return 1 + c(n / 2);
		else
			return 1 + c(3 * n + 1);
	}

	public static void main(String[] args) {
		for (int i = 1; i < 10; i++) {
			System.out.println(c(i));			
		}
	}
}
