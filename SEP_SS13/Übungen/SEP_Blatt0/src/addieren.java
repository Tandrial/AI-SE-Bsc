public class addieren {

	public int add(int n) {
		int ergebnis = 0;		
		ergebnis = n * (n + 1) / 2;
		// for (; n > 0; ergebnis += n--);

		return ergebnis;
	}

	public static void main(String args[]) {

		addieren a = new addieren();

		int ergebnis1 = a.add(1);
		int ergebnis3 = a.add(3);
		int ergebnis6 = a.add(6);
		int ergebnis100 = a.add(100);

		System.out.println("addieren(1)   hat " + ergebnis1
				+ " berechnet, richtig ist 1");
		System.out.println("addieren(3)   hat " + ergebnis3
				+ " berechnet, richtig ist 6");
		System.out.println("addieren(6)   hat " + ergebnis6
				+ " berechnet, richtig ist 21");
		System.out.println("addieren(100) hat " + ergebnis100
				+ " berechnet, richtig ist 5050");
	}
}