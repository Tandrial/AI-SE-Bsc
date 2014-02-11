public class Uebung3_Aufgabe3 {

	public static void main(String[] args) {

		int a = 1;
		int b = 2;
		int c = 1;
		if (a == b)
			System.out.println("Fall 1");
		else {
			if (a == c) {
				System.out.println("Fall 2");
			}
			if (b == c)
				System.out.println("Fall 3");
			else if (a % 2 == 1) {
				if (b % 2 == 0 && c % 2 != 1)
					System.out.println("Fall 4");
			}
			System.out.println("Fall 5");
		}

	}

}
