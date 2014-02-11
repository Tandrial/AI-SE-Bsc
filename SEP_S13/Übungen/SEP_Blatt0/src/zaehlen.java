public class zaehlen {

	public int count(String s, char z) {
		int ergebnis = 0;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == z)
				ergebnis++;

		return ergebnis;
	}

	public static void main(String args[]) {

		zaehlen a = new zaehlen();

		int ergebnis1 = a.count("Das Leben ist schoen", 'e');
		int ergebnis2 = a.count("Das Leben ist schoen", 'l');
		int ergebnis3 = a.count("Das Leben ist schoen", 'u');

		System.out.println("Der String lautet: Das Leben ist schoen");
		System.out.println("Ergebnis der Methode zaehlen fuer e ist "
				+ ergebnis1 + ", richtig ist 3");
		System.out.println("Ergebnis der Methode zaehlen fuer l ist "
				+ ergebnis2 + ", richtig ist 0");
		System.out.println("Ergebnis der Methode zaehlen fuer u ist "
				+ ergebnis3 + ", richtig ist 0");
	}
}