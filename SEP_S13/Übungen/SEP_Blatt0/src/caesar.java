public class caesar {

	public String chiffre(String s, int v) {

		StringBuilder ergebnis = new StringBuilder();

		for (int i = 0; i < s.length(); i++)
			ergebnis.append((char) (((int) s.charAt(i) - 96 + v) % 26 + 96));

		return ergebnis.toString();
	}

	public static void main(String args[]) {

		caesar c = new caesar();

		System.out.println("Casesar-Chiffre von 'haus' mit 1 ist "
				+ c.chiffre("haus", 1) + " richtig ist: ibvt");
		System.out.println("Casesar-Chiffre von 'bauzaun' mit 8 ist "
				+ c.chiffre("bauzaun", 8) + " richtig ist: jichicv");
		System.out.println("Casesar-Chiffre von 'hundert' mit 10 ist "
				+ c.chiffre("hundert", 10) + " richtig ist: rexnobd");
		System.out.println("Casesar-Chiffre von 'rexnobd' mit 16 ist "
				+ c.chiffre("rexnobd", 16) + " richtig ist: hundert");
	}
}